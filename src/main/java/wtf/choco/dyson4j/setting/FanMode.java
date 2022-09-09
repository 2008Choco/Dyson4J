package wtf.choco.dyson4j.setting;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The fan's mode.
 */
public enum FanMode implements MqttStateValueProvider {

    /**
     * The fan is on and blowing air.
     */
    ON("FAN"),

    /**
     * The fan is off.
     */
    OFF("OFF"),

    /**
     * The fan will turn on and off according to environmental values.
     */
    AUTO("AUTO");

    private static final Map<String, FanMode> BY_ID = new HashMap<>();

    static {
        for (FanMode mode : values()) {
            BY_ID.put(mode.getStateValue(), mode);
        }
    }

    private final String stateValue;

    private FanMode(String stateValue) {
        this.stateValue = stateValue;
    }

    @Override
    public String getStateValue() {
        return stateValue;
    }

    /**
     * Get a {@link FanMode} by its internal MQTT id.
     *
     * @param id the MQTT id
     *
     * @return the value that matches the given id, or null if none exists
     */
    @Nullable
    public static FanMode getById(@NotNull String id) {
        return BY_ID.get(id);
    }

}
