package wtf.choco.dyson4j.exception;

import org.jetbrains.annotations.Nullable;

import wtf.choco.dyson4j.DysonFan;

/**
 * Thrown when a connection-related error occurs with a {@link DysonFan}.
 */
public class DysonConnectionException extends RuntimeException {

    private static final long serialVersionUID = -4203603960323208441L;

    /**
     * Construct a new {@link DysonConnectionException}.
     *
     * @param message the message to provide
     * @param cause the cause of this exception
     */
    public DysonConnectionException(@Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
    }

    /**
     * Construct a new {@link DysonConnectionException}.
     *
     * @param message the message to provide
     */
    public DysonConnectionException(@Nullable String message) {
        super(message);
    }

}
