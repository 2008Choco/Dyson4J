package wtf.choco.dyson4j.mqtt.message;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jetbrains.annotations.NotNull;

import wtf.choco.dyson4j.setting.FanState;
import wtf.choco.dyson4j.setting.MqttStateValueProvider;

/**
 * A {@link DysonMqttMessage} that sets one or more of the Dyson fan's states/settings.
 */
public final class DysonMqttMessageSetState extends DysonMqttMessage<Void> {

    private static final String DATA_KEY = "data";

    private final Map<FanState<?>, MqttStateValueProvider> states;

    /**
     * Construct a new {@link DysonMqttMessageSetState}.
     *
     * @param states the states to set
     */
    public DysonMqttMessageSetState(@NotNull Map<FanState<?>, MqttStateValueProvider> states) {
        super(MESSAGE_ID_STATE_SET);
        this.states = new HashMap<>(states);
    }

    /**
     * Construct a new {@link DysonMqttMessageSetState}.
     *
     * @param <T> the state value type
     * @param state the state to set
     * @param value the new value of the state
     */
    public <T extends MqttStateValueProvider> DysonMqttMessageSetState(@NotNull FanState<T> state, @NotNull T value) {
        this(Map.of(state, value));
    }

    @Override
    protected void populateAdditionalData(JsonObject object) {
        JsonObject data = new JsonObject();

        for (Entry<FanState<?>, MqttStateValueProvider> state : states.entrySet()) {
            data.addProperty(state.getKey().getId(), state.getValue().getStateValue());
        }

        object.add(DATA_KEY, data);
    }

}
