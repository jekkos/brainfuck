package be.kuleuven.med.brainfuck.core;

import java.util.List;

import org.jdesktop.application.AbstractBean;

import be.kuleuven.med.brainfuck.entity.LedMatrix;

public class LedMatrixModel extends AbstractBean {
	
	public static final String HEIGHT = "height";

	public static final String WIDTH = "width";

	public static final String SERIAL_PORT_NAMES = "serialPortNames";
	
	public static final String SELECTED_SERIAL_PORT_NAME = "selectedSerialPort";
	
	private LedMatrix ledMatrix;
		
	private List<String> serialPortNames;
	
	private String selectedSerialPortName;

	public LedMatrixModel(LedMatrix ledMatrix) {
		this.ledMatrix = ledMatrix;
	}
	
    public int getWidth() {
		return ledMatrix.getWidth();
	}

	public void setWidth(int width) {
		firePropertyChange(WIDTH, ledMatrix.getWidth(), width);
		ledMatrix.setWidth(width);
	}

	public int getHeight() {
		return ledMatrix.getHeight();
	}
	
	public String getHeightString() {
		return Integer.toString(getHeight());
	}
	
	public String getWidthString() {
		return Integer.toString(getWidth());
	}

	public void setHeight(int height) {
		firePropertyChange(HEIGHT, ledMatrix.getHeight(), height);
		ledMatrix.setHeight(height);
	}

	public List<String> getSerialPortNames() {
		return serialPortNames;
	}

	public void setSerialPortNames(List<String> serialPortNames) {
		firePropertyChange(SERIAL_PORT_NAMES, this.serialPortNames, this.serialPortNames = serialPortNames);
	}
	
	public String getSelectedSerialPortName() {
		return selectedSerialPortName;
	}

	public void setSelectedSerialPortName(String selectedSerialPortName) {
		firePropertyChange(SELECTED_SERIAL_PORT_NAME, this.selectedSerialPortName, this.selectedSerialPortName = selectedSerialPortName);
	}

}
