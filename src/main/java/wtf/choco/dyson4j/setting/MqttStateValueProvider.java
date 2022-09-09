package wtf.choco.dyson4j.setting;

import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

/**
 * A {@link Supplier} capable of providing an MQTT state value.
 */
public interface MqttStateValueProvider extends Supplier<String> {

    /**
     * Get the state value.
     *
     * @return the state value
     */
    @NotNull
    public String getStateValue();

    @Override
    public default String get() {
        return getStateValue();
    }

}
