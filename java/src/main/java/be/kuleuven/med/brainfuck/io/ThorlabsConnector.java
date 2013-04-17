package be.kuleuven.med.brainfuck.io;

import be.kuleuven.med.brainfuck.domain.setting.SerialPortSettings;

public class ThorlabsConnector extends SerialPortConnector {
	
	public static final String DEVICE_NAME = "Thorlabs DC2010/DC2100";

	private static final String DEVICE_OK = "0";
	
	private static final String DC2100 = "DC2100";
	
	private static final String DC2010 = "DC2010";
	
	private static final String LED_ON = "1";
	
	private static final String LED_OFF = "0";
	
	public ThorlabsConnector(SerialPortSettings serialPortSettings) {
		super(serialPortSettings);
	}

	@Override
	protected void initializeConnector(String serialPortName) throws InterruptedException {
		sendCommand("n?");
		String response = waitForResponse();
		if (response.contains(DC2010) || response.contains(DC2100)) {
			LOGGER.info("Thorlabs DC2XXX driver initialized successfully");
		} else {
			String lastError = getLastError();
			LOGGER.error("Failed to initialize Thorlabs driver " + lastError);
			throw new IllegalStateException(lastError);
		}
	}
	
	public boolean isLedOn() throws InterruptedException {
		sendCommand("o?");
		return LED_ON.equals(waitForResponse());
	}
	
	public boolean setLedOn(boolean on) throws InterruptedException {
		String ledState = on ? LED_ON : LED_OFF;
		sendCommand("o " + ledState);
		return isDeviceOk();
	}
	
	public String getPwmFrequency() throws InterruptedException {
		sendCommand("pf?");
		return waitForResponse();
	}
	
	public boolean setPwmFrequency(int frequency) throws InterruptedException {
		sendCommand("pf " + frequency);
		return isDeviceOk();
	}
	
	public String getPwmCurrent() throws InterruptedException {
		sendCommand("pc?");
		return waitForResponse();
	}
	
	public boolean setPwmCurrent(int pwmCurrent) throws InterruptedException {
		sendCommand("pc " + pwmCurrent);
		return isDeviceOk();
	}
	
	public String getDutyCycle() throws InterruptedException {
		sendCommand("pd?");
		return waitForResponse();
	}
	
	public boolean setDutyCycle(int dutyCycle) throws InterruptedException {
		sendCommand("pd " + dutyCycle);
		return isDeviceOk();
	}
	
	public String getPwmCounts() throws InterruptedException {
		sendCommand("pn?");
		return waitForResponse();
	}
	
	public boolean setPwmCounts(int pwmCounts) throws InterruptedException {
		sendCommand("pn " + pwmCounts);
		return isDeviceOk();
	}
	
	public OperationMode getOperationMode() throws InterruptedException {
		sendCommand("m?");
		return OperationMode.findOperationMode(waitForResponse());
	}
	
	public boolean setOperationMode(OperationMode operationMode) throws InterruptedException {
		boolean result = setLedOn(false);
		sendCommand("m " + operationMode.ordinal());
		return result && isDeviceOk();
	}

	public DriverStatus getStatus() throws InterruptedException {
		sendCommand("r?");
		return DriverStatus.findDriverStatus(waitForResponse());
	}
	
	private boolean isDeviceOk() throws InterruptedException {
		return isInitialized() && DEVICE_OK.equals(waitForResponse());
	}
	
	public String getLastError() throws InterruptedException {
		sendCommand("e?");
		return waitForResponse();
	}
	
	@Override
	protected void sendCommand(String command) {
		if (isInitialized()) {
			super.sendCommand(command);
		}
	}

	public enum DriverStatus {
		NO_LED(0x02), LED_OPEN(0x08), LIMIT(0x20), NO_FAULT(0x00);
		
		private int bitMask;
		
		private DriverStatus(int bitMask) {
			this.bitMask = bitMask;
		}
		
		public static DriverStatus findDriverStatus(String aByte) {
			for (DriverStatus driverStatus : values()) {
				if ((Integer.parseInt(aByte , 16) & driverStatus.bitMask) == 1) {
					return driverStatus;
				}
			}
			return null;
		}
		
	}
	
	public enum OperationMode {
		
		CONSTANT_CURRENT, PWM, EXTERNAL_CONTROL;
		
		public static OperationMode findOperationMode(String mode) {
			for (OperationMode operationMode : values()) {
				if (Integer.toString(operationMode.ordinal()).equals(mode)) {
					return operationMode;
				}
			}
			return null;
		}
		
	}

}
