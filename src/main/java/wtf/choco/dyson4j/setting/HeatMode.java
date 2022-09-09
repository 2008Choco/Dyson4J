package wtf.choco.dyson4j.setting;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The heater mode of a fan.
 */
public enum HeatMode implements MqttStateValueProvider {

    /**
     * The heater is enabled.
     */
    HEAT("HEAT"),

    /**
     * The heater is disabled (cool air instead).
     */
    OFF("OFF");

    private static final Map<String, HeatMode> BY_ID = new HashMap<>();

    static {
        for (HeatMode mode : values()) {
            BY_ID.put(mode.getStateValue(), mode);
        }
    }

    private final String stateValue;

    private HeatMode(String stateValue) {
        this.stateValue = stateValue;
    }

    @Override
    public String getStateValue() {
        return stateValue;
    }

    /**
     * Get a {@link HeatMode} by its internal MQTT id.
     *
     * @param id the MQTT id
     *
     * @return the value that matches the given id, or null if none exists
     */
    @Nullable
    public static HeatMode getById(@NotNull String id) {
        return BY_ID.get(id);
    }

}
