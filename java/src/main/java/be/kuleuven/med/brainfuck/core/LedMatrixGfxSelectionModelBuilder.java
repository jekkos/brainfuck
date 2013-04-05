package be.kuleuven.med.brainfuck.core;

import java.util.Collections;
import java.util.Set;

import be.kuleuven.med.brainfuck.settings.LedSettings;

import com.google.common.collect.Sets;

public class LedMatrixGfxSelectionModelBuilder {
	
	public static final LedMatrixGfxSelectionModel EMPTY_SELECTION = 
			new LedMatrixGfxSelectionModelBuilder().build();

	private LedMatrixGfxSelectionModel ledMatrixGfxSelectionModel;
	
	private Set<LedSettings> selectedLedSettings;

	public static LedMatrixGfxSelectionModel of(LedSettings ledSettings) {
		return new LedMatrixGfxSelectionModelBuilder().addLedSettings(ledSettings).build();
	}

	public LedMatrixGfxSelectionModelBuilder(LedMatrixGfxSelectionModel ledMatrixGfxSelectionModel) {
		this.ledMatrixGfxSelectionModel = ledMatrixGfxSelectionModel;
		Set<LedSettings> selectedLedSettings = ledMatrixGfxSelectionModel.getSelectedLedSettings();
		this.selectedLedSettings = Sets.newHashSet(selectedLedSettings);
	}

	private LedMatrixGfxSelectionModelBuilder() { 
		ledMatrixGfxSelectionModel = new LedMatrixGfxSelectionModel();
		selectedLedSettings = Sets.newHashSet();
	}
	
	public LedMatrixGfxSelectionModel build() {
		ledMatrixGfxSelectionModel.setSelectedLedSettings(selectedLedSettings);
		if (isRowSelected(selectedLedSettings)) {
			ledMatrixGfxSelectionModel.setRowSelected(true);
			// find common row pin (if there is one)
			ledMatrixGfxSelectionModel.setRowPin(findCommonRowPin(selectedLedSettings));
		}
		if (isColumnSelected(selectedLedSettings)) {
			ledMatrixGfxSelectionModel.setColumnSelected(true);
			// find common column pin (if there is one)
			ledMatrixGfxSelectionModel.setColumnPin(findCommonColumnPin(selectedLedSettings));
		}
		return ledMatrixGfxSelectionModel;
	}
	
	public LedMatrixGfxSelectionModelBuilder addRemoveLedSettings(LedSettings ledSettings) {
		if (ledMatrixGfxSelectionModel != null && ledMatrixGfxSelectionModel.isSelected(ledSettings)) {
			selectedLedSettings.remove(ledSettings);
		} else {
			selectedLedSettings.add(ledSettings);
		}
		return this;
	}
	
	public LedMatrixGfxSelectionModelBuilder addLedSettings(LedSettings ledSettings) {
		selectedLedSettings = Collections.singleton(ledSettings);
		return this;
	}
	
	private int findCommonRowPin(Set<LedSettings> selectedLedSettings) {
		int result = -1;
		for(LedSettings ledSettings : selectedLedSettings) {
			if (result == -1) {
				result = ledSettings.getRowPin();
			} else {
				return 0;
			}
		}
		return result;
	}
	
	private int findCommonColumnPin(Set<LedSettings> selectedLedSettings) {
		int result = -1;
		for(LedSettings ledSettings : selectedLedSettings) {
			if (result == -1) {
				result = ledSettings.getColumnPin();
			} else {
				return 0;
			}
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
		return true;
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
		return true;
	}
	
}
