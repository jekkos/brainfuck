package be.kuleuven.med.brainfuck.model;

import java.util.Collections;
import java.util.Set;

import org.jdesktop.application.AbstractBean;

import be.kuleuven.med.brainfuck.domain.LedSettings;

import com.google.common.collect.Sets;

public class LedMatrixGfxSelectionModel extends AbstractBean {

	public static final String ROW_PIN = "rowPin";
	
	public static final String COLUMN_PIN = "columnPin";
	
	public static final String ROW_SELECTED = "rowSelected";
	
	public static final String COLUMN_SELECTED = "columnSelected";
	
	public static final String INTENSITY = "intensity";
	
	public static final String FLICKER_FREQUENCY = "flickerFrequency";
	
	public static final String SECONDS_TO_RUN = "secondsToRun";

	private Set<LedSettings> selectedLedSettings = Sets.newHashSet();

	private boolean rowSelected;
	
	private boolean columnSelected;

	private Integer rowPin;

	private Integer columnPin;
	
	private Integer flickerFrequency = LedSettings.MIN_FLICKER_FREQUENCY;
	
	private Integer secondsToRun = LedSettings.DEFAULT_SECONDS_TO_RUN;

	private int intensity = LedSettings.MAX_INTENSITY;
	
	public LedMatrixGfxSelectionModel() { }

	public LedMatrixGfxSelectionModel(Set<LedSettings> selectedLedSettings) {
		this.selectedLedSettings = selectedLedSettings;
	}

	public boolean isCleared() {
		return selectedLedSettings.isEmpty();
	}

	public boolean isSelected(LedSettings ledSettings) {
		return selectedLedSettings.contains(ledSettings);
	}
	
	public Integer getRowPin() {
		return rowPin;
	}

	public void setRowPin(Integer rowPin) {
		firePropertyChange(ROW_PIN, this.rowPin , this.rowPin = rowPin);
	}

	public Integer getColumnPin() {
		return columnPin;
	}

	public void setColumnPin(Integer columnPin) {
		firePropertyChange(COLUMN_PIN, this.columnPin , this.columnPin = columnPin);
	}

	public boolean isRowSelected() {
		return rowSelected;
	}

	public void setRowSelected(boolean rowSelected) {
		firePropertyChange(ROW_SELECTED, this.rowSelected, this.rowSelected = rowSelected);
	}

	public boolean isColumnSelected() {
		return columnSelected;
	}

	public void setColumnSelected(boolean columnSelected) {
		firePropertyChange(COLUMN_SELECTED, this.columnSelected, this.columnSelected = columnSelected);
	}
	
	public int getIntensity() {
		return intensity;
	}

	public void setIntensity(int intensity) {
		firePropertyChange(INTENSITY, this.intensity, this.intensity = intensity);
	}

	public Integer getFlickerFrequency() {
		return flickerFrequency;
	}

	public void setFlickerFrequency(Integer flickerFrequency) {
		firePropertyChange(FLICKER_FREQUENCY, this.flickerFrequency, this.flickerFrequency = flickerFrequency);
	}

	public Integer getSecondsToRun() {
		return secondsToRun;
	}

	public void setSecondsToRun(Integer secondsToRun) {
		firePropertyChange(SECONDS_TO_RUN, this.secondsToRun, this.secondsToRun = secondsToRun);
	}
	
	public Set<LedSettings> getSelectedLedSettings() {
		return Collections.unmodifiableSet(selectedLedSettings);
	}

	public void setSelectedLedSettings(Set<LedSettings> selectedLedSettings) {
		this.selectedLedSettings = selectedLedSettings;
	}
	
}
