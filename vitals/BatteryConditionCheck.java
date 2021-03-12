package vitals;

import java.util.function.Function;

public class BatteryConditionCheck {

	static boolean batteryIsOk(float temperature, float soc, float chargeRate) {
		try {
			Function<Float, Function<Float, Boolean>> socCheckMethod = temperatureCheck(temperature);
			Function<Float, Boolean> chargeRateCheckMethod = socCheckMethod.apply(soc);
			return chargeRateCheckMethod.apply(chargeRate);
		} catch (NullPointerException ne) {
			return false;
		}
	}

	static Function<Float, Boolean> chargeRateCheck = (chargeRate) -> {
		if (chargeRate > 0.8) {
			MessageUtil.printMessage(MessageConstants.BREACH, MessageConstants.CHARGE_RATE, MessageConstants.HIGH);
			return false;
		}
		checkForMaxLevelWarning(chargeRate, 0.8f, MessageConstants.CHARGE_RATE, 5);
		return true;
	};
	static Function<Float, Function<Float, Boolean>> socCheck = (soc) -> {
		if (soc < 20) {
			MessageUtil.printMessage(MessageConstants.BREACH, MessageConstants.SOC, MessageConstants.LOW);
			return null;
		}
		if (soc > 80) {
			MessageUtil.printMessage(MessageConstants.BREACH, MessageConstants.SOC, MessageConstants.HIGH);
			return null;
		}
		checkForMinLevelWarning(soc, 20, 80, MessageConstants.SOC, 5);
		checkForMaxLevelWarning(soc, 80, MessageConstants.SOC, 5);
		return chargeRateCheck;
	};

	static Function<Float, Function<Float, Boolean>> temperatureCheck(float temperature) {
		if (temperature < 0) {
			MessageUtil.printMessage(MessageConstants.BREACH, MessageConstants.TEMPERATURE, MessageConstants.LOW);
			return null;
		}
		if (temperature > 45) {
			MessageUtil.printMessage(MessageConstants.BREACH, MessageConstants.TEMPERATURE, MessageConstants.HIGH);
			return null;
		}
		checkForMinLevelWarning(temperature, 0, 45, MessageConstants.TEMPERATURE, 5);
		checkForMaxLevelWarning(temperature, 45, MessageConstants.TEMPERATURE, 5);
		return socCheck;
	}

	static void checkForMinLevelWarning(float value, float min, float max, String type, float deltaPercentage) {
		float delta = (deltaPercentage / max) * 100;
		if (value <= (min + delta)) {
			MessageUtil.printMessage(MessageConstants.WARNING, type, MessageConstants.LOW);
		}
	}

	static void checkForMaxLevelWarning(float value, float max, String type, float deltaPercentage) {
		float delta = (deltaPercentage / max) * 100;
		if (value >= (max - delta)) {
			MessageUtil.printMessage(MessageConstants.WARNING, type, MessageConstants.HIGH);
		}
	}
}
