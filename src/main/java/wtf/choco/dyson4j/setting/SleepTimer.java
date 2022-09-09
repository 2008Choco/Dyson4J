package wtf.choco.dyson4j.setting;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.NotNull;

import wtf.choco.dyson4j.util.Preconditions;

/**
 * Represents a timer (calculated in minutes) until the fan will automatically disable.
 */
public interface SleepTimer extends MqttStateValueProvider {

    /**
     * The sleep timer is disabled.
     */
    public static final SleepTimer OFF = SleepTimerOff.INSTANCE;

    /**
     * Get the amount of minutes in this timer.
     *
     * @return the minutes
     */
    public long getMinutes();

    /**
     * Get a {@link SleepTimer} instance representing the given amount of time in the given
     * {@link TimeUnit}. If the provided time is 0 or less, {@link #OFF} is returned.
     *
     * @param time the time. Must be less than 10,000
     * @param unit the unit of measurement
     *
     * @return the sleep timer
     */
    @NotNull
    public static SleepTimer of(long time, @NotNull TimeUnit unit) {
        Preconditions.checkArgument(unit != null, "unit must not be null");

        if (time <= 0) {
            return OFF;
        }

        time = unit.toMinutes(time);
        if (time > 9999) {
            throw new IllegalArgumentException("time must not exceed 9999 minutes");
        }

        return new SleepTimerMinutes(time);
    }

    /**
     * Get a {@link SleepTimer} instance representing the given {@link Duration}. If the provided
     * duration is 0 or negative, {@link #OFF} is returned.
     *
     * @param duration the duration of time
     *
     * @return the sleep timer
     */
    public static SleepTimer of(@NotNull Duration duration) {
        Preconditions.checkArgument(duration != null, "duration must not be null");

        return of(duration.toMinutes(), TimeUnit.MINUTES);
    }

    /**
     * Get a {@link SleepTimer} instance representing the given amount of minutes. If the provided
     * minutes is 0 or less, {@link #OFF} is returned.
     *
     * @param minutes the minutes. Must be less than 10,000
     *
     * @return the sleep timer
     */
    public static SleepTimer ofMinutes(int minutes) {
        return of(minutes, TimeUnit.MINUTES);
    }

    /**
     * Get a {@link SleepTimer} instance representing the given amount of hours. If the provided
     * hours is 0 or less, {@link #OFF} is returned.
     *
     * @param hours the hours. Must be less than 167 (10,000 minutes)
     *
     * @return the sleep timer
     */
    public static SleepTimer ofHours(int hours) {
        return of(hours, TimeUnit.HOURS);
    }

}
