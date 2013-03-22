package be.kuleuven.med.brainfuck.settings;

import be.kuleuven.med.brainfuck.entity.LedPosition;

public class LedSettings {

	private LedPosition ledPosition;
	
	private int rowPin;
	
	private int columnPin;
	
	public int getxPosition() {
		return ledPosition.getX();
	}

	public LedPosition getLedPosition() {
		return ledPosition;
	}

	public void setyPosition(int yPosition) {
		ledPosition.setY(yPosition);
	}

	public int getRowPin() {
		return rowPin;
	}

	public void setRowPin(int rowPin) {
		this.rowPin = rowPin;
	}

	public int getColumnPin() {
		return columnPin;
	}

	public void setColumnPin(int columnPin) {
		this.columnPin = columnPin;
	}
	
}
