package wtf.choco.dyson4j.setting;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The air quality target.
 */
public enum AirQualityTarget implements MqttStateValueProvider {

    /**
     * Maintains an extremely high air quality for those very sensitive to particles
     * and pollutants.
     */
    BETTER("0001"),
    /**
     * Maintains a higher air quality for those sensitive to particles and pollutants.
     */
    HIGH("0003"),
    /**
     * Maintains "good" air quality for the average human.
     */
    NORMAL("0004");

    private static final Map<String, AirQualityTarget> BY_ID = new HashMap<>();

    static {
        for (AirQualityTarget target : values()) {
            BY_ID.put(target.getStateValue(), target);
        }
    }

    private final String stateValue;

    private AirQualityTarget(String stateValue) {
        this.stateValue = stateValue;
    }

    @Override
    public String getStateValue() {
        return stateValue;
    }

    /**
     * Get an {@link AirQualityTarget} by its internal MQTT id.
     *
     * @param id the MQTT id
     *
     * @return the value that matches the given id, or null if none exists
     */
    @Nullable
    public static AirQualityTarget getById(@NotNull String id) {
        return BY_ID.get(id);
    }

}
