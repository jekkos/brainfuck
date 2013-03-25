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

import org.apache.log4j.Logger;

import be.kuleuven.med.brainfuck.settings.SerialPortSettings;

import com.google.common.collect.Lists;

public class SerialPortConnector implements SerialPortEventListener {
	
	private final static Logger LOGGER = Logger.getLogger(SerialPortConnector.class);

	/** Milliseconds to block while waiting for port open */
	private final static int TIME_OUT = 2000;

	private SerialPort serialPort;

	/** Buffered input stream from the port */
	private InputStream input;
	/** The output stream to the port */
	private OutputStream output;
	
	private SerialPortSettings serialPortSettings;
	
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
	}
	
	public List<String> getSerialPortNames() {
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
	
	public String getSelectedSerialPortName() {
		return serialPortSettings.getName();
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

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				int available = input.available();
				byte chunk[] = new byte[available];
				input.read(chunk, 0, available);
				// Displayed results are codepage dependent
				String result = new String(chunk);
				// result coming back from arduino
				LOGGER.info(result);
			} catch (Exception e) {
				LOGGER.error(e);
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}

	public void shock(int strength) {
		try {
			if (output != null) {
				// writeout to arduino
				output.write(new String(strength + "\r\n").getBytes());
				LOGGER.info("SHOCK LEVEL " + strength);
			} else {
				LOGGER.info("no serial device attached..");
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}

