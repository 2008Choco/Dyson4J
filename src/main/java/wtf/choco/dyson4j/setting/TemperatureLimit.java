package wtf.choco.dyson4j.setting;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a maximum temperature (represented in Kelvin) that a heater will not exceed.
 */
public interface TemperatureLimit extends MqttStateValueProvider {

    /**
     * Get the temperature represented by this {@link TemperatureLimit} in the given {@link TemperatureUnit}.
     *
     * @param unit the unit in which to receive the temperature
     *
     * @return the temperature
     */
    public float getTemperature(@NotNull TemperatureUnit unit);

    /**
     * Get a {@link TemperatureLimit} instance of the given temperature and {@link TemperatureUnit}.
     *
     * @param temperature the temperature. Must not exceed either {@link TemperatureUnit#getMinValue()} or
     * {@link TemperatureUnit#getMaxValue()}
     * @param unit the unit in which the temperature is represented
     *
     * @return the temperature limit instance
     */
    @NotNull
    public static TemperatureLimit of(float temperature, @NotNull TemperatureUnit unit) {
        if (temperature < unit.getMinValue() || temperature > unit.getMaxValue()) {
            throw new IllegalArgumentException("temperature must not exceed " + unit.getMinValue() + " - " + unit.getMaxValue() + " " + unit.getName() + ". Given: " + temperature + " " + unit.getName());
        }

        return new TemperatureLimitKelvin(unit.toKelvin(temperature));
    }

}
