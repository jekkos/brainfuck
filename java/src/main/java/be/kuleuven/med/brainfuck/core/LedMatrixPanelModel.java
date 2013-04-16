package be.kuleuven.med.brainfuck.core;

import java.util.Collections;
import java.util.List;

import org.jdesktop.application.AbstractBean;

import be.kuleuven.med.brainfuck.settings.ExperimentSettings;
import be.kuleuven.med.brainfuck.settings.LedMatrixSettings;

import com.google.common.collect.Lists;

public class LedMatrixPanelModel extends AbstractBean {
	
	public static final String HEIGHT = "height";

	public static final String WIDTH = "width";
	
	public static final String LED_MATRIX_CONNECTOR_INITIALIZED = "ledMatrixConnectorInitialized";
	
	public static final String THORLABS_CONNECTOR_INITIALIZED = "thorlabsConnectorInitialized";
	
	public static final String EXPERIMENT_INITIALIZED = "experimentInitialized";
	
	public static final String EXPERIMENT_RUNNING = "experimentRunning";

	public static final String EXPERIMENT_SETTINGS = "experimentSettings";
	
	public static final String SERIAL_PORT_NAMES = "serialPortNames";
	
	private List<String> serialPortNames = Lists.newArrayList();
	
	private String selectedLedMatrixPortName;
	
	private String selectedThorlabsPortName;
	
	private boolean ledMatrixConnectorInitialized;
	
	private boolean thorlabsConnectorInitialized;
	
	private boolean experimentInitialized;
	
	private boolean experimentRunning;
	
	private int width;
	
	private int height;
	
	private ExperimentSettings experimentSettings;
	
	public LedMatrixPanelModel(LedMatrixSettings ledMatrixSettings, ExperimentSettings experimentSettings) {
		this.width = ledMatrixSettings.getWidth();
		this.height = ledMatrixSettings.getHeight();
		this.experimentSettings = experimentSettings;
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

 	public List<String> getSerialPortNames() {
		return Collections.unmodifiableList(serialPortNames);
	}

	public void setSerialPortNames(List<String> serialPortNames) {
		firePropertyChange(SERIAL_PORT_NAMES, this.serialPortNames, this.serialPortNames = serialPortNames);
	}
	
	public String getSelectedLedMatrixPortName() {
		return selectedLedMatrixPortName;
	}

	public void setSelectedLedMatrixPortName(String selectedLedMatrixPortName) {
		this.selectedLedMatrixPortName = selectedLedMatrixPortName;
	}
	
	public String getSelectedThorlabsPortName() {
		return selectedThorlabsPortName;
	}

	public void setSelectedThorlabsPortName(String selectedThorlabsPortName) {
		this.selectedThorlabsPortName = selectedThorlabsPortName;
	}

	public boolean isLedMatrixConnectorInitialized() {
		return ledMatrixConnectorInitialized;
	}

	public void setLedMatrixConnectorInitialized(boolean ledMatrixConnectorInitialized) {
		firePropertyChange(LED_MATRIX_CONNECTOR_INITIALIZED, this.ledMatrixConnectorInitialized, this.ledMatrixConnectorInitialized = ledMatrixConnectorInitialized);
	}
	
	public boolean isThorlabsConnectorInitialized() {
		return thorlabsConnectorInitialized;
	}

	public void setThorlabsConnectorInitialized(boolean thorlabsConnectorInitialized) {
		firePropertyChange(THORLABS_CONNECTOR_INITIALIZED, this.thorlabsConnectorInitialized, this.thorlabsConnectorInitialized = thorlabsConnectorInitialized);
	}

	public boolean isExperimentInitialized() {
		return experimentInitialized;
	}

	public void setExperimentInitialized(boolean experimentInitialized) {
		firePropertyChange(EXPERIMENT_INITIALIZED, this.experimentInitialized, this.experimentInitialized = experimentInitialized);
	}

	public void setExperimentSettings(ExperimentSettings experimentSettings) {
		firePropertyChange(EXPERIMENT_SETTINGS, this.experimentSettings, this.experimentSettings = experimentSettings);
	}

	public ExperimentSettings getExperimentSettings() {
		return experimentSettings;
	}

	public boolean isExperimentRunning() {
		return experimentRunning;
	}

	public void setExperimentRunning(boolean experimentRunning) {
		firePropertyChange(EXPERIMENT_RUNNING, this.experimentRunning, this.experimentRunning = experimentRunning);
	}

}
