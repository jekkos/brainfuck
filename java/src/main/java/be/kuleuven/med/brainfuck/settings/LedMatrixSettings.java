package be.kuleuven.med.brainfuck.settings;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LedMatrixSettings {

	private List<LedSettings> ledSettings;
	
	int width, height;

	public List<LedSettings> getLedSettings() {
		return Collections.unmodifiableList(ledSettings);
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
	
}
