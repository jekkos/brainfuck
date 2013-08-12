package be.kuleuven.med.brainfuck.domain.builder;

import be.kuleuven.med.brainfuck.domain.LedMatrixSettings;
import be.kuleuven.med.brainfuck.domain.LedPosition;
import be.kuleuven.med.brainfuck.domain.LedSettings;


public class LedMatrixSettingsBuilder {

	private LedMatrixSettings ledMatrixSettings;
	
	public LedMatrixSettingsBuilder() {
		this.ledMatrixSettings = new LedMatrixSettings();
	}
	
	public LedMatrixSettingsBuilder withSize(int width, int height) {
		ledMatrixSettings.setWidth(width);
		ledMatrixSettings.setHeight(height);
		buildLedSettingsList(ledMatrixSettings, width, height);
		return this;
	}
	
	private void buildLedSettingsList(LedMatrixSettings ledMatrixSettings, int width, int height) {
		for (int i = 0; i < width; i ++) {
			for (int j = 0; j < height; j++) {
				ledMatrixSettings.addLedSettings(new LedSettings(LedPosition.ledPositionFor(i, j)));
			}
		}
	}
	
	public LedMatrixSettings build() {
		return ledMatrixSettings;
	}
	
}
