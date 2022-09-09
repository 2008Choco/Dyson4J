package wtf.choco.dyson4j;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import wtf.choco.dyson4j.setting.FanState;
import wtf.choco.dyson4j.setting.MqttStateValueProvider;

/**
 * Represents a Dyson fan's status as queried by {@link DysonFan#requestCurrentState()}.
 * This status is a snapshot of when the state was requested. An instance of this object
 * will not reflect changes requested by future invocations.
 */
public interface DeviceStatus {

    /**
     * An "error code" returned by Dyson fans to indicate normal status.
     *
     * @see #ERROR_CODE_NORMAL_2
     */
    public static final String ERROR_CODE_NORMAL = "02C0";

    /**
     * An "error code" returned by Dyson fans to indicate normal status.
     *
     * @see #ERROR_CODE_NORMAL
     */
    public static final String ERROR_CODE_NORMAL_2 = "02C9";

    /**
     * A (misleadingly named) error code returned by Dyson fans to indicate that the fan's
     * filter should be replaced.
     */
    public static final String ERROR_CODE_REPLACE_FILTER = "NONE";


    /**
     * A warning code returned by Dyson fans to indicate normal status.
     */
    public static final String WARNING_CODE_NORMAL = "NONE";

    /**
     * A warning code returned by Dyson fans to indicate that the faan's filter should be
     * replaced.
     */
    public static final String WARNING_CODE_REPLACE_FILTER = "FLTR";

    /**
     * Get the current error code of the device. All status responses have errors codes, even
     * if nothing is inherently wrong with the fan.
     *
     * @return the error code
     *
     * @see #ERROR_CODE_NORMAL
     * @see #ERROR_CODE_NORMAL_2
     * @see #ERROR_CODE_REPLACE_FILTER
     */
    @NotNull
    public String getErrorCode();

    /**
     * Get the current warning code of the device. All status responses have warning codes,
     * even if nothing is inherently wrong with the fan.
     *
     * @return the warning code
     *
     * @see #WARNING_CODE_NORMAL
     * @see #WARNING_CODE_REPLACE_FILTER
     */
    @NotNull
    public String getWarningCode();

    /**
     * Get the amount of time remaining (in hours) for the fan's current filter. After this
     * time reaches zero, an error and warning code to replace the filter will be present.
     *
     * @return the remaining filter life hours
     */
    public int getRemainingFilterLife();

    /**
     * Get the active value of the given {@link FanState} as of this request.
     *
     * @param <T> the state value type
     * @param state the state to get
     *
     * @return the state value, or null if a value was not provided in the fan's response
     */
    @Nullable
    public <T extends MqttStateValueProvider> T getState(@NotNull FanState<T> state);

}
