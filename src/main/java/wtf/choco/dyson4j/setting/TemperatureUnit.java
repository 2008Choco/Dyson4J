package wtf.choco.dyson4j.setting;

import org.jetbrains.annotations.NotNull;

import wtf.choco.dyson4j.util.Preconditions;

/**
 * A unit of measurement for temperature.
 */
public enum TemperatureUnit {

    /**
     * The Celsius scale (C°).
     */
    CELSIUS("Celsius", 1.0F, 37.0F),

    /**
     * The Fahrenheit scale (F°).
     */
    FAHRENHEIT("Fahrenheit", 34.0F, 98.0F),

    /**
     * The Kelvin scale (K).
     */
    KELVIN("Kelvin", 274.15F, 310.15F);

    private static final float FAHRENHEIT_FREEZING = 32.0F;
    private static final float KELVIN_FREEZING_IN_CELSIUS = 273.15F;
    private static final float NINE_FIFTHS = 9.0F / 5.0F; // Used in Celsius <-> Fahrenheit conversions

    private final String name;
    private final float minValue, maxValue;

    private TemperatureUnit(String name, float minValue, float maxValue) {
        this.name = name;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    /**
     * Get the friendly name of this temperature unit.
     *
     * @return the name
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Get the minimum value accepted by a Dyson fan for this temperature unit.
     *
     * @return the minimum value
     */
    public float getMinValue() {
        return minValue;
    }

    /**
     * Get the maximum value accepted by a Dyson fan for this temperature unit.
     *
     * @return the maximum value
     */
    public float getMaxValue() {
        return maxValue;
    }

    /**
     * Convert the given value from this temperature unit to Celsius.
     *
     * @param temperature the temperature to convert
     *
     * @return the temperature in Celsius
     */
    public float toCelsius(float temperature) {
        return switch (this) {
            case CELSIUS -> temperature;
            case FAHRENHEIT -> (temperature - FAHRENHEIT_FREEZING) / NINE_FIFTHS;
            case KELVIN -> temperature - KELVIN_FREEZING_IN_CELSIUS;
        };
    }

    /**
     * Convert the given value from this temperature unit to Fahrenheit.
     *
     * @param temperature the temperature to convert
     *
     * @return the temperature in Fahrenheit
     */
    public float toFahrenheit(float temperature) {
        return switch (this) {
            case CELSIUS -> (temperature * NINE_FIFTHS) + FAHRENHEIT_FREEZING;
            case FAHRENHEIT -> temperature;
            case KELVIN -> ((temperature - KELVIN_FREEZING_IN_CELSIUS) * NINE_FIFTHS) + FAHRENHEIT_FREEZING;
        };
    }

    /**
     * Convert the given value from this temperature unit to Kelvin.
     *
     * @param temperature the temperature to convert
     *
     * @return the temperature in Kelvin
     */
    public float toKelvin(float temperature) {
        return switch (this) {
            case CELSIUS -> temperature + KELVIN_FREEZING_IN_CELSIUS;
            case FAHRENHEIT -> toCelsius(temperature) + KELVIN_FREEZING_IN_CELSIUS;
            case KELVIN -> temperature;
        };
    }

    /**
     * Convert the given value from this temperature unit to the given {@link TemperatureUnit}.
     *
     * @param unit the unit to which the temperature should be converted
     * @param temperature the temperature to convert
     *
     * @return the temperature in the given unit
     */
    public float to(@NotNull TemperatureUnit unit, float temperature) {
        Preconditions.checkArgument(unit != null, "unit must not be null");

        return switch (unit) {
            case CELSIUS -> toCelsius(temperature);
            case FAHRENHEIT -> toFahrenheit(temperature);
            case KELVIN -> toKelvin(temperature);
        };
    }

}
