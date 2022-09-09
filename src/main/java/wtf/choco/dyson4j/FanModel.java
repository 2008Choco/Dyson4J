package wtf.choco.dyson4j;

import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import wtf.choco.dyson4j.setting.FanState;

/**
 * Represents a supported Dyson fan model.
 */
public enum FanModel {

    /**
     * Dyson Pure Cool Link Tower.
     */
    DYSON_PURE_COOL_LINK_TOWER("475", "Dyson Pure Cool Link Tower"),

    /**
     * Dyson Pure Cool Link Desk.
     */
    DYSON_PURE_COOL_LINK_DESK("469", "Dyson Pure Cool Link Desk"),

    /**
     * Dyson Pure Hot+Cool Link Tower.
     */
    DYSON_PURE_HOT_COOL_LINK_TOWER("455", "Dyson Pure Hot+Cool Link Tower"),

    /**
     * Dyson 360 Eye.
     */
    DYSON_360_EYE("N223", "Dyson 360 Eye"),

    /**
     * Dyson Pure Cool.
     */
    DYSON_PURE_COOL("438", "Dyson Pure Cool"),

    /**
     * Dyson Pure Cool Humidify.
     */
    DYSON_PURE_COOL_HUMIDIFY("358", "Dyson Pure Cool Humidify"),

    /**
     * Dyson Pure Cool Desktop.
     */
    DYSON_PURE_COOL_DESKTOP("520", "Dyson Pure Cool Desktop"),

    /**
     * Dyson Pure Hot+Cool.
     */
    DYSON_PURE_HOT_COOL("527", "Dyson Pure Hot+Cool");

    private final String productCode;
    private final String friendlyName;
    private final Predicate<FanState<?>> stateSupportPredicate; // TODO: Pull request, anybody? :)

    private FanModel(@NotNull String productCode, @NotNull String friendlyName, @NotNull Predicate<FanState<?>> stateSupportPredicate) {
        this.productCode = productCode;
        this.friendlyName = friendlyName;
        this.stateSupportPredicate = stateSupportPredicate;
    }

    private FanModel(@NotNull String productCode, @NotNull String friendlyName) {
        this(productCode, friendlyName, state -> true);
    }

    /**
     * Get the fan model's product code.
     *
     * @return the product code
     */
    @NotNull
    public String getProductCode() {
        return productCode;
    }

    /**
     * Get the friendly, human-readable name of this fan model.
     *
     * @return the friendly name
     */
    @NotNull
    public String getFriendlyName() {
        return friendlyName;
    }

    /**
     * Check whether or not this fan supports the provided {@link FanState}.
     *
     * @param state the state to check
     *
     * @return true if the fan supports the provided state, false otherwise
     */
    public boolean supportsFeature(@NotNull FanState<?> state) {
        return state != null && stateSupportPredicate.test(state);
    }

}
