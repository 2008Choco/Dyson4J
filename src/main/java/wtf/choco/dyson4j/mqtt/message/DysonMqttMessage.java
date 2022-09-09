package wtf.choco.dyson4j.mqtt.message;

import com.google.gson.JsonObject;

import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a supported MQTT message for a Dyson fan.
 *
 * @param <R> the expected result
 */
public abstract class DysonMqttMessage<R> {

    /**
     * Dyson MQTT message id for "STATE-SET".
     */
    protected static final String MESSAGE_ID_STATE_SET = "STATE-SET";
    /**
     * Dyson MQTT message id for "REQUEST-CURRENT-STATE".
     */
    protected static final String MESSAGE_ID_REQUEST_CURRENT_STATE = "REQUEST-CURRENT-STATE";

    private final String messageId;

    /**
     * Construct a new {@link DysonMqttMessage}.
     *
     * @param messageId the id of the message to send
     *
     * @see #MESSAGE_ID_STATE_SET
     * @see #MESSAGE_ID_REQUEST_CURRENT_STATE
     */
    public DysonMqttMessage(@NotNull String messageId) {
        this.messageId = messageId;
    }

    /**
     * Populate the message payload with additional JSON data.
     *
     * @param object the payload object
     */
    protected void populateAdditionalData(@NotNull JsonObject object) { }

    /**
     * Get this message as a {@link JsonObject}.
     *
     * @return the message object
     */
    @NotNull
    public final JsonObject asJsonObject() {
        JsonObject object = new JsonObject();

        object.addProperty("msg", messageId);
        object.addProperty("time", getNowTimestamp());
        this.populateAdditionalData(object);

        return object;
    }

    /**
     * Get this message as a {@link MqttMessage}.
     *
     * @return the MQTT message
     */
    @NotNull
    public final MqttMessage asMqttMessage() {
        return new MqttMessage(asJsonObject().toString().getBytes(StandardCharsets.UTF_8));
    }

    private String getNowTimestamp() {
        return ZonedDateTime.now(ZoneId.of("GMT")).format(DateTimeFormatter.ISO_INSTANT);
    }

}
