package wtf.choco.dyson4j.setting;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The fan's oscillation state.
 */
public enum FanOscillation implements MqttStateValueProvider {

    /**
     * The fan is oscillating back and forth within a 90 degree area.
     */
    ON("ON"),

    /**
     * The fan is not oscillating, and pointing in a single direction.
     */
    OFF("OFF");

    private static final Map<String, FanOscillation> BY_ID = new HashMap<>();

    static {
        for (FanOscillation oscillation : values()) {
            BY_ID.put(oscillation.getStateValue(), oscillation);
        }
    }

    private final String stateValue;

    private FanOscillation(String stateValue) {
        this.stateValue = stateValue;
    }

    @Override
    public String getStateValue() {
        return stateValue;
    }

    /**
     * Get a {@link FanOscillation} by its internal MQTT id.
     *
     * @param id the MQTT id
     *
     * @return the value that matches the given id, or null if none exists
     */
    @Nullable
    public static FanOscillation getById(@NotNull String id) {
        return BY_ID.get(id);
    }

}
