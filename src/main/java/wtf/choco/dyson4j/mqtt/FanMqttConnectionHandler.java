package wtf.choco.dyson4j.mqtt;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.jetbrains.annotations.NotNull;

import wtf.choco.dyson4j.DysonFan;
import wtf.choco.dyson4j.DysonFanCredentials;
import wtf.choco.dyson4j.StandardDysonFan;
import wtf.choco.dyson4j.exception.DysonConnectionException;
import wtf.choco.dyson4j.mqtt.message.DysonMqttMessage;
import wtf.choco.dyson4j.mqtt.message.DysonMqttMessageRespondable;
import wtf.choco.dyson4j.util.Preconditions;

/**
 * An internal MQTT connection client handler. Not meant for external use. It is strongly
 * advised to make use of a {@link StandardDysonFan} instead for a more robust API.
 */
public final class FanMqttConnectionHandler {

    private final String topicCommand, topicStatusCurrent;

    private final UUID clientId;
    private final IMqttAsyncClient client;
    private final MqttConnectOptions connectionOptions;
    private final MqttStatusMessageListener statusListener = new MqttStatusMessageListener();

    private final AtomicInteger pendingMessages = new AtomicInteger();

    /**
     * Construct a new {@link FanMqttConnectionHandler}.
     *
     * @param fan the {@link DysonFan} instance to which this connection handler belongs
     * @param credentials the fan credentials used to connect to its MQTT server
     *
     * @throws MqttException if an MQTT exception occurred while connecting
     */
    public FanMqttConnectionHandler(@NotNull DysonFan fan, @NotNull DysonFanCredentials credentials) throws MqttException {
        Preconditions.checkArgument(fan != null, "fan must not be null");
        Preconditions.checkArgument(credentials != null, "credentials must not be null");

        this.clientId = UUID.randomUUID();

        /*
         * A Dyson fan's MQTT server can be located under the TCP protocol on port 1883. We can
         * use a random client id though as this does not matter to us. Additionally, we do not
         * need any persistent storage, so null will suffice as per the library's specification.
         */
        this.client = new MqttAsyncClient("tcp://" + fan.getAddress().getHostAddress() + ":1883", clientId.toString(), null);

        this.connectionOptions = new MqttConnectOptions();
        this.connectionOptions.setCleanSession(true);
        this.connectionOptions.setUserName(credentials.getUsername());
        this.connectionOptions.setPassword(credentials.getPasswordHashed().toCharArray());

        String topicPrefix = fan.getModel().getProductCode() + "/" + credentials.getUsername();
        this.topicCommand = topicPrefix + "/command";
        this.topicStatusCurrent = topicPrefix + "/status/current";
    }

    /**
     * Check whether or not the internal MQTT client is connected.
     *
     * @return true if connected, false otherwise
     */
    public boolean isConnected() {
        return client.isConnected();
    }

    /**
     * Connect the MQTT client to the fan's address using internal connection options.
     *
     * @param timeout the maximum amount of time until the connection will timeout and
     * fail
     * @param unit the time unit used for {@code timeout}
     *
     * @return a completable future, completed when a connection has been established
     *
     * @throws DysonConnectionException (in the CompletableFuture) if a connection to
     * the fan could not be established within a reasonable amount of time
     *
     * @see DysonFan#connect()
     */
    @NotNull
    public CompletableFuture<Void> connect(int timeout, @NotNull TimeUnit unit) {
        Preconditions.checkArgument(timeout > 0, "timeout must be > 0");
        Preconditions.checkArgument(unit != null, "unit must not be null");

        CompletableFuture<Void> future = new CompletableFuture<>();

        try {
            this.connectionOptions.setConnectionTimeout((int) unit.toSeconds(timeout));

            this.client.connect(connectionOptions, null, ICompoundMqttActionListener.of((token, optionalException) -> {
                optionalException.ifPresentOrElse(
                        future::completeExceptionally,
                        () -> {
                            try {
                                this.client.subscribe(topicStatusCurrent, 0, statusListener);
                                future.complete(null);
                            } catch (MqttException e) {
                                future.completeExceptionally(e);
                            }
                        }
                );
            }));
        } catch (MqttException e) {
            future.completeExceptionally(e);
        }

        return future;
    }

    /**
     * Disconnect the MQTT client from the fan.
     *
     * @param destroy whether or not to destroy the connection. If true, the client is no longer
     * usable and a new connection handler needs to be created. If false, {@link #connect(int, TimeUnit)}
     * may be called again to reconnect
     *
     * @return a completable future, completed when the disconnection has finalized
     *
     * @throws DysonConnectionException (in the CompletableFuture) if the client failed to
     * disconnect, or some other communication error occurs
     */
    @NotNull
    public CompletableFuture<Void> disconnect(boolean destroy) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        try {
            // TODO: Unsubscribe from the client's status topic
            this.client.disconnect(null, ICompoundMqttActionListener.of((token, optionalException) -> {
                optionalException.ifPresentOrElse(
                    future::completeExceptionally,
                    () -> future.complete(null)
                );
            }));
        } catch (MqttException e) {
            future.completeExceptionally(e);
        }

        // If we want to destroy the connection, we need to append another stage
        if (destroy) {
            /*
             * Have to early return because of the use of the lambda above in the optionalException.
             * This is fine though because we're not doing anything after this anyways
             */
            return future.thenRun(() -> {
                try {
                    this.client.close();
                } catch (MqttException e) {
                    /*
                     * Should never happen because we know we're disconnected by this point, but
                     * we'll be ABSOLUTELY CERTAIN, and handle the exception.
                     */
                    throw new CompletionException(e);
                }
            });
        }

        return future;
    }

    /**
     * Get the amount of pending MQTT messages in the pipeline waiting to be received
     * by the fan's MQTT server.
     *
     * @return the amount of pending messages
     */
    public int getPendingMessages() {
        return pendingMessages.get();
    }

    /**
     * Send a {@link DysonMqttMessage} to the fan's MQTT server and possibly receive
     * a response.
     *
     * @param <R> the message's response type
     * @param message the message to send
     *
     * @return a completable future, completed when a response has been received and
     * was handled by the message implementation containing the response object, or
     * completed when the fan receive's the message if the message does not expect
     * a response from the MQTT server.
     */
    @NotNull
    public <R> CompletableFuture<R> sendMessage(@NotNull DysonMqttMessage<R> message) {
        Preconditions.checkArgument(message != null, "message must not be null");

        MqttMessage mqttMessage = message.asMqttMessage();
        mqttMessage.setQos(0);

        this.pendingMessages.incrementAndGet();

        CompletableFuture<R> future = new CompletableFuture<R>()
                .whenComplete((ignore, e) -> pendingMessages.decrementAndGet());

        try {
            this.client.publish(topicCommand, mqttMessage, null, ICompoundMqttActionListener.of((token, optionalException) -> {
                optionalException.ifPresentOrElse(
                        future::completeExceptionally,
                        () -> {
                            if (!(message instanceof DysonMqttMessageRespondable<R> respondableMessage)) {
                                future.complete(null);
                                return;
                            }

                            this.statusListener.pendingResponses.add(new PendingResponse<>(respondableMessage, future));
                        }
                );
            }));
        } catch (MqttException e) {
            throw new CompletionException(e);
        }

        return future;
    }

    /*
     * This is a bit of a hack, but it's the only way that could be found to listen for messages that have a
     * specific response after a request message. There isn't a guarantee that they'll be received in order,
     * but it doesn't matter too much given that the message responses should be identical and are not affected
     * by input in the request messages.
     *
     * We're essentially holding the respondable message instance that was sent, as well as the CompletableFuture
     * that needs to be completed once a response has been received from the MQTT server. If there are any
     * incoming messages that match the message's expected response message id (e.g. "CURRENT-STATE" for any
     * "STATE-SET" messages), then we'll just assume it's for this message even though it technically may not be.
     *
     * If someone can come up with a better way to handle this more reliably via MQTT, a pull request would be
     * appreciated. I'm not sure Dyson nor Eclipse Paho exposes a way to retain this sort of information between
     * messages. I think this is something Dyson would have had to have implemented in their messaging protocol.
     */
    private final class PendingResponse<R> {

        private final DysonMqttMessageRespondable<R> message;
        private final CompletableFuture<R> future;

        private PendingResponse(DysonMqttMessageRespondable<R> message, CompletableFuture<R> future) {
            this.message = message;
            this.future = future;
        }

        private boolean handleResponse(String messageId, JsonObject response) {
            String expectedMessageId = message.getExpectedResponseMessageId();
            if (!Objects.equals(expectedMessageId, messageId)) {
                return false;
            }

            R responseObject;
            try {
                responseObject = message.handleResponse(response);
            } catch (Exception e) {
                this.future.completeExceptionally(e);
                return true;
            }

            this.future.complete(responseObject);
            return true;
        }

    }

    private final class MqttStatusMessageListener implements IMqttMessageListener {

        private final Gson gson = new Gson();
        private final List<PendingResponse<?>> pendingResponses = new ArrayList<>(8);

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            String messageContents = new String(message.getPayload(), StandardCharsets.UTF_8);

            JsonObject object;
            try {
                object = gson.fromJson(messageContents, JsonObject.class);
            } catch (JsonParseException e) {
                System.err.println("Failed to parse incoming MQTT message: \"" + messageContents + "\"");
                return;
            }

            String messageId = object.get("msg").getAsString();

            for (int i = 0; i < pendingResponses.size(); i++) {
                PendingResponse<?> response = pendingResponses.get(i);
                if (!response.handleResponse(messageId, object)) {
                    continue;
                }

                this.pendingResponses.remove(i);
                break;
            }
        }

    }

}
