package be.kuleuven.med.brainfuck.io;

import gnu.io.SerialPortEvent;

import java.io.IOException;

import be.kuleuven.med.brainfuck.settings.LedSettings;
import be.kuleuven.med.brainfuck.settings.SerialPortSettings;

public class LedMatrixConnector extends SerialPortConnector {
	
	// default value for digital write
	private static final String NO_VALUE = "00";
	// function constants
	public static final String ANALOG_WRITE = "aw";
	public static final String DIGITAL_WRITE = "dw";
	public static final String PIN_MODE = "pm";
	// level constants
	public static final String LOW = "LOW";
	public static final String HIGH = "HIGH";
	// pin mode constants
	private static final String OUTPUT = "OUTP";
	// address all pins
	private static final String ALL = "al";
	
	private final int maxPortNumber;
	
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
		ledSettings.setIlluminated(illuminated);
		toggleLed(ledSettings);
	}
	
	public void toggleLed(LedSettings ledSettings) {
		sendCommand(buildCommand(ledSettings));
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
	
	private String buildCommand(LedSettings ledSettings) {
		StringBuilder result = new StringBuilder(NO_VALUE);
		if (isMaxIntensity(ledSettings)) {
			result.append(NO_VALUE).append(DIGITAL_WRITE);
		} else {
			result.append(ledSettings.getIntensity()).append(ANALOG_WRITE);
		}
		result.append(ledSettings.getRowPin());
		result.append(ledSettings.isIlluminated() ? HIGH : LOW);
		return result.toString();
	}
	
	private void sendCommand(String command) {
		try {
			if (getOutput() != null) {
				// writeout to arduino
				command = new StringBuilder(command).append("\r\n").toString();
				getOutput().write(command.getBytes());
				// TODO eventually remove logging here for timing purposes
				LOGGER.info("Command sent: " + command);
			} else {
				LOGGER.info("no serial device attached..");
			}
		} catch (IOException e1) {
			LOGGER.error(e1);
		}
	}

	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				int available = getInput().available();
				byte chunk[] = new byte[available];
				getInput().read(chunk, 0, available);
				// Displayed results are codepage dependent
				String result = new String(chunk);
				// result coming back from arduino
				LOGGER.info("Data received: " + result);
			} catch (Exception e) {
				LOGGER.error(e);
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}
	
}

