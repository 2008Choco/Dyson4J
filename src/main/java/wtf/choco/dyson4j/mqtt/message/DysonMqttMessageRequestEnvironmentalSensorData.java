package wtf.choco.dyson4j.mqtt.message;

import com.google.gson.JsonObject;

import wtf.choco.dyson4j.EnvironmentalSensorData;
import wtf.choco.dyson4j.setting.SleepTimer;

/**
 * A {@link DysonMqttMessage} that requests the MQTT server to respond with the Dyson fan's
 * current environmental sensor data. The response is packaged as an {@link EnvironmentalSensorData}.
 */
public final class DysonMqttMessageRequestEnvironmentalSensorData extends DysonMqttMessageRespondable<EnvironmentalSensorData> {

    private static final String TEMPERATURE_OFF = "OFF";
    private static final String VOLATILE_COMPOUNDS_INITIALIZING = "INIT";

    /**
     * Construct a new {@link DysonMqttMessageRequestEnvironmentalSensorData}.
     */
    public DysonMqttMessageRequestEnvironmentalSensorData() {
        super(MESSAGE_ID_REQUEST_CURRENT_STATE, MESSAGE_ID_RESPONSE_ENVIRONMENTAL_CURRENT_SENSOR_DATA);
    }

    @Override
    public EnvironmentalSensorData handleResponse(JsonObject response) {
        JsonObject data = response.getAsJsonObject("data");

        String temperatureString = data.get("tact").getAsString();
        double temperature = TEMPERATURE_OFF.equals(temperatureString) ? -1.0 : (Double.parseDouble(data.get("tact").getAsString()) / 10.0);
        int relativeHumidify = Integer.parseInt(data.get("hact").getAsString());
        int particles = Integer.parseInt(data.get("pact").getAsString());
        String volatileParticlesString = data.get("vact").getAsString();
        int volatileCompounds = VOLATILE_COMPOUNDS_INITIALIZING.equals(volatileParticlesString) ? -1 : Integer.parseInt(volatileParticlesString);

        SleepTimer sleepTimer;

        String sleepTimerString = data.get("sltm").getAsString();
        if (SleepTimer.OFF.getStateValue().equals(sleepTimerString)) {
            sleepTimer = SleepTimer.OFF;
        } else {
            sleepTimer = SleepTimer.ofMinutes(Integer.parseInt(data.get("sltm").getAsString()));
        }

        return new DysonEnvironmentalSensorData(temperature, relativeHumidify, particles, volatileCompounds, sleepTimer);
    }

    private static final class DysonEnvironmentalSensorData implements EnvironmentalSensorData {

        private final double temperature;
        private final int relativeHumidity, particles, volatileCompounds;
        private final SleepTimer sleepTimer;

        private DysonEnvironmentalSensorData(double temperature, int relativeHumidify, int particles, int volatileCompounds, SleepTimer sleepTimer) {
            this.temperature = temperature;
            this.relativeHumidity = relativeHumidify;
            this.particles = particles;
            this.volatileCompounds = volatileCompounds;
            this.sleepTimer = sleepTimer;
        }

        @Override
        public double getTemperature() {
            return temperature;
        }

        @Override
        public int getRelativeHumidity() {
            return relativeHumidity;
        }

        @Override
        public int getParticles() {
            return particles;
        }

        @Override
        public int getVolatileCompounds() {
            return volatileCompounds;
        }

        @Override
        public SleepTimer getSleepTimer() {
            return sleepTimer;
        }

        @Override
        public String toString() {
            return String.format("DysonEnvironmentalSensorData[temperature=%s, relativeHumidity=%s, particles=%s, volatileCompounds=%s, sleepTimer=%s]",
                    temperature, relativeHumidity, particles, volatileCompounds, sleepTimer
            );
        }

    }

}
