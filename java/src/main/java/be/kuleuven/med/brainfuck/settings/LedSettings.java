package be.kuleuven.med.brainfuck.settings;

import be.kuleuven.med.brainfuck.entity.LedPosition;

public class LedSettings {

	private LedPosition ledPosition;
	
	private int rowPin;
	
	private int columnPin;
	
	public LedSettings(LedPosition ledPosition) {
		this.ledPosition = ledPosition;
	}

	public int getX() {
		return ledPosition.getX();
	}
	
	public void setX(int x) {
		ledPosition.setX(x);
	}

	public LedPosition getLedPosition() {
		return ledPosition;
	}

	public void setY(int yPosition) {
		ledPosition.setY(yPosition);
	}

	public int getY() {
		return ledPosition.getY();
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
