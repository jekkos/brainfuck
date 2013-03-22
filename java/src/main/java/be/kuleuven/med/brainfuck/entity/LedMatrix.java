package be.kuleuven.med.brainfuck.entity;

import java.util.Map;

import be.kuleuven.med.brainfuck.settings.LedMatrixSettings;
import be.kuleuven.med.brainfuck.settings.LedSettings;

import com.google.common.collect.Maps;

public class LedMatrix {
	
	private Map<LedPosition, LedSettings> leds;
	
	private LedMatrixSettings ledMatrixSettings;
	
	public LedMatrix() { }

	public LedMatrix(LedMatrixSettings ledMatrixSettings) {
		this.ledMatrixSettings = ledMatrixSettings;
		// popuplate internal data structure
		buildMatrix(ledMatrixSettings);
	}
	
	private Map<LedPosition, LedSettings> buildMatrix(LedMatrixSettings ledMatrixSettings) {
		Map<LedPosition, LedSettings> result = Maps.newHashMap();
		for(LedSettings ledSettings : ledMatrixSettings.getLedSettings()) {
			result.put(ledSettings.getLedPosition(), ledSettings);
		}
		return null;
	}
	
	public void resizeMatrix(int width, int height) {
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

	public LedSettings getLed(LedPosition ledPosition) {
		return leds.get(ledPosition);
	}
	
}
