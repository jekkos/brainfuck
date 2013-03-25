package be.kuleuven.med.brainfuck.core;

import java.util.Collections;
import java.util.List;

import org.jdesktop.application.AbstractBean;

import be.kuleuven.med.brainfuck.settings.LedSettings;

import com.google.common.collect.Lists;

public class LedMatrixAppModel extends AbstractBean {
	
	public static final String HEIGHT = "height";

	public static final String WIDTH = "width";
	
	public static final String ROW_PIN = "rowPin";
	
	public static final String COLUMN_PIN = "columnPin";
	
	public static final String ARDUINO_INITIALIZED = "arduinoInitialized";

	public static final String SELECTED_LED_SETTINGS = "selectedLedSettings";
	
	private List<String> serialPortNames = Lists.newArrayList();
	
	private String selectedSerialPortName;
	
	private boolean arduinoInitialized;
	
	private int rowPin;
	
	private int columnPin;
	
	private int width;
	
	private int height;

	private LedSettings selectedLedSettings;
	
	public LedMatrixAppModel(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
    public int getWidth() {
    	return width;
	}

	public void setWidth(int width) {
		firePropertyChange(WIDTH, this.width, this.width = width);
	}

	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		firePropertyChange(HEIGHT, this.height, this.height = height);
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

 	public List<String> getSerialPortNames() {
		return Collections.unmodifiableList(serialPortNames);
	}

	public void setSerialPortNames(List<String> serialPortNames) {
		this.serialPortNames = serialPortNames;
	}
	
	public String getSelectedSerialPortName() {
		return selectedSerialPortName;
	}

	public void setSelectedSerialPortName(String selectedSerialPortName) {
		this.selectedSerialPortName = selectedSerialPortName;
	}

	public boolean isArduinoInitialized() {
		return arduinoInitialized;
	}

	public void setArduinoInitialized(boolean arduinoInitialized) {
		firePropertyChange(ARDUINO_INITIALIZED, this.arduinoInitialized, this.arduinoInitialized = arduinoInitialized);
	}

	public void setSelectedLedSettings(LedSettings selectedLedSettings) {
		firePropertyChange(SELECTED_LED_SETTINGS, this.selectedLedSettings, this.selectedLedSettings = selectedLedSettings);
	}

	public LedSettings getSelectedLedSettings() {
		return selectedLedSettings;
	}

}
