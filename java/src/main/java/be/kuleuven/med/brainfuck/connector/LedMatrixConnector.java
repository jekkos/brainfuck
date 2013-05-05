package be.kuleuven.med.brainfuck.connector;

import be.kuleuven.med.brainfuck.domain.settings.LedSettings;
import be.kuleuven.med.brainfuck.domain.settings.SerialPortSettings;

public class LedMatrixConnector extends RXTXConnector {
	
	// default value for digital write
	private static final String NO_VALUE = "00";
	// function constants
	private static final String ANALOG_WRITE = "aw";
	private static final String DIGITAL_WRITE = "dw";
	private static final String PIN_MODE = "pm";
	// level constants
	private static final String LOW = " LOW";
	private static final String HIGH = "HIGH";
	// pin mode constants
	private static final String OUTPUT = "OUTP";
	// address all pins
	private static final String ALL = "al";

	private final int maxPortNumber;
	
	public static String buildAnalogWrite(boolean illuminated, int pin, int intensity) {
		StringBuilder result = new StringBuilder(NO_VALUE).append(ANALOG_WRITE);
		result.append(String.format("%04d", pin));
		result.append(String.format("%04d", illuminated ? intensity : 0));
		return result.toString();
	}
	
	public static String buildDigitalWrite(boolean illuminated, int pin) {
		StringBuilder result = new StringBuilder(NO_VALUE).append(DIGITAL_WRITE);
		result.append(String.format("%04d", pin));
		result.append(illuminated ? HIGH : LOW);
		return result.toString();
	}
	
	public LedMatrixConnector(SerialPortSettings serialPortSettings, int maxPortNumber) {
		super(serialPortSettings);
		this.maxPortNumber = maxPortNumber;
	}
	
	protected void initializeConnector(String serialPortName) {
		for (int i = 0; i < maxPortNumber; i++) {
			sendCommand(buildInitCommand(i));
		}
	}
	
	public void toggleLed(LedSettings ledSettings, boolean illuminated) {
		if (isMaxIntensity(ledSettings)) {
			sendCommand(buildDigitalWrite(illuminated, ledSettings.getRowPin()));
			sendCommand(buildDigitalWrite(illuminated, ledSettings.getColumnPin()));
		} else {
			// only modulate rows when using analog write
			sendCommand(buildAnalogWrite(illuminated, ledSettings.getRowPin(), ledSettings.getIntensity()));
			sendCommand(buildDigitalWrite(illuminated, ledSettings.getColumnPin()));
		}
	}
	
	public void disableAllLeds() {
		sendCommand(new StringBuilder(NO_VALUE).append(ALL).append(LOW).toString());
	}
	
	private boolean isMaxIntensity(LedSettings ledSettings) {
		return LedSettings.MAX_INTENSITY == ledSettings.getIntensity();
	}
	
	private String buildInitCommand(int pin) {
		StringBuilder result = new StringBuilder(NO_VALUE);
		result.append(PIN_MODE).append(pin).append(OUTPUT);
		return result.toString();
	}
	
}

