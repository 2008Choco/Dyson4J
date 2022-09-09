package wtf.choco.dyson4j;

import org.jetbrains.annotations.NotNull;

import wtf.choco.dyson4j.setting.FanState;
import wtf.choco.dyson4j.setting.MqttStateValueProvider;

/**
 * Represents a multi-state change. Acts as a builder to prepare state changes locally before
 * committing to the fan over MQTT.
 */
public interface MultiStateChange {

    /**
     * Set the provided {@link FanState} to the given value.
     *
     * @param <T> the state's value type
     * @param state the state to set
     * @param value the value to set
     *
     * @return this instance. Allows for chained method calls
     */
    @NotNull
    public <T extends MqttStateValueProvider> MultiStateChange setState(@NotNull FanState<T> state, @NotNull T value);

}
