package be.kuleuven.med.brainfuck.connector;

import gnu.io.CommPortIdentifier;

import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Logger;

import be.kuleuven.med.brainfuck.domain.settings.SerialPortSettings;

import com.google.common.collect.Lists;

public abstract class SerialPortConnector {
	
	protected final static Logger LOGGER = Logger.getLogger(SerialPortConnector.class);

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

	public abstract void initialize(String serialPortName) throws Exception;
	
	public abstract boolean isInitialized();
	
	public abstract boolean close();
	
	public SerialPortConnector(SerialPortSettings serialPortSettings) {
		this.serialPortSettings = serialPortSettings;
	}
	
	public SerialPortSettings getSerialPortSettings() {
		return serialPortSettings;
	}

	public void setSerialPortSettings(SerialPortSettings serialPortSettings) {
		this.serialPortSettings = serialPortSettings;
	}

	public String getSelectedSerialPortName() {
		return serialPortSettings.getName();
	}

	
}

