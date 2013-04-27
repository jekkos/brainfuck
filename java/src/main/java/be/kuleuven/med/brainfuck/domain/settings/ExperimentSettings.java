package be.kuleuven.med.brainfuck.domain.settings;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ExperimentSettings {

	public static final int MIN_CYCLES_TO_RUN = 1;

	private int cyclesToRun = MIN_CYCLES_TO_RUN;

	public int getCyclesToRun() {
		return cyclesToRun;
	}

	public void setCyclesToRun(int cyclesToRun) {
		this.cyclesToRun = cyclesToRun;
	}

}
