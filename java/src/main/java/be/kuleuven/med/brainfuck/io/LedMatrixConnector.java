package be.kuleuven.med.brainfuck.io;

import gnu.io.SerialPortEvent;

import java.io.IOException;
import java.util.Set;

import be.kuleuven.med.brainfuck.settings.LedSettings;
import be.kuleuven.med.brainfuck.settings.SerialPortSettings;

import com.google.common.collect.Sets;

public class LedMatrixConnector extends SerialPortConnector {
	// default value for digital write
	private static final String MAX_INTENSITY = "00";
	// function constants
	public static final String ANALOG_WRITE = "aw";
	public static final String DIGITAL_WRITE = "dw";
	public static final String PIN_MODE = "pm";
	// level constants
	public static final String LOW = "LOW";
	public static final String HIGH = "HIGH";
	// pin mode constants
	private static final String OUTPUT = "OUTP";
	
	private Set<Integer> initializedPins = Sets.newHashSet();

	public LedMatrixConnector(SerialPortSettings serialPortSettings) {
		super(serialPortSettings);
	}

	public void toggleLed(LedSettings ledSettings) {
		initPins(ledSettings);
		sendCommand(buildCommand(ledSettings));
	}
	
	private void initPins(LedSettings ledSettings) {
		if (!initializedPins.contains(ledSettings.getRowPin())) {
			sendCommand(buildInitCommand(ledSettings.getRowPin()));
			initializedPins.add(ledSettings.getRowPin());
		}
		if (!initializedPins.contains(ledSettings.getColumnPin())) {
			sendCommand(buildInitCommand(ledSettings.getColumnPin()));
			initializedPins.add(ledSettings.getColumnPin());
		}
	}
	
	private boolean isMaxIntensity(LedSettings ledSettings) {
		return LedSettings.MAX_INTENSITY == ledSettings.getIntensity();
	}
	
	private String buildInitCommand(int pin) {
		StringBuilder result = new StringBuilder(MAX_INTENSITY);
		result.append(PIN_MODE).append(pin).append(OUTPUT);
		return result.toString();
	}
	
	private String buildCommand(LedSettings ledSettings) {
		StringBuilder result = new StringBuilder(MAX_INTENSITY);
		if (isMaxIntensity(ledSettings)) {
			result.append(MAX_INTENSITY).append(DIGITAL_WRITE);
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
				int available = input.available();
				byte chunk[] = new byte[available];
				input.read(chunk, 0, available);
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

