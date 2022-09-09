package wtf.choco.dyson4j.setting;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The focus mode of a fan blowing air.
 */
public enum FocusMode implements MqttStateValueProvider {

    /**
     * The fan will focus air directly in front of it.
     */
    FOCUS("ON"),

    /**
     * The fan will diffuse air in the general area in front of it.
     */
    DIFFUSE("OFF");

    private static final Map<String, FocusMode> BY_ID = new HashMap<>();

    static {
        for (FocusMode mode : values()) {
            BY_ID.put(mode.getStateValue(), mode);
        }
    }

    private final String stateValue;

    private FocusMode(String stateValue) {
        this.stateValue = stateValue;
    }

    @Override
    public String getStateValue() {
        return stateValue;
    }

    /**
     * Get a {@link FocusMode} by its internal MQTT id.
     *
     * @param id the MQTT id
     *
     * @return the value that matches the given id, or null if none exists
     */
    @Nullable
    public static FocusMode getById(@NotNull String id) {
        return BY_ID.get(id);
    }

}
