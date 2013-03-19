package be.kuleuven.med.brainfuck.settings;

import javax.xml.bind.annotation.XmlRootElement;

import be.kuleuven.med.brainfuck.entity.LedMatrix;

@XmlRootElement
public class LedMatrixSettings {

	private LedMatrix ledMatrix = new LedMatrix(3, 2);

	public LedMatrix getLedMatrix() {
		return ledMatrix;
	}

	public void setLedMatrix(LedMatrix ledMatrix) {
		this.ledMatrix = ledMatrix;
	}
	
}
