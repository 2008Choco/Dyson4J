package wtf.choco.dyson4j.mqtt.message;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Function;

import wtf.choco.dyson4j.DeviceStatus;
import wtf.choco.dyson4j.setting.AirQualityMonitor;
import wtf.choco.dyson4j.setting.AirQualityTarget;
import wtf.choco.dyson4j.setting.FanMode;
import wtf.choco.dyson4j.setting.FanOscillation;
import wtf.choco.dyson4j.setting.FanSpeed;
import wtf.choco.dyson4j.setting.FanState;
import wtf.choco.dyson4j.setting.FocusMode;
import wtf.choco.dyson4j.setting.HeatMode;
import wtf.choco.dyson4j.setting.MqttStateValueProvider;
import wtf.choco.dyson4j.setting.NightMode;
import wtf.choco.dyson4j.setting.ResetFilterState;
import wtf.choco.dyson4j.setting.TemperatureLimit;
import wtf.choco.dyson4j.setting.TemperatureUnit;
import wtf.choco.dyson4j.util.Preconditions;

/**
 * A {@link DysonMqttMessage} that requests the MQTT server to respond with the Dyson fan's
 * current status. The response is packaged as a {@link DeviceStatus}.
 */
public final class DysonMqttMessageRequestCurrentState extends DysonMqttMessageRespondable<DeviceStatus> {

    /**
     * Construct a new {@link DysonMqttMessageRequestCurrentState}.
     */
    public DysonMqttMessageRequestCurrentState() {
        super(MESSAGE_ID_REQUEST_CURRENT_STATE, MESSAGE_ID_RESPONSE_CURRENT_STATE);
    }

    @Override
    public DeviceStatus handleResponse(JsonObject response) {
        JsonObject productState = response.getAsJsonObject("product-state");

        String errorCode = productState.get("ercd").getAsString();
        String warningCode = productState.get("wacd").getAsString();
        int remainingFilterLife = Integer.parseInt(productState.get("filf").getAsString());

        DysonDeviceStatus deviceStatus = new DysonDeviceStatus(errorCode, warningCode, remainingFilterLife);

        // Apply all the states from the response
        this.applyState(deviceStatus, productState, FanState.MODE, FanMode::getById);
        this.applyState(deviceStatus, productState, FanState.SPEED, FanSpeed::getById);
        this.applyState(deviceStatus, productState, FanState.AIR_QUALITY_TARGET, AirQualityTarget::getById);
        this.applyState(deviceStatus, productState, FanState.MONITOR_AIR_QUALITY, AirQualityMonitor::getById);
        this.applyState(deviceStatus, productState, FanState.OSCILLATION, FanOscillation::getById);
        this.applyState(deviceStatus, productState, FanState.NIGHT_MODE, NightMode::getById);
        this.applyState(deviceStatus, productState, FanState.HEAT_MODE, HeatMode::getById);
        this.applyState(deviceStatus, productState, FanState.MAXIMUM_TEMPERATURE, input -> TemperatureLimit.of(Integer.parseInt(input.substring(0, 3)), TemperatureUnit.KELVIN));
        this.applyState(deviceStatus, productState, FanState.FOCUS_MODE, FocusMode::getById);

        // This must be done manually so that the state can be set appropriately. This is displayed in an error code
        if (DeviceStatus.ERROR_CODE_REPLACE_FILTER.equals(deviceStatus.getErrorCode())) {
            deviceStatus.setState(FanState.RESET_FILTER_STATE, ResetFilterState.RESET);
        }

        return deviceStatus;
    }

    private <T extends MqttStateValueProvider> void applyState(DysonDeviceStatus deviceStatus, JsonObject productState, FanState<T> state, Function<String, T> getter) {
        JsonElement stateElement = productState.get(state.getId());
        if (stateElement == null) {
            return;
        }

        deviceStatus.setState(state, getter.apply(stateElement.getAsString()));
    }

    private static final class DysonDeviceStatus implements DeviceStatus {

        private final Map<FanState<?>, MqttStateValueProvider> states = new HashMap<>();

        private final String errorCode, warningCode;
        private final int remainingFilterLife;

        private DysonDeviceStatus(String errorCode, String warningCode, int remainingFilterLife) {
            this.errorCode = errorCode;
            this.warningCode = warningCode;
            this.remainingFilterLife = remainingFilterLife;
        }

        @Override
        public String getErrorCode() {
            return errorCode;
        }

        @Override
        public String getWarningCode() {
            return warningCode;
        }

        @Override
        public int getRemainingFilterLife() {
            return remainingFilterLife;
        }

        private <T extends MqttStateValueProvider> void setState(FanState<T> state, T value) {
            Preconditions.checkArgument(state != null, "state must not be null");
            Preconditions.checkArgument(value != null, "value must not be null");

            this.states.put(state, value);
        }

        @Override
        public <T extends MqttStateValueProvider> T getState(FanState<T> state) {
            Preconditions.checkArgument(state != null, "state must not be null");

            return state.getValueType().cast(states.get(state));
        }

        @Override
        public String toString() {
            StringJoiner statesStringJoiner = new StringJoiner(",");
            this.states.forEach((state, value) -> statesStringJoiner.add("\"" + state.getName() + "\":\"" + value.getStateValue() + "\""));

            return String.format("DysonDeviceStatus[errorCode=%s, warningCode=%s, remainingFilterLife=%s, states:[ %s ]]",
                    errorCode, warningCode, remainingFilterLife, statesStringJoiner
            );
        }

    }

}
