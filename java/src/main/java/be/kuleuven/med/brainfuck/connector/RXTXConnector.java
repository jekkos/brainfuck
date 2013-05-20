package be.kuleuven.med.brainfuck.connector;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

import be.kuleuven.med.brainfuck.domain.settings.SerialPortSettings;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public abstract class RXTXConnector extends SerialPortConnector implements SerialPortEventListener {

	/** Milliseconds to block while waiting for port open */
	protected final static int TIME_OUT = 2000;
	
	public static final String RETURN = "\r\n";

	protected SerialPort serialPort;
	/** Buffered input stream from the port */
	protected InputStream input;
	/** The output stream to the port */
	protected OutputStream output;
	
	private BlockingQueue<String> queue = new SynchronousQueue<String>();

	public RXTXConnector(SerialPortSettings serialPortSettings) {
		super(serialPortSettings);
	}
	
	protected abstract void initializeConnector(String serialPortName);

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

	protected String waitForResponse() throws InterruptedException {
		return queue.take();
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public void serialEvent(SerialPortEvent oEvent) {
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

	public void initialize(String serialPortName) throws Exception {
		@SuppressWarnings("rawtypes")
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
	
		// iterate through, looking for the port
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			if (currPortId.getName().equals(serialPortName)) {
				getSerialPortSettings().setName(serialPortName);
				// open serial port, and use class name for the appName.
				serialPort = (SerialPort) currPortId.open(this.getClass().getName(),
						TIME_OUT);
			
				// set port parameters
				serialPort.setSerialPortParams(getSerialPortSettings().getDataRate(),
						getSerialPortSettings().getDataBits(),
						getSerialPortSettings().getStopBits(),
						getSerialPortSettings().getParityBits());
			
				// open the streams
				input = serialPort.getInputStream();
				output = serialPort.getOutputStream();
			
				// add event listeners
				serialPort.addEventListener(this);
				serialPort.notifyOnDataAvailable(true);
				
				initializeConnector(serialPortName);
				break;
			}
		}
		
	}

	public synchronized boolean close() {
		if (isInitialized()) {
			serialPort.removeEventListener();
			serialPort.close();
			return true;
		}
		return false;
	}

	public boolean isInitialized() {
		return serialPort != null;
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


}