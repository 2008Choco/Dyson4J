package wtf.choco.dyson4j.setting;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The night mode of the fan.
 */
public enum NightMode implements MqttStateValueProvider {

    /**
     * The indicator on the fan is dimmed unless updated.
     */
    ON("ON"),

    /**
     * The indicator on the fan is always bright.
     */
    OFF("OFF");

    private static final Map<String, NightMode> BY_ID = new HashMap<>();

    static {
        for (NightMode mode : values()) {
            BY_ID.put(mode.getStateValue(), mode);
        }
    }

    private final String stateValue;

    private NightMode(String stateValue) {
        this.stateValue = stateValue;
    }

    @Override
    public String getStateValue() {
        return stateValue;
    }

    /**
     * Get a {@link NightMode} by its internal MQTT id.
     *
     * @param id the MQTT id
     *
     * @return the value that matches the given id, or null if none exists
     */
    @Nullable
    public static NightMode getById(@NotNull String id) {
        return BY_ID.get(id);
    }

}
