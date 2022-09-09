package wtf.choco.dyson4j;

import java.net.InetAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import wtf.choco.dyson4j.exception.DysonConnectionException;
import wtf.choco.dyson4j.setting.FanState;
import wtf.choco.dyson4j.setting.MqttStateValueProvider;

/**
 * Represents a Dyson fan.
 *
 * @see StandardDysonFan
 */
public interface DysonFan {

    /**
     * Connect to the Dyson fan. Once connected, the fan may accept state changes and
     * queries. This operation is not instant and will be run asynchronously.
     *
     * @param timeout the maximum amount of time until the connection will timeout and
     * fail. Must be greater than 0
     * @param unit the time unit used for {@code timeout}
     *
     * @return a completable future containing this instance, completed when a
     * connection to the fan has been established
     *
     * @throws DysonConnectionException (in the CompletableFuture) if a connection to
     * the fan could not be established within a reasonable amount of time
     */
    @NotNull
    public CompletableFuture<DysonFan> connect(int timeout, @NotNull TimeUnit unit);

    /**
     * Connect to the Dyson fan. Once connected, the fan may accept state changes and
     * queries. This operation is not instant and will be run asynchronously. Connection
     * attempts will automatically timeout after 5 seconds unless otherwise specified
     * by {@link #connect(int, TimeUnit)}.
     *
     * @return a completable future containing this instance, completed when a
     * connection to the fan has been established
     *
     * @throws DysonConnectionException (in the CompletableFuture) if a connection to
     * the fan could not be established within a reasonable amount of time
     *
     * @see #connect(int, TimeUnit)
     */
    @NotNull
    public default CompletableFuture<DysonFan> connect() {
        return connect(5, TimeUnit.SECONDS);
    }

    /**
     * Check whether or not a connection to the Dyson fan has been established. If this
     * method returns false, any state changes or queries will throw a
     * {@link DysonConnectionException}.
     *
     * @return true if connected, false otherwise
     */
    public boolean isConnected();

    /**
     * Get this Dyson fan's {@link FanModel model}.
     *
     * @return the model
     */
    @NotNull
    public FanModel getModel();

    /**
     * Get the {@link InetAddress} where this fan is located on the network.
     *
     * @return the address
     */
    @NotNull
    public InetAddress getAddress();

    /**
     * Check whether or not the provided {@link FanState} is supported by this DysonFan's
     * {@link FanModel model}.
     * <p>
     * This is a convenience method. Equivalent to {@code getModel().isSupported(state)}.
     *
     * @param state the state to check
     *
     * @return true if supported, false otherwise
     */
    public boolean supportsFeature(@NotNull FanState<?> state);

    /**
     * Set a single state on this fan. If setting multiple values is desired, it is advised
     * to use {@link #setState(Consumer)} instead so that all state changes may be made in a
     * single request to the fan.
     * <p>
     * The fan must be {@link #isConnected() connected}. This method will be performed
     * asynchronously.
     *
     * @param <T> the state value type
     * @param state the state to change
     * @param value the value to which the state should be set
     *
     * @return a completable future, completed when the state has been sent to the fan
     *
     * @throws DysonConnectionException if the Dyson fan is not connected, or in the
     * CompletableFuture if some other communication error occurs
     */
    @NotNull
    public <T extends MqttStateValueProvider> CompletableFuture<Void> setState(@NotNull FanState<T> state, @NotNull T value);

    /**
     * Set multiple states on this fan in a single query. This will update more than one state
     * on the fan in a single query.
     * <p>
     * The fan must be {@link #isConnected() connected}. This method will be performed
     * asynchronously.
     *
     * @param change a consumer containing a {@link MultiStateChange} instance, which may be
     * used to build a sequence of state changes to send to the fan
     *
     * @return a completable future, completed when the states have been sent to the fan
     *
     * @throws DysonConnectionException if the Dyson fan is not connected, or in the
     * CompletableFuture if some other communication error occurs
     */
    @NotNull
    public CompletableFuture<Void> setState(@NotNull Consumer<@NotNull MultiStateChange> change);

    /**
     * Request the device's current {@link DeviceStatus status}.
     * <p>
     * The fan must be {@link #isConnected() connected}. This method will be performed
     * asynchronously.
     *
     * @return a completable future, completed when the device status has been received
     *
     * @throws DysonConnectionException if the Dyson fan is not connected, or in the
     * CompletableFuture if some other communication error occurs
     */
    @NotNull
    public CompletableFuture<DeviceStatus> requestCurrentState();

    /**
     * Request the device's current {@link EnvironmentalSensorData sensor data}.
     * <p>
     * The fan must be {@link #isConnected() connected}. This method will be performed
     * asynchronously.
     *
     * @return a completable future, completed when the device sensor data has been received
     *
     * @throws DysonConnectionException if the Dyson fan is not connected, or in the
     * CompletableFuture if some other communication error occurs
     */
    @NotNull
    public CompletableFuture<EnvironmentalSensorData> requestEnvironmentalSensorData();

    /**
     * Check whether or not this fan has any messages pending.
     * <p>
     * Every operation on the fan that requires communication will be tallied as a pending
     * message until the message has been successfully received by the fan. So long as there
     * are pending messages, some operation has not yet been received by the fan.
     *
     * @return true if there are pending messages, false otherwise
     */
    public boolean hasPendingMessages();

    /**
     * Get the amount of pending messages.
     * <p>
     * Every operation on the fan that requires communication will be tallied as a pending
     * message until the message has been successfully received by the fan. So long as there
     * are pending messages, some operation has not yet been received by the fan.
     *
     * @return the amount of pending messages
     */
    public int getPendingMessages();

    /**
     * Disconnect from the Dyson fan.
     * <p>
     * The fan must be {@link #isConnected() connected}. This method will be performed
     * asynchronously.
     *
     * @param destroy whether or not to destroy the connection. If true, this DysonFan instance
     * will not be reusable. If false, {@link #connect()} may be called again to reconnect
     *
     * @return a completable future, completed when the disconnection has finalized
     *
     * @throws DysonConnectionException if the Dyson fan was not yet connected, or in the
     * CompletableFuture if the fan failed to disconnect (for some reason), or some other
     * communication error occurs
     */
    @NotNull
    public CompletableFuture<Void> disconnect(boolean destroy);

    /**
     * Disconnect from the Dyson fan. This method will destroy the connection to the
     * fan's MQTT client and will not allow for reconnection.
     * <p>
     * The fan must be {@link #isConnected() connected}. This method will be performed
     * asynchronously.
     * <p>
     * This is a convenience method. Equivalent to {@code disconnect(true)}.
     *
     * @return a completable future, completed when the disconnection has finalized
     *
     * @throws DysonConnectionException if the Dyson fan was not yet connected, or in the
     * CompletableFuture if the fan failed to disconnect (for some reason), or some other
     * communication error occurs
     */
    @NotNull
    public default CompletableFuture<Void> disconnect() {
        return disconnect(true);
    }

}
