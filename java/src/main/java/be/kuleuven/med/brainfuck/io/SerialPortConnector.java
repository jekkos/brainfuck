package be.kuleuven.med.brainfuck.io;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Logger;

import be.kuleuven.med.brainfuck.settings.SerialPortSettings;

import com.google.common.collect.Lists;

public abstract class SerialPortConnector implements SerialPortEventListener {
	
	protected final static Logger LOGGER = Logger.getLogger(SerialPortConnector.class);

	/** Milliseconds to block while waiting for port open */
	private final static int TIME_OUT = 2000;

	private SerialPort serialPort;

	/** Buffered input stream from the port */
	private InputStream input;
	/** The output stream to the port */
	private OutputStream output;
	
	private SerialPortSettings serialPortSettings;
	
	public static List<String> getSerialPortNames() {
		List<String> result = Lists.newArrayList();
		@SuppressWarnings("rawtypes")
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
		// iterate through, looking for the port
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			result.add(currPortId.getName());
		}
		return result;
	}
	
	public SerialPortConnector(SerialPortSettings serialPortSettings) {
		this.serialPortSettings = serialPortSettings;
	}

	public void initialize(String serialPortName) throws Exception {
		CommPortIdentifier portId = null;
		@SuppressWarnings("rawtypes")
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		// iterate through, looking for the port
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			if (currPortId.getName().equals(serialPortName)) {
				portId = currPortId;
				break;
			}
		}
		serialPortSettings.setName(serialPortName);
		// open serial port, and use class name for the appName.
		serialPort = (SerialPort) portId.open(this.getClass().getName(),
				TIME_OUT);

		// set port parameters
		serialPort.setSerialPortParams(serialPortSettings.getDataRate(),
				serialPortSettings.getDataBits(),
				serialPortSettings.getStopBits(),
				serialPortSettings.getParityBits());

		// open the streams
		input = serialPort.getInputStream();
		output = serialPort.getOutputStream();

		// add event listeners
		serialPort.addEventListener(this);
		serialPort.notifyOnDataAvailable(true);
		
		initializeConnector(serialPortName);
	}
	
	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public abstract void serialEvent(SerialPortEvent oEvent);

	/**
	 * Perform some connector specific initialization.
	 */
	protected abstract void initializeConnector(String serialPortName);
	
	public String getSelectedSerialPortName() {
		return serialPortSettings.getName();
	}
	
 	public OutputStream getOutput() {
		return output;
	}
 	
	public void setOutput(OutputStream output) {
		this.output = output;
	}

	public InputStream getInput() {
		return input;
	}
	
	public void setInput(InputStream input) {
		this.input = input;
	}

	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

}

