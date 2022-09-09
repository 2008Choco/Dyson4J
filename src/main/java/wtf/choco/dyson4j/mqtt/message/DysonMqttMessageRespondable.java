package wtf.choco.dyson4j.mqtt.message;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an MQTT message for a Dyson fan that expects a response from the MQTT server.
 *
 * @param <R> the expected result
 */
public abstract class DysonMqttMessageRespondable<R> extends DysonMqttMessage<R> {

    /**
     * Dyson MQTT response message id for "CURRENT-STATE", sent in response to
     * {@link DysonMqttMessage#MESSAGE_ID_REQUEST_CURRENT_STATE}
     */
    protected static final String MESSAGE_ID_RESPONSE_CURRENT_STATE = "CURRENT-STATE";
    /**
     * Dyson MQTT response message id for "ENVIRONMENTAL-CURRENT-SENSOR-DATA", sent in response to
     * {@link DysonMqttMessage#MESSAGE_ID_REQUEST_CURRENT_STATE}
     */
    protected static final String MESSAGE_ID_RESPONSE_ENVIRONMENTAL_CURRENT_SENSOR_DATA = "ENVIRONMENTAL-CURRENT-SENSOR-DATA";

    private final String expectedResponseMessageId;

    /**
     * Construct a new {@link DysonMqttMessageRespondable}.
     *
     * @param messageId the id of the message to send
     * @param expectedResponseMessageId the id of the message expected to be received as
     * a response
     *
     * @see #MESSAGE_ID_RESPONSE_CURRENT_STATE
     * @see #MESSAGE_ID_RESPONSE_ENVIRONMENTAL_CURRENT_SENSOR_DATA
     */
    public DysonMqttMessageRespondable(@NotNull String messageId, @NotNull String expectedResponseMessageId) {
        super(messageId);
        this.expectedResponseMessageId = expectedResponseMessageId;
    }

    /**
     * Get the id of the message expected as a response to this message.
     *
     * @return the message id
     */
    @NotNull
    public String getExpectedResponseMessageId() {
        return expectedResponseMessageId;
    }

    /**
     * Called when a response has been received on the MQTT channel for this message.
     *
     * @param response the response object
     *
     * @return the result
     */
    @NotNull
    public abstract R handleResponse(@NotNull JsonObject response);

}
