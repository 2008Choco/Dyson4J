package wtf.choco.dyson4j.setting;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import wtf.choco.dyson4j.util.Preconditions;

/**
 * Represents a state that a Dyson fan possesses.
 *
 * @param <T> the state value type
 */
public final class FanState<T extends MqttStateValueProvider> {

    private static final Map<String, FanState<? extends MqttStateValueProvider>> BY_ID = new HashMap<>();

    /**
     * The mode of the fan.
     *
     * @see FanMode
     */
    public static final FanState<FanMode> MODE = new FanState<>("fmod", "Fan Mode", FanMode.class);

    /**
     * The speed of the fan.
     *
     * @see FanSpeed
     */
    public static final FanState<FanSpeed> SPEED = new FanState<>("fnsp", "Fan Speed", FanSpeed.class);

    /**
     * Whether or not the fan is oscillating.
     *
     * @see FanOscillation
     */
    public static final FanState<FanOscillation> OSCILLATION = new FanState<>("oson", "Oscillation", FanOscillation.class);

    /**
     * The sleep timer (in minutes).
     *
     * @see SleepTimer
     */
    public static final FanState<SleepTimer> SLEEP_TIMER = new FanState<>("sltm", "Sleep Timer", SleepTimer.class);

    /**
     * Whether or not the fan will passively monitor air quality.
     *
     * @see AirQualityMonitor
     */
    public static final FanState<AirQualityMonitor> MONITOR_AIR_QUALITY = new FanState<>("rhtm", "Monitor Air Quality", AirQualityMonitor.class);

    /**
     * Whether or not the fan requires a filter change.
     * <p>
     * If this state is set to {@link ResetFilterState#RESET}, the fan will reset the lifetime
     * of its filter as though it has been changed.
     *
     * @see ResetFilterState
     */
    public static final FanState<ResetFilterState> RESET_FILTER_STATE = new FanState<>("rsft", "Reset Filter State", ResetFilterState.class);

    /**
     * The target air quality the fan will attempt to achieve with its filter.
     *
     * @see AirQualityTarget
     */
    public static final FanState<AirQualityTarget> AIR_QUALITY_TARGET = new FanState<>("qtar", "Air Quality Target", AirQualityTarget.class);

    /**
     * Night mode (not to be confused with {@link #MODE}).
     *
     * @see NightMode
     */
    public static final FanState<NightMode> NIGHT_MODE = new FanState<>("nmod", "Night Mode", NightMode.class);

    /**
     * Heater mode.
     *
     * @see HeatMode
     */
    public static final FanState<HeatMode> HEAT_MODE = new FanState<>("hmod", "Heat Mode", HeatMode.class);

    /**
     * The type of air distribution focus.
     *
     * @see FocusMode
     */
    public static final FanState<FocusMode> FOCUS_MODE = new FanState<>("ffoc", "Focus Mode", FocusMode.class);

    /**
     * The maximum temperature (in Kelvin) that the fan will attempt to achieve in heat mode.
     *
     * @see TemperatureLimit
     */
    public static final FanState<TemperatureLimit> MAXIMUM_TEMPERATURE = new FanState<>("hmax", "Maximum Temperature", TemperatureLimit.class);

    private final String id;
    private final String name;
    private final Class<T> valueType;

    private FanState(String id, String name, Class<T> valueType) {
        this.id = id;
        this.name = name;
        this.valueType = valueType;

        BY_ID.put(id, this);
    }

    /**
     * Get the unique id of this fan state (used in MQTT communications).
     *
     * @return the unique id
     */
    @NotNull
    public String getId() {
        return id;
    }

    /**
     * Get the friendly name of this fan state, useful for display.
     *
     * @return the name
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Get the {@link Class} of the value required by this state.
     *
     * @return the value class
     */
    @NotNull
    public Class<T> getValueType() {
        return valueType;
    }

    /**
     * Get a {@link FanState} by its internal MQTT id.
     *
     * @param <T> a generic MqttStateValueProvider type
     * @param id the internal MQTT id of the fan state
     *
     * @return the fan state, or null if none exists with the given id
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public static <T extends MqttStateValueProvider> FanState<T> getById(@NotNull String id) {
        Preconditions.checkArgument(id != null, "id must not be null");
        return (FanState<T>) BY_ID.get(id);
    }

}
