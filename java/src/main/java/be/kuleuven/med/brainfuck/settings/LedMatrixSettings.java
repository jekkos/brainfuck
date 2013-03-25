package be.kuleuven.med.brainfuck.settings;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Lists;

@XmlRootElement
public class LedMatrixSettings {

	@XmlElement(name="ledSettings")
	private List<LedSettings> ledSettingsList = Lists.newArrayList();
	
	private int width, height;
	
	LedMatrixSettings() {	}

	public List<LedSettings> getLedSettingsList() {
		return Collections.unmodifiableList(ledSettingsList);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public boolean addLedSettings(LedSettings ledSettings) {
		return ledSettingsList.add(ledSettings);
	}
	
	public boolean removeLedSettings(LedSettings ledSettings) {
		return ledSettingsList.remove(ledSettings);
	}
	
}
