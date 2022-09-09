package wtf.choco.dyson4j.setting;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SleepTimerTest {

    @Test
    void testIllegalValues() {
        assertThrows(IllegalArgumentException.class, () -> SleepTimer.ofMinutes(10000));
        assertDoesNotThrow(() -> SleepTimer.ofMinutes(9999));
    }

    @Test
    void testOfHours() {
        assertEquals(60, SleepTimer.ofHours(1).getMinutes());
        assertThrows(IllegalArgumentException.class, () -> SleepTimer.ofHours(167)); // 167 hours = 10020 minutes
    }

    @Test
    void testOfDuration() {
        assertEquals(120, SleepTimer.of(Duration.ofHours(2)).getMinutes());
        assertThrows(IllegalArgumentException.class, () -> SleepTimer.of(Duration.ofDays(7))); // 7 days = 10080 minutes
    }

    @Test
    void testOfTimeUnit() {
        assertEquals(240, SleepTimer.of(4, TimeUnit.HOURS).getMinutes());
        assertEquals(30, SleepTimer.of(30, TimeUnit.MINUTES).getMinutes());
        assertEquals(2, SleepTimer.of(120, TimeUnit.SECONDS).getMinutes());
    }

    @Test
    void testOff() {
        assertSame(SleepTimer.OFF, SleepTimer.ofMinutes(0));
        assertSame(SleepTimer.OFF, SleepTimer.ofMinutes(-60));
    }

    @Test
    void testEquality() {
        assertEquals(SleepTimer.ofMinutes(10), SleepTimer.ofMinutes(10));
        assertNotEquals(SleepTimer.ofMinutes(10), SleepTimer.ofMinutes(11));
    }

}
