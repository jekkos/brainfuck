package be.kuleuven.med.brainfuck.settings;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ExperimentSettings {

	public static final int MAX_FLICKER_FREQUENCY = 200;
	
	public static final int MIN_FLICKER_FREQUENCY = 0;

	private int flickerFrequency = MIN_FLICKER_FREQUENCY;
	
	private long secondsToRun;

	public int getFlickerFrequency() {
		return flickerFrequency;
	}

	public void setFlickerFrequency(int flickerFrequency) {
		this.flickerFrequency = flickerFrequency;
	}

	public long getSecondsToRun() {
		return secondsToRun;
	}

	public void setSecondsToRun(long secondsToRun) {
		this.secondsToRun = secondsToRun;
	}
	
}
