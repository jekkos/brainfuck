package be.kuleuven.med.brainfuck.io;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

import org.apache.log4j.Logger;

import be.kuleuven.med.brainfuck.settings.SerialPortSettings;

import com.google.common.collect.Lists;

public abstract class SerialPortConnector implements SerialPortEventListener {
	
	protected final static Logger LOGGER = Logger.getLogger(SerialPortConnector.class);

	/** Milliseconds to block while waiting for port open */
	private final static int TIME_OUT = 2000;

	public static final String RETURN = "\r\n";

	private SerialPort serialPort;

	/** Buffered input stream from the port */
	private InputStream input;
	/** The output stream to the port */
	private OutputStream output;
	
	private SerialPortSettings serialPortSettings;
	
	private BlockingQueue<String> queue = new SynchronousQueue<String>();
	
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
	
	/**
	 * Perform some connector specific initialization.
	 */
	protected abstract void initializeConnector(String serialPortName) throws Exception;

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
		if (isInitialized()) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}
	
	public boolean isInitialized() {
		return serialPort != null;
	}

	protected void sendCommand(String command) {
		try {
			if (getOutput() != null) {
				command = new StringBuilder(command).append(RETURN).toString();
				getOutput().write(command.getBytes());
				LOGGER.info("Command sent: " + command);
			} else {
				LOGGER.info("no serial device attached..");
			}
		} catch (IOException e1) {
			LOGGER.error(e1);
		}
	}
	
	protected synchronized String waitForResponse() throws InterruptedException {
		return queue.take();
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
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
				queue.put(result);
			} catch (Exception e) {
				LOGGER.error(e);
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}

}

