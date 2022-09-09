package wtf.choco.dyson4j;

import org.jetbrains.annotations.NotNull;

import wtf.choco.dyson4j.setting.FanState;
import wtf.choco.dyson4j.setting.SleepTimer;

/**
 * Represents a Dyson fan's environmental sensor data as queried by
 * {@link DysonFan#requestEnvironmentalSensorData()}. This sensor data is a snapshot of
 * when the data was requested. An instance of this object will not reflect changes
 * requested by future invocations.
 */
public interface EnvironmentalSensorData {

    /**
     * Get the temperature of the environment measured in Kelvin, or -1.0 if the
     * temperature monitor is disabled.
     *
     * @return the temperature in Kelvin
     */
    public double getTemperature();

    /**
     * Get the relative humidity of the environment from 0 - 100 as a percent value.
     *
     * @return the relative humidity
     */
    public int getRelativeHumidity();

    /**
     * Get the amount of particles in the air as a value from 0 to 1,000. Note however
     * that {@link FanState#MONITOR_AIR_QUALITY} must be enabled for this value to be
     * properly returned.
     *
     * @return the amount of particles
     */
    public int getParticles();

    /**
     * Get the amount of volatile compounds in the air as a value from 0 to 1,000, or
     * -1 if the volatile compounds sensor is still initializing. Note however that
     * {@link FanState#MONITOR_AIR_QUALITY} must be enabled for this value to be
     * properly returned.
     *
     * @return the amount of volatile compounds
     */
    public int getVolatileCompounds();

    /**
     * Get the active {@link SleepTimer}. If there is no active sleep timer,
     * {@link SleepTimer#OFF} will be returned instead.
     *
     * @return the sleep timer
     */
    @NotNull
    public SleepTimer getSleepTimer();

}
