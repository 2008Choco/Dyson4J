package wtf.choco.dyson4j.util;

import org.jetbrains.annotations.Nullable;

/**
 * Represents a supplier of results that is capable of throwing an exception
 * during the supply.
 * <p>
 * There is no requirement that a new or distinct result be returned each
 * time the supplier is invoked.
 * <p>
 * This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #get()}.
 *
 * @param <T> the type of results supplied by this supplier
 * @param <E> the type of exception that may be thrown
 */
@FunctionalInterface
public interface ExceptionalSupplier<T, E extends Throwable> {

    /**
     * Gets a result.
     *
     * @return a result
     *
     * @throws E the exception
     */
    @Nullable
    public T get() throws E;

}
