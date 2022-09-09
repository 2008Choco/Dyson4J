package wtf.choco.dyson4j.setting;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the state of the air quality monitor. When disabled, fans will not be able
 * to supply accurate air quality environmental data.
 */
public enum AirQualityMonitor implements MqttStateValueProvider {

    /**
     * The air quality monitor should be enabled.
     */
    ON("ON"),

    /**
     * The air quality monitor should be disabled.
     */
    OFF("OFF");

    private static final Map<String, AirQualityMonitor> BY_ID = new HashMap<>();

    static {
        for (AirQualityMonitor monitor : values()) {
            BY_ID.put(monitor.getStateValue(), monitor);
        }
    }

    private final String stateValue;

    private AirQualityMonitor(String stateValue) {
        this.stateValue = stateValue;
    }

    @Override
    public String getStateValue() {
        return stateValue;
    }

    /**
     * Get an {@link AirQualityMonitor} by its internal MQTT id.
     *
     * @param id the MQTT id
     *
     * @return the value that matches the given id, or null if none exists
     */
    @Nullable
    public static AirQualityMonitor getById(@NotNull String id) {
        return BY_ID.get(id);
    }

}
