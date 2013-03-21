package be.kuleuven.med.brainfuck.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Led {

	private int x;
	
	private int y;
	
	private int rowPin;
	
	private int columnPin;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
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
