package be.kuleuven.med.brainfuck.domain.setting;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Lists;

@XmlRootElement
public class LedMatrixSettings {

	public static final int MAX_PORT_NUMBER = 13;
	
	@XmlElement(name="ledSettings")
	private List<LedSettings> ledSettingsList = Lists.newArrayList();
	
	private int width, height;
	
	private int maxPortNumber = MAX_PORT_NUMBER;
	
	public LedMatrixSettings() {	}

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

	public int getMaxPortNumber() {
		return maxPortNumber;
	}

	public void setMaxPortNumber(int maxPortNumber) {
		this.maxPortNumber = maxPortNumber;
	}
	
}
