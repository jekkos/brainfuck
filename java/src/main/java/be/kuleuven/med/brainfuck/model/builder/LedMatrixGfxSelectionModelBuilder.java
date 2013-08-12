package be.kuleuven.med.brainfuck.model.builder;

import java.util.Collections;
import java.util.Set;

import be.kuleuven.med.brainfuck.domain.LedSettings;
import be.kuleuven.med.brainfuck.model.LedMatrixGfxSelectionModel;

import com.google.common.collect.Sets;

public class LedMatrixGfxSelectionModelBuilder {
	
	public static final LedMatrixGfxSelectionModel EMPTY_SELECTION = 
			new LedMatrixGfxSelectionModelBuilder().build();

	private LedMatrixGfxSelectionModel ledMatrixGfxSelectionModel;
	
	private Set<LedSettings> selectedLedSettings;

	public static LedMatrixGfxSelectionModel of(LedSettings ledSettings) {
		return new LedMatrixGfxSelectionModelBuilder(ledSettings).build();
	}

	public LedMatrixGfxSelectionModelBuilder(LedMatrixGfxSelectionModel ledMatrixGfxSelectionModel) {
		this.ledMatrixGfxSelectionModel = ledMatrixGfxSelectionModel;
		Set<LedSettings> selectedLedSettings = ledMatrixGfxSelectionModel.getSelectedLedSettings();
		this.selectedLedSettings = Sets.newHashSet(selectedLedSettings);
	}

	public LedMatrixGfxSelectionModelBuilder() { 
		ledMatrixGfxSelectionModel = new LedMatrixGfxSelectionModel();
		selectedLedSettings = Sets.newHashSet();
	}

	private LedMatrixGfxSelectionModelBuilder(LedSettings ledSettings) {
		ledMatrixGfxSelectionModel = new LedMatrixGfxSelectionModel();
		selectedLedSettings = Collections.singleton(ledSettings);
	}
	
	public LedMatrixGfxSelectionModel build() {
		ledMatrixGfxSelectionModel.setSelectedLedSettings(selectedLedSettings);
		// set row properties
		ledMatrixGfxSelectionModel.setRowSelected(isRowSelected(selectedLedSettings));
		ledMatrixGfxSelectionModel.setRowPin(findCommonRowPin(selectedLedSettings));
		// set column properties
		ledMatrixGfxSelectionModel.setColumnSelected(isColumnSelected(selectedLedSettings));
		ledMatrixGfxSelectionModel.setColumnPin(findCommonColumnPin(selectedLedSettings));
		// set intensity
		ledMatrixGfxSelectionModel.setIntensity(findCommonIntensity(selectedLedSettings));
		// set flicker frequency
		ledMatrixGfxSelectionModel.setFlickerFrequency(findCommonFlickerFrequency(selectedLedSettings));
		// set time to run
		ledMatrixGfxSelectionModel.setSecondsToRun(findCommonSecondsToRun(selectedLedSettings));
		return ledMatrixGfxSelectionModel;
	}
	
	public LedMatrixGfxSelectionModelBuilder addRemoveLedSettings(LedSettings... selectedLedSettingsList) {
		for (LedSettings ledSettings : selectedLedSettingsList) {
			if (ledMatrixGfxSelectionModel != null && ledMatrixGfxSelectionModel.isSelected(ledSettings)) {
				selectedLedSettings.remove(ledSettings);
			} else {
				selectedLedSettings.add(ledSettings);
			}
		}
		return this;
	}
	
	public LedMatrixGfxSelectionModelBuilder addLedSettings(LedSettings ledSettings) {
		selectedLedSettings = Collections.singleton(ledSettings);
		return this;
	}
	
	private int findCommonIntensity(Set<LedSettings> selectedLedSettings) {
		int result = LedSettings.MAX_INTENSITY;
		for(LedSettings ledSettings : selectedLedSettings) {
			if (result == LedSettings.MAX_INTENSITY) {
				result = ledSettings.getIntensity();
			} else if (ledSettings.getIntensity() != result) {
				return result;
			}
		}
		return result;
	}
	
	private Integer findCommonSecondsToRun(Set<LedSettings> selectedLedSettings) {
		Integer result = null;
		for(LedSettings ledSettings : selectedLedSettings) {
			if (result == null) {
				result = ledSettings.getSecondsToRun();
			} else if (ledSettings.getSecondsToRun() != result) {
				return result;
			}
		}
		return result;
	}
	
	private Integer findCommonFlickerFrequency(Set<LedSettings> selectedLedSettings) {
		Integer result = null;
		for(LedSettings ledSettings : selectedLedSettings) {
			if (result == null) {
				result = ledSettings.getFlickerFrequency();
			} else if (ledSettings.getFlickerFrequency() != result) {
				return result;
			}
		}
		return result;
	}
	
	private Integer findCommonRowPin(Set<LedSettings> selectedLedSettings) {
		Integer result = null;
		for(LedSettings ledSettings : selectedLedSettings) {
			if (result == null) {
				result = ledSettings.getRowPin();
			} else if (ledSettings.getRowPin() != result) {
				return null;
			}
		}
		if (result != null && result == 0) {
			return null;
		}
		return result;
	}
	
	private Integer findCommonColumnPin(Set<LedSettings> selectedLedSettings) {
		Integer result = null;
		for(LedSettings ledSettings : selectedLedSettings) {
			if (result == null) {
				result = ledSettings.getColumnPin();
			} else if (ledSettings.getColumnPin() != result) {
				return null;
			}
		}
		if (result != null && result == 0) {
			return null;
		}
		return result;
	}
	
	private boolean isRowSelected(Set<LedSettings> selectedLedSettings) {
		int selectedRow = -1;
		for(LedSettings ledSettings : selectedLedSettings) {
			if (selectedRow == -1) {
				selectedRow = ledSettings.getY();
			} else if (ledSettings.getY() != selectedRow){
				return false;
			}
		}
		return selectedRow > -1 ? true : false;
	}
	
	private boolean isColumnSelected(Set<LedSettings> selectedLedSettings) {
		int selectedColumn = -1;
		for(LedSettings ledSettings : selectedLedSettings) {
			if (selectedColumn == -1) {
				selectedColumn = ledSettings.getX();
			} else if (ledSettings.getX() != selectedColumn){
				return false;
			}
		}
		return selectedColumn > -1 ? true : false;
	}
	
}
