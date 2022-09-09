package wtf.choco.dyson4j.setting;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The state of whether or not a filter change is required.
 */
public enum ResetFilterState implements MqttStateValueProvider {

    /**
     * Reset the filter's lifetime as though the filter was replaced.
     */
    RESET("RSTF"),

    /**
     * Do absolutely nothing. That's it.
     */
    DO_NOTHING("STET");

    private static final Map<String, ResetFilterState> BY_ID = new HashMap<>();

    static {
        for (ResetFilterState state : values()) {
            BY_ID.put(state.getStateValue(), state);
        }
    }

    private final String stateValue;

    private ResetFilterState(String stateValue) {
        this.stateValue = stateValue;
    }

    @Override
    public String getStateValue() {
        return stateValue;
    }

    /**
     * Get a {@link ResetFilterState} by its internal MQTT id.
     *
     * @param id the MQTT id
     *
     * @return the value that matches the given id, or null if none exists
     */
    @Nullable
    public static ResetFilterState getById(@NotNull String id) {
        return BY_ID.get(id);
    }

}
