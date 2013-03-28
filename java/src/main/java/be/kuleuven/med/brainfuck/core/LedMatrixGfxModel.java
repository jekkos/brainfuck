package be.kuleuven.med.brainfuck.core;

import java.awt.Shape;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.kuleuven.med.brainfuck.entity.LedPosition;
import be.kuleuven.med.brainfuck.settings.LedMatrixSettings;
import be.kuleuven.med.brainfuck.settings.LedSettings;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class LedMatrixGfxModel {

	private Set<LedSettings> selectedLeds = Sets.newHashSet();
	
	private Set<LedSettings> illuminatedLeds = Sets.newHashSet();
	
	private List<Shape> shapes = Lists.newArrayList();
	
	private Map<LedPosition, LedSettings> leds;
	
	private LedMatrixSettings ledMatrixSettings;
	
	LedMatrixGfxModel(LedMatrixSettings ledMatrixSettings, Map<LedPosition, LedSettings> leds) {
		this.ledMatrixSettings = ledMatrixSettings;
		this.leds = leds;
	}
	
	public boolean removeLedSettings(LedSettings ledSettings) {
		LedSettings removedLedSettings = leds.remove(ledSettings.getLedPosition());
		boolean result = removedLedSettings != null;
		return result && ledMatrixSettings.removeLedSettings(ledSettings);
	}
	
	public boolean addLedSettings(LedSettings ledSettings) {
		LedSettings addedLedSettings = leds.put(ledSettings.getLedPosition(), ledSettings); 
		boolean result = addedLedSettings != null;
		return result && ledMatrixSettings.addLedSettings(ledSettings);
	}
	
	public void setIlluminated(LedSettings ledSettings, boolean illuminated) {
		if (illuminated) {
			illuminatedLeds.add(ledSettings);
		} else {
			illuminatedLeds.remove(ledSettings);
		}
	}
	
	public void setSelected(LedSettings ledSettings, boolean selected) {
		if (selected) {
			selectedLeds.add(ledSettings);
		} else {
			selectedLeds.remove(ledSettings);
		}
	}
	
	public List<Shape> getShapes() {
		return shapes;
	}

	public void setShapes(List<Shape> shapes) {
		this.shapes = shapes;
	}

	public void clearSelected() {
		selectedLeds.clear();
	}
	
	public void clear() {
		selectedLeds.clear();
		illuminatedLeds.clear();
	}

	public boolean isIlluminated(LedSettings ledSettings) {
		return illuminatedLeds.contains(ledSettings);
	}
	
	public boolean isSelected(LedSettings ledSettings) {
		return selectedLeds.contains(ledSettings);
	}
	
	public LedMatrixSettings getLedMatrixSettings() {
		return ledMatrixSettings;
	}

	public void setLedMatrixSettings(LedMatrixSettings ledMatrixSettings) {
		this.ledMatrixSettings = ledMatrixSettings;
	}
	
	public LedSettings getLedSettings(LedPosition ledPosition) {
		return leds.get(ledPosition);
	}

	public int getWidth() {
		return getLedMatrixSettings().getWidth();
	}
	
	public int getHeight() {
		return getLedMatrixSettings().getHeight();
	}
	
}
