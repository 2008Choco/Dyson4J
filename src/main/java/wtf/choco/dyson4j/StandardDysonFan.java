package wtf.choco.dyson4j;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.jetbrains.annotations.NotNull;

import wtf.choco.dyson4j.exception.DysonConnectionException;
import wtf.choco.dyson4j.mqtt.FanMqttConnectionHandler;
import wtf.choco.dyson4j.mqtt.message.DysonMqttMessageRequestCurrentState;
import wtf.choco.dyson4j.mqtt.message.DysonMqttMessageRequestEnvironmentalSensorData;
import wtf.choco.dyson4j.mqtt.message.DysonMqttMessageSetState;
import wtf.choco.dyson4j.setting.FanState;
import wtf.choco.dyson4j.setting.MqttStateValueProvider;
import wtf.choco.dyson4j.util.ExceptionalSupplier;
import wtf.choco.dyson4j.util.Preconditions;

/**
 * A standard {@link DysonFan}.
 */
public final class StandardDysonFan implements DysonFan {

    private FanMqttConnectionHandler mqttConnection;
    private final ExceptionalSupplier<FanMqttConnectionHandler, MqttException> mqttConnectionSupplier;

    private final FanModel model;
    private final InetAddress address;

    /**
     * Construct a new {@link StandardDysonFan}.
     *
     * @param model the fan's model
     * @param address the address at which the Dyson fan may be located
     * @param credentials the fan credentials (as found on the fan's sticker or manual)
     */
    public StandardDysonFan(@NotNull FanModel model, @NotNull InetAddress address, @NotNull DysonFanCredentials credentials) {
        Preconditions.checkArgument(model != null, "model must not be null");
        Preconditions.checkArgument(address != null, "address must not be null");
        Preconditions.checkArgument(credentials != null, "credentials must not be null");

        this.model = model;
        this.address = address;
        this.mqttConnectionSupplier = () -> new FanMqttConnectionHandler(this, credentials);
    }

    @Override
    public CompletableFuture<DysonFan> connect(int timeout, TimeUnit unit) {
        Preconditions.checkArgument(timeout > 0, "timeout must be > 0");
        Preconditions.checkArgument(unit != null, "unit must not be null");

        return getConnectionHandler().thenCompose(connection -> connection.connect(timeout, unit))
                .exceptionally(e -> {
                    throw new DysonConnectionException("Could not connect to Dyson fan", e);
                })
                .thenApply(ignore -> this);
    }

    @Override
    public boolean isConnected() {
        return mqttConnection != null && mqttConnection.isConnected();
    }

    @Override
    public FanModel getModel() {
        return model;
    }

    @Override
    public InetAddress getAddress() {
        return address;
    }

    @Override
    public boolean supportsFeature(FanState<?> state) {
        Preconditions.checkArgument(state != null, "state must not be null");
        return model.supportsFeature(state);
    }

    @Override
    public <T extends MqttStateValueProvider> CompletableFuture<Void> setState(FanState<T> state, T value) {
        Preconditions.checkArgument(state != null, "state must not be null");
        Preconditions.checkArgument(value != null, "value must not be null");

        if (!isConnected()) {
            throw new DysonConnectionException("Not connected to Dyson fan.");
        }

        if (!model.supportsFeature(state)) {
            return CompletableFuture.completedFuture(null);
        }

        return getConnectionHandler().thenCompose(connection -> connection.sendMessage(new DysonMqttMessageSetState(state, value)));
    }

    @Override
    public CompletableFuture<Void> setState(Consumer<MultiStateChange> change) {
        Preconditions.checkArgument(change != null, "change must not be null");

        if (!isConnected()) {
            throw new DysonConnectionException("Not connected to Dyson fan.");
        }

        StandardMultiStateChange standardChange = new StandardMultiStateChange();
        change.accept(standardChange);

        // Remove any unsupported states
        standardChange.states.keySet().removeIf(state -> !model.supportsFeature(state));

        return getConnectionHandler().thenCompose(connection -> connection.sendMessage(new DysonMqttMessageSetState(standardChange.states)));
    }

    @Override
    public CompletableFuture<DeviceStatus> requestCurrentState() {
        if (!isConnected()) {
            throw new DysonConnectionException("Not connected to Dyson fan.");
        }

        return getConnectionHandler().thenCompose(connection -> connection.sendMessage(new DysonMqttMessageRequestCurrentState()));
    }

    @Override
    public CompletableFuture<EnvironmentalSensorData> requestEnvironmentalSensorData() {
        if (!isConnected()) {
            throw new DysonConnectionException("Not connected to Dyson fan");
        }

        return getConnectionHandler().thenCompose(connection -> connection.sendMessage(new DysonMqttMessageRequestEnvironmentalSensorData()));
    }

    @Override
    public boolean hasPendingMessages() {
        return isConnected() && mqttConnection.getPendingMessages() > 0;
    }

    @Override
    public int getPendingMessages() {
        return isConnected() ? mqttConnection.getPendingMessages() : 0;
    }

    @Override
    public CompletableFuture<Void> disconnect(boolean destroy) {
        if (!isConnected()) {
            return CompletableFuture.completedFuture(null);
        }

        return mqttConnection.disconnect(destroy).exceptionally(e -> {
            throw new DysonConnectionException("Could not disconnect from fan", e);
        });
    }

    private CompletableFuture<FanMqttConnectionHandler> getConnectionHandler() {
        if (mqttConnection != null) {
            return CompletableFuture.completedFuture(mqttConnection);
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                return (mqttConnection = mqttConnectionSupplier.get());
            } catch (MqttException e) {
                throw new CompletionException(e);
            }
        });
    }

    private final class StandardMultiStateChange implements MultiStateChange {

        private Map<FanState<?>, MqttStateValueProvider> states = null;

        @Override
        public <T extends MqttStateValueProvider> MultiStateChange setState(FanState<T> state, T value) {
            Preconditions.checkArgument(state != null, "state must not be null");
            Preconditions.checkArgument(value != null, "value must not be null");

            if (model.supportsFeature(state)) {
                if (states == null) {
                    this.states = new HashMap<>();
                }

                this.states.put(state, value);
            }

            return this;
        }

    }

}
