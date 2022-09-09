package wtf.choco.dyson4j.setting;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The speed/power of the fan.
 */
public enum FanSpeed implements MqttStateValueProvider {

    /**
     * Power: 1
     */
    POWER_1("0001"),

    /**
     * Power: 2
     */
    POWER_2("0002"),

    /**
     * Power: 3
     */
    POWER_3("0003"),

    /**
     * Power: 4
     */
    POWER_4("0004"),

    /**
     * Power: 5
     */
    POWER_5("0005"),

    /**
     * Power: 6
     */
    POWER_6("0006"),

    /**
     * Power: 7
     */
    POWER_7("0007"),

    /**
     * Power: 8
     */
    POWER_8("0008"),

    /**
     * Power: 9
     */
    POWER_9("0009"),

    /**
     * Power: 10
     */
    POWER_10("0010"),

    /**
     * Automatic power according to environmental targets.
     */
    AUTO("AUTO");

    private static final Map<String, FanSpeed> BY_ID = new HashMap<>();

    static {
        for (FanSpeed speed : values()) {
            BY_ID.put(speed.getStateValue(), speed);
        }
    }

    private final String stateValue;

    private FanSpeed(String stateValue) {
        this.stateValue = stateValue;
    }

    @Override
    public String getStateValue() {
        return stateValue;
    }

    /**
     * Get a {@link FanSpeed} based on a number.
     *
     * @param power the power (between 1 and 10 (inclusive))
     *
     * @return the {@link FanSpeed} matching the given power
     *
     * @throws IllegalArgumentException if the power exceeds 1 - 10 (inclusive)
     */
    @NotNull
    public static FanSpeed power(int power) {
        if (power <= 0 || power > 10) {
            throw new IllegalArgumentException("power must not exceed 1 - 10 (inclusive). Given: " + power);
        }

        return FanSpeed.values()[power - 1];
    }

    /**
     * Get a {@link FanSpeed} by its internal MQTT id.
     *
     * @param id the MQTT id
     *
     * @return the value that matches the given id, or null if none exists
     */
    @Nullable
    public static FanSpeed getById(@NotNull String id) {
        return BY_ID.get(id);
    }

}
