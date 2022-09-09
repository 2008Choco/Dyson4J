package wtf.choco.dyson4j.setting;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TemperatureLimitTest {

    private static final float EPSILON = 0.02F;

    @Test
    void testOf() {
        for (TemperatureUnit unit : TemperatureUnit.values()) {
            assertThrows(IllegalArgumentException.class, () -> TemperatureLimit.of(unit.getMinValue() - 0.5F, unit));
            assertThrows(IllegalArgumentException.class, () -> TemperatureLimit.of(unit.getMaxValue() + 0.5F, unit));
        }

        assertEquals(20, TemperatureLimit.of(20, TemperatureUnit.CELSIUS).getTemperature(TemperatureUnit.CELSIUS));
        assertEquals(300.65F, TemperatureLimit.of(300.65F, TemperatureUnit.KELVIN).getTemperature(TemperatureUnit.KELVIN), EPSILON);
        assertEquals(55.25F, TemperatureLimit.of(55.25F, TemperatureUnit.FAHRENHEIT).getTemperature(TemperatureUnit.FAHRENHEIT), EPSILON);
    }

    @Test
    void testEquality() {
        assertEquals(TemperatureLimit.of(21, TemperatureUnit.CELSIUS), TemperatureLimit.of(69.8F, TemperatureUnit.FAHRENHEIT));
    }

}
