package wtf.choco.dyson4j.util;

import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A utility class containing various precondition checks that will throw unchecked exceptions
 * if conditions are violated.
 * <p>
 * This class is strongly based on Guava's Preconditions class, only shading the entirety of
 * Guava is stupid for just one class.
 */
public final class Preconditions {

    /**
     * Check whether or not the provided condition is true, else throw an {@link IllegalArgumentException}
     * with the provided message.
     *
     * @param condition the condition to check
     * @param message the exception message
     */
    public static void checkArgument(boolean condition, @Nullable String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Check whether or not the provided condition is true, else throw an {@link IllegalArgumentException}
     * with the provided message.
     *
     * @param condition the condition to check
     * @param message a supplier containing the exception message
     */
    public static void checkArgument(boolean condition, @NotNull Supplier<@Nullable String> message) {
        if (!condition) {
            throw new IllegalArgumentException(message.get());
        }
    }

}
