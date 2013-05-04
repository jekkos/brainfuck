package be.kuleuven.med.brainfuck.connector;

import be.kuleuven.med.brainfuck.connector.ThorlabsDC2100Connector.DriverStatus;
import be.kuleuven.med.brainfuck.connector.ThorlabsDC2100Connector.OperationMode;
import be.kuleuven.med.brainfuck.domain.settings.SerialPortSettings;

public abstract class ThorlabsConnector extends SerialPortConnector {

	public ThorlabsConnector(SerialPortSettings serialPortSettings) {
		super(serialPortSettings);
	}

	public static final String DEVICE_NAME = "Thorlabs DC2010/DC2100";

	public abstract boolean isLedOn() throws InterruptedException;

	public abstract boolean setLedOn(boolean on) throws InterruptedException;

	public abstract String getPwmFrequency() throws InterruptedException;

	public abstract boolean setPwmFrequency(int frequency)
			throws InterruptedException;

	public abstract String getPwmCurrent() throws InterruptedException;

	public abstract boolean setPwmCurrent(int pwmCurrent)
			throws InterruptedException;

	public abstract String getDutyCycle() throws InterruptedException;

	public abstract boolean setDutyCycle(int dutyCycle)
			throws InterruptedException;

	public abstract String getPwmCounts() throws InterruptedException;

	public abstract boolean setPwmCounts(int pwmCounts)
			throws InterruptedException;

	public abstract OperationMode getOperationMode()
			throws InterruptedException;

	public abstract boolean setOperationMode(OperationMode operationMode)
			throws InterruptedException;

	public abstract DriverStatus getStatus() throws InterruptedException;

	public abstract String getLastError() throws InterruptedException;

}