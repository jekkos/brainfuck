package be.kuleuven.med.brainfuck.core;

import java.util.Map;
import java.util.Set;

import be.kuleuven.med.brainfuck.entity.LedPosition;
import be.kuleuven.med.brainfuck.settings.LedMatrixSettings;
import be.kuleuven.med.brainfuck.settings.LedSettings;

import com.google.common.collect.Sets;

public class LedMatrixGfxModel {

	private Set<LedSettings> selectedLeds = Sets.newHashSet();
	
	private Set<LedSettings> illuminatedLeds = Sets.newHashSet();
	
	private Map<LedPosition, LedSettings> ledSettingsMap;
	
	private LedMatrixSettings ledMatrixSettings;
	
	LedMatrixGfxModel(LedMatrixSettings ledMatrixSettings, Map<LedPosition, LedSettings> ledSettingsMap) {
		this.ledMatrixSettings = ledMatrixSettings;
		this.ledSettingsMap = ledSettingsMap;
	}
	
	public boolean removeLedSettings(LedSettings ledSettings) {
		LedSettings removedLedSettings = ledSettingsMap.remove(ledSettings.getLedPosition());
		boolean result = removedLedSettings != null;
		return result && ledMatrixSettings.removeLedSettings(ledSettings);
	}
	
	public boolean addLedSettings(LedSettings ledSettings) {
		LedSettings addedLedSettings = ledSettingsMap.put(ledSettings.getLedPosition(), ledSettings); 
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
	
	public Map<LedPosition, LedSettings> getLedSettingsMap() {
		return ledSettingsMap;
	}

	public void setLedSettingsMap(Map<LedPosition, LedSettings> ledSettingsMap) {
		this.ledSettingsMap = ledSettingsMap;
	}

	public LedSettings getLedSettings(LedPosition ledPosition) {
		return ledSettingsMap.get(ledPosition);
	}

	public int getWidth() {
		return getLedMatrixSettings().getWidth();
	}
	
	public int getHeight() {
		return getLedMatrixSettings().getHeight();
	}
	
}
