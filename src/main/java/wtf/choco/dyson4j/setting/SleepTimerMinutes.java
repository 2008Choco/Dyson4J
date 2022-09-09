package wtf.choco.dyson4j.setting;

/**
 * A {@link SleepTimer} implementation holding its value in minutes.
 */
final class SleepTimerMinutes implements SleepTimer {

    private final long minutes;
    private final String minutesString;

    SleepTimerMinutes(long minutes) {
        this.minutes = minutes;
        this.minutesString = String.valueOf(minutes);
    }

    @Override
    public long getMinutes() {
        return minutes;
    }

    @Override
    public String getStateValue() {
        return minutesString;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(minutes);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || (obj instanceof SleepTimerMinutes other && minutes == other.minutes);
    }

    @Override
    public String toString() {
        return String.format("SleepTimer[minutes=%s]", minutes);
    }

}
