package be.kuleuven.med.brainfuck.domain.setting;

import gnu.io.SerialPort;

public class SerialPortSettings {
	
	/** Default bits per second for COM port. */
	public static final int DATA_RATE_9600_BAUD = 9600;
	
	public static final int DATA_RATE_115200_BAUD = 115200;
	
	private static final int DEFAULT_DATA_BITS = SerialPort.DATABITS_8;
	
	private static final int DEFAULT_STOP_BITS = SerialPort.STOPBITS_1;
	
	private static final int DEFAULT_PARITY_BITS = SerialPort.PARITY_NONE;

	private String name;
	
	private int dataRate = DATA_RATE_9600_BAUD;
	
	private int dataBits = DEFAULT_DATA_BITS;
	
	private int stopBits = DEFAULT_STOP_BITS;
	
	private int parityBits = DEFAULT_PARITY_BITS;
	
	public SerialPortSettings() { 	}

	public SerialPortSettings(int dataRate) {
		this.dataRate = dataRate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDataRate() {
		return dataRate;
	}

	public void setDataRate(int dataRate) {
		this.dataRate = dataRate;
	}

	public int getDataBits() {
		return dataBits;
	}

	public int getStopBits() {
		return stopBits;
	}

	public void setStopBits(int stopBits) {
		this.stopBits = stopBits;
	}

	public void setDataBits(int dataBits) {
		this.dataBits = dataBits;
	}

	public int getParityBits() {
		return parityBits;
	}

	public void setParityBits(int parityBits) {
		this.parityBits = parityBits;
	}
	
}
