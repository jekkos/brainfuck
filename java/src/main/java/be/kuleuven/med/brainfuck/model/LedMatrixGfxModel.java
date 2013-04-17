package be.kuleuven.med.brainfuck.model;

import java.util.Map;

import org.jdesktop.application.AbstractBean;

import be.kuleuven.med.brainfuck.domain.settings.LedMatrixSettings;
import be.kuleuven.med.brainfuck.domain.settings.LedPosition;
import be.kuleuven.med.brainfuck.domain.settings.LedSettings;

public class LedMatrixGfxModel extends AbstractBean {
	
	public static final String ILLUMINATED= "illuminated";
	
	public static final String LED_MATRIX_GFX_SELECTION_MODEL = "ledMatrixGfxSelectionModel";

	private Map<LedPosition, LedSettings> ledSettingsMap;
	
	private LedMatrixSettings ledMatrixSettings;
	
	private LedMatrixGfxSelectionModel ledMatrixGfxSelectionModel;
	
	private boolean illuminated = false;
	
	public LedMatrixGfxModel(LedMatrixSettings ledMatrixSettings, Map<LedPosition, LedSettings> ledSettingsMap) {
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
		boolean result = addedLedSettings == null;
		return result && ledMatrixSettings.addLedSettings(ledSettings);
	}
	
	public void setIlluminated(boolean illuminated) {
		firePropertyChange(ILLUMINATED, this.illuminated, this.illuminated = illuminated);
	}
	
	public boolean isIlluminated() {
		return illuminated;
	}
	
	public LedMatrixGfxSelectionModel getLedMatrixGfxSelectionModel() {
		return ledMatrixGfxSelectionModel;
	}

	public void setLedMatrixGfxSelectionModel(
			LedMatrixGfxSelectionModel ledMatrixGfxSelectionModel) {
		firePropertyChange(LED_MATRIX_GFX_SELECTION_MODEL, 
				this.ledMatrixGfxSelectionModel, this.ledMatrixGfxSelectionModel = ledMatrixGfxSelectionModel);
	}
	
	public boolean isSelected(LedSettings ledSettings) {
		return ledMatrixGfxSelectionModel.isSelected(ledSettings);
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
