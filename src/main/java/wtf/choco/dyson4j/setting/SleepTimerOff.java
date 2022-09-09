package wtf.choco.dyson4j.setting;

/**
 * A {@link SleepTimer} implementation representing the off state.
 */
final class SleepTimerOff implements SleepTimer {

    static final SleepTimer INSTANCE = new SleepTimerOff();

    private SleepTimerOff() { }

    @Override
    public long getMinutes() {
        return 0;
    }

    @Override
    public String getStateValue() {
        return "OFF";
    }

    @Override
    public String toString() {
        return "SleepTimer[OFF]";
    }

}
