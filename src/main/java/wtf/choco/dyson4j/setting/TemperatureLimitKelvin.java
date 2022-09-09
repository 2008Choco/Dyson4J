package wtf.choco.dyson4j.setting;

import wtf.choco.dyson4j.util.Preconditions;

/**
 * A {@link TemperatureLimit} implementation with values represented in Kelvin.
 */
final class TemperatureLimitKelvin implements TemperatureLimit {

    private static final float EPSILON = 0.02F;

    private final float kelvin;
    private final String kelvinString;

    TemperatureLimitKelvin(float kelvin) {
        this.kelvin = kelvin;
        this.kelvinString = String.valueOf((int) kelvin) + "0";
    }

    @Override
    public float getTemperature(TemperatureUnit unit) {
        Preconditions.checkArgument(unit != null, "unit must not be null");

        return TemperatureUnit.KELVIN.to(unit, kelvin);
    }

    @Override
    public String getStateValue() {
        return kelvinString;
    }

    @Override
    public int hashCode() {
        return Float.hashCode(kelvin);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || (obj instanceof TemperatureLimitKelvin other && Math.abs(kelvin - other.kelvin) <= EPSILON);
    }

}
