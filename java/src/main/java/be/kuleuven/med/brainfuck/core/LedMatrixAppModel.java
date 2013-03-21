package be.kuleuven.med.brainfuck.core;

import java.util.List;

import org.jdesktop.application.AbstractBean;

import be.kuleuven.med.brainfuck.entity.LedMatrix;
import be.kuleuven.med.brainfuck.settings.LedMatrixSettings;

import com.jgoodies.binding.list.SelectionInList;

public class LedMatrixAppModel extends AbstractBean {
	
	public static final String HEIGHT = "height";

	public static final String WIDTH = "width";
	
	public static final String ROW_PIN = "rowPin";
	
	public static final String COLUMN_PIN = "columnPin";
	
	public static final String ARDUINO_INITIALIZED = "arduinoInitialized";

	private LedMatrix ledMatrix;

	private SelectionInList<String> serialPortNameSelectionInList;
	
	private boolean arduinoInitialized;
	
	private int rowPin;
	
	private int columnPin;
	
	public LedMatrixAppModel(LedMatrixSettings ledMatrixSettings) {
		this.ledMatrix = ledMatrixSettings.getLedMatrix();
		serialPortNameSelectionInList = new SelectionInList<String>();
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
	
	public void setHeight(int height) {
		firePropertyChange(HEIGHT, ledMatrix.getHeight(), height);
		ledMatrix.setHeight(height);
	}

	public int getRowPin() {
		return rowPin;
	}

	public void setRowPin(int rowPin) {
		firePropertyChange(ROW_PIN, this.rowPin , this.rowPin = rowPin);
	}

	public int getColumnPin() {
		return columnPin;
	}

	public void setColumnPin(int columnPin) {
		firePropertyChange(COLUMN_PIN, this.columnPin , this.columnPin = columnPin);
	}

	public SelectionInList<String> getSerialPortSelectionInList() {
		return serialPortNameSelectionInList;
	}

 	public List<String> getSerialPortNames() {
		return this.serialPortNameSelectionInList.getList();
	}

	public void setSerialPortNames(List<String> serialPortNames) {
		this.serialPortNameSelectionInList.setList(serialPortNames);
	}
	
	public String getSelectedSerialPortName() {
		return serialPortNameSelectionInList.getSelection();
	}

	public void setSelectedSerialPortName(String selectedSerialPortName) {
		serialPortNameSelectionInList.setSelection(selectedSerialPortName);
	}

	public boolean isArduinoInitialized() {
		return arduinoInitialized;
	}

	public void setArduinoInitialized(boolean arduinoInitialized) {
		firePropertyChange(ARDUINO_INITIALIZED, this.arduinoInitialized, this.arduinoInitialized = arduinoInitialized);
	}

}
