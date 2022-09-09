package wtf.choco.dyson4j.mqtt;

import java.util.Optional;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link IMqttActionListener} that handles both successes and failures in a single method
 * invocation as a {@link FunctionalInterface}.
 */
@FunctionalInterface
public interface ICompoundMqttActionListener extends IMqttActionListener {

    /**
     * Handle an MQTT action.
     * <p>
     * This method will be called for both successes and failures. The {@code token} will always
     * be available, but the {@code exception} will be present depending on whether or not the
     * action succeeded or failed. If it failed, the optional will contain the exception that
     * was thrown as a result of the failure.
     *
     * @param token the action token
     * @param exception an optional containing the exception that may or may not have been thrown.
     * If the action was a success, this optional will be empty
     */
    public void handle(@NotNull IMqttToken token, @NotNull Optional<Throwable> exception);

    @Override
    public default void onSuccess(IMqttToken asyncActionToken) {
        this.handle(asyncActionToken, Optional.empty());
    }

    @Override
    public default void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        this.handle(asyncActionToken, Optional.of(exception));
    }

    /**
     * A utility method to satisfy the compiler where instances of {@link IMqttActionListener}
     * are required by library methods, but an {@link ICompoundMqttActionListener} is used.
     * <p>
     * This is sneaky :)
     *
     * @param listener the listener
     *
     * @return the listener as an IMqttActionListener
     */
    @NotNull
    public static IMqttActionListener of(@NotNull ICompoundMqttActionListener listener) {
        return listener;
    }

}
