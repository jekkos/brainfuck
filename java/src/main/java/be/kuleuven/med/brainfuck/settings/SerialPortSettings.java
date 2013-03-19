package be.kuleuven.med.brainfuck.settings;

import gnu.io.SerialPort;

public class SerialPortSettings {
	
	/** Default bits per second for COM port. */
	private static final int DEFAULT_DATA_RATE = 9600;
	
	private static final int DEFAULT_DATA_BITS = SerialPort.DATABITS_8;
	
	private static final int DEFAULT_STOP_BITS = SerialPort.STOPBITS_1;

	private String name;
	
	private int dataRate = DEFAULT_DATA_RATE;
	
	private int dataBits = DEFAULT_DATA_BITS;
	
	private int stopBits = DEFAULT_STOP_BITS;

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
	
}
