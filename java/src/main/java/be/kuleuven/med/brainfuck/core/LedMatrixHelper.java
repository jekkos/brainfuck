package be.kuleuven.med.brainfuck.core;

import java.util.Map;

import be.kuleuven.med.brainfuck.entity.LedPosition;
import be.kuleuven.med.brainfuck.settings.LedMatrixSettings;
import be.kuleuven.med.brainfuck.settings.LedSettings;

import com.google.common.collect.Maps;

public class LedMatrixHelper {
	
	private Map<LedPosition, LedSettings> leds;
	
	private LedMatrixSettings ledMatrixSettings;
	
	public LedMatrixHelper() { }

	public LedMatrixHelper(LedMatrixSettings ledMatrixSettings) {
		this.ledMatrixSettings = ledMatrixSettings;
		// popuplate internal data structure
		leds = buildMatrix(ledMatrixSettings);
	}
	
	private Map<LedPosition, LedSettings> buildMatrix(LedMatrixSettings ledMatrixSettings) {
		Map<LedPosition, LedSettings> result = Maps.newHashMap();
		for(LedSettings ledSettings : ledMatrixSettings.getLedSettingsList()) {
			result.put(ledSettings.getLedPosition(), ledSettings);
		}
		return result;
	}
	
	private void removeLedSettings(int width, int height) {
		int oldWidth = ledMatrixSettings.getWidth();
		int oldHeight = ledMatrixSettings.getHeight();
		for (int i = 0; i < oldWidth; i++) {
			boolean removeRow = i >= width;
			for (int j = 0; j < oldHeight; j++) {
				boolean removeColumn = j >= height;
				LedPosition ledPosition = LedPosition.ledPositionFor(i, j);
				LedSettings ledSettings = leds.get(ledPosition);
				if (ledSettings != null && (removeRow || removeColumn)) {
					leds.remove(ledPosition);
					ledMatrixSettings.removeLedSettings(ledSettings);
				}
			}
		}
	}
	
	private void addLedSettings(int width, int height) {
		// go throguh all available settings and create new entries
		for (int i = 0; i < width; i ++) {
			for (int j = 0; j < height; j ++) {
				LedPosition ledPosition = LedPosition.ledPositionFor(i, j);
				if (leds.get(ledPosition) == null) {
					LedSettings ledSettings = new LedSettings(ledPosition);
					leds.put(ledPosition, ledSettings);
					ledMatrixSettings.addLedSettings(ledSettings);
				}
			}
		}
	}
	
	public void resizeMatrix(int width, int height) {
		removeLedSettings(width, height);
		addLedSettings(width, height);
		// add or remove nodes..
		ledMatrixSettings.setWidth(width);
		ledMatrixSettings.setHeight(height);
	}
	
	public int getWidth() {
		return ledMatrixSettings.getWidth();
	}

	public int getHeight() {
		return ledMatrixSettings.getHeight();
	}

	public void addLed(LedPosition ledPosition, LedSettings led) {
		leds.put(ledPosition, led);
	}

	public LedSettings getLedSettings(LedPosition ledPosition) {
		return leds.get(ledPosition);
	}
	
}
