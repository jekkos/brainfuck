package be.kuleuven.med.brainfuck.modelbuilder;

import java.util.Map;

import be.kuleuven.med.brainfuck.domain.setting.LedMatrixSettings;
import be.kuleuven.med.brainfuck.domain.setting.LedPosition;
import be.kuleuven.med.brainfuck.domain.setting.LedSettings;
import be.kuleuven.med.brainfuck.model.LedMatrixGfxModel;

import com.google.common.collect.Maps;

public class LedMatrixGfxModelBuilder {
	
	private final LedMatrixGfxModel ledMatrixGfxModel;
	
	public LedMatrixGfxModelBuilder(LedMatrixSettings ledMatrixSettings) {
		// popuplate internal data structure
		ledMatrixGfxModel = new LedMatrixGfxModel(ledMatrixSettings, buildMatrix(ledMatrixSettings));
		ledMatrixGfxModel.setLedMatrixGfxSelectionModel(LedMatrixGfxSelectionModelBuilder.EMPTY_SELECTION);
	}
	
	public LedMatrixGfxModelBuilder(LedMatrixGfxModel ledMatrixGfxModel) {
		this.ledMatrixGfxModel = ledMatrixGfxModel;
	}
	
	private Map<LedPosition, LedSettings> buildMatrix(LedMatrixSettings ledMatrixSettings) {
		Map<LedPosition, LedSettings> result = Maps.newHashMap();
		for(LedSettings ledSettings : ledMatrixSettings.getLedSettingsList()) {
			result.put(ledSettings.getLedPosition(), ledSettings);
		}
		return result;
	}
	
	private LedMatrixSettings getLedMatrixSettings() {
		return ledMatrixGfxModel.getLedMatrixSettings();
	}
	
	private void removeLedSettings(int width, int height) {
		int oldWidth = getLedMatrixSettings().getWidth();
		int oldHeight = getLedMatrixSettings().getHeight();
		for (int i = 0; i < oldWidth; i++) {
			boolean removeRow = i >= width;
			for (int j = 0; j < oldHeight; j++) {
				boolean removeColumn = j >= height;
				LedPosition ledPosition = LedPosition.ledPositionFor(i, j);
				LedSettings ledSettings = ledMatrixGfxModel.getLedSettings(ledPosition);
				if (ledSettings != null && (removeRow || removeColumn)) {
					ledMatrixGfxModel.removeLedSettings(ledSettings);
				}
			}
		}
	}
	
	private void addLedSettings(int width, int height) {
		// go throguh all available settings and create new entries
		for (int i = 0; i < width; i ++) {
			for (int j = 0; j < height; j ++) {
				LedPosition ledPosition = LedPosition.ledPositionFor(i, j);
				if (ledMatrixGfxModel.getLedSettings(ledPosition) == null) {
					LedSettings ledSettings = new LedSettings(ledPosition);
					ledMatrixGfxModel.addLedSettings(ledSettings);
				}
			}
		}
	}
	
	public LedMatrixGfxModelBuilder resizeMatrix(int width, int height) {
		removeLedSettings(width, height);
		addLedSettings(width, height);
		// add or remove nodes..
		getLedMatrixSettings().setWidth(width);
		getLedMatrixSettings().setHeight(height);
		return this;
	}
	
	public int getWidth() {
		return getLedMatrixSettings().getWidth();
	}

	public int getHeight() {
		return getLedMatrixSettings().getHeight();
	}
	
	public LedMatrixGfxModel build() {
		return ledMatrixGfxModel;
	}

}
