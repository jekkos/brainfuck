package be.kuleuven.med.brainfuck.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Lists;

@XmlRootElement
public class LedMatrix {
	
	private int width, height;
	
	private List<Led> leds = Lists.newArrayList();
	
	public LedMatrix() { }

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
	
	public void addLed(Led led) {
		leds.add(led);
	}

	public Led getLed(int x, int y) {
		for (Led led : leds) {
			if (led.getX() == x && led.getY() == y) {
				return led;
			}
		}
		return null;
	}
	
}
