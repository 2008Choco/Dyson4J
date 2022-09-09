package wtf.choco.dyson4j.setting;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TemperatureUnitTest {

    private static final float EPSILON = 0.02F;

    @Test
    void testUnitConversionsCelsius() {
        assertEquals(-35.0F, TemperatureUnit.CELSIUS.toCelsius(-35));
        assertEquals(-31.0F, TemperatureUnit.CELSIUS.toFahrenheit(-35));
        assertEquals(238.15F, TemperatureUnit.CELSIUS.toKelvin(-35));

        assertEquals(0.0F, TemperatureUnit.CELSIUS.toCelsius(0));
        assertEquals(32.0F, TemperatureUnit.CELSIUS.toFahrenheit(0));
        assertEquals(273.15F, TemperatureUnit.CELSIUS.toKelvin(0));

        assertEquals(25.0F, TemperatureUnit.CELSIUS.toCelsius(25));
        assertEquals(77.0F, TemperatureUnit.CELSIUS.toFahrenheit(25));
        assertEquals(298.15F, TemperatureUnit.CELSIUS.toKelvin(25));
    }

    @Test
    void testUnitConversionsFahrenheit() {
        assertEquals(-37.5, TemperatureUnit.FAHRENHEIT.toCelsius(-35.5F), EPSILON);
        assertEquals(-35.5F, TemperatureUnit.FAHRENHEIT.toFahrenheit(-35.5F), EPSILON);
        assertEquals(235.65F, TemperatureUnit.FAHRENHEIT.toKelvin(-35.5F), EPSILON);

        assertEquals(-17.7778F, TemperatureUnit.FAHRENHEIT.toCelsius(0), EPSILON);
        assertEquals(0.0F, TemperatureUnit.FAHRENHEIT.toFahrenheit(0), EPSILON);
        assertEquals(255.372F, TemperatureUnit.FAHRENHEIT.toKelvin(0), EPSILON);

        assertEquals(26.94444F, TemperatureUnit.FAHRENHEIT.toCelsius(80.5F), EPSILON);
        assertEquals(80.5F, TemperatureUnit.FAHRENHEIT.toFahrenheit(80.5F), EPSILON);
        assertEquals(300.0944F, TemperatureUnit.FAHRENHEIT.toKelvin(80.5F), EPSILON);
    }

    @Test
    void testUnitConversionsKelvin() {
        assertEquals(-173.15F, TemperatureUnit.KELVIN.toCelsius(100), EPSILON);
        assertEquals(-279.67, TemperatureUnit.KELVIN.toFahrenheit(100), EPSILON);
        assertEquals(100F, TemperatureUnit.KELVIN.toKelvin(100), EPSILON);

        assertEquals(0.0F, TemperatureUnit.KELVIN.toCelsius(273.15F), EPSILON);
        assertEquals(32.0F, TemperatureUnit.KELVIN.toFahrenheit(273.15F), EPSILON);
        assertEquals(273.15F, TemperatureUnit.KELVIN.toKelvin(273.15F), EPSILON);

        assertEquals(226.85, TemperatureUnit.KELVIN.toCelsius(500.0F), EPSILON);
        assertEquals(440.33, TemperatureUnit.KELVIN.toFahrenheit(500.0F), EPSILON);
        assertEquals(500.0F, TemperatureUnit.KELVIN.toKelvin(500.0F), EPSILON);
    }

    @Test
    void testUnitConversionsToUnit() {
        for (TemperatureUnit unit : TemperatureUnit.values()) {
            assertEquals(unit.toCelsius(100), unit.to(TemperatureUnit.CELSIUS, 100));
            assertEquals(unit.toFahrenheit(100), unit.to(TemperatureUnit.FAHRENHEIT, 100));
            assertEquals(unit.toKelvin(100), unit.to(TemperatureUnit.KELVIN, 100));
        }
    }

}
