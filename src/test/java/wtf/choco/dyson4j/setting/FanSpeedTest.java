package wtf.choco.dyson4j.setting;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FanSpeedTest {

    @Test
    void testPower() {
        assertSame(FanSpeed.POWER_1, FanSpeed.power(1));
        assertSame(FanSpeed.POWER_2, FanSpeed.power(2));
        assertSame(FanSpeed.POWER_3, FanSpeed.power(3));
        assertSame(FanSpeed.POWER_4, FanSpeed.power(4));
        assertSame(FanSpeed.POWER_5, FanSpeed.power(5));
        assertSame(FanSpeed.POWER_6, FanSpeed.power(6));
        assertSame(FanSpeed.POWER_7, FanSpeed.power(7));
        assertSame(FanSpeed.POWER_8, FanSpeed.power(8));
        assertSame(FanSpeed.POWER_9, FanSpeed.power(9));
        assertSame(FanSpeed.POWER_10, FanSpeed.power(10));

        assertThrows(IllegalArgumentException.class, () -> FanSpeed.power(0));
        assertThrows(IllegalArgumentException.class, () -> FanSpeed.power(11));
    }

}
