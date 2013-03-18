package be.kuleuven.med.brainfuck.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LedMatrix {
	
	private int width, height;

	public LedMatrix(int i, int j) {
		this.width = i;
		this.height = j;
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
