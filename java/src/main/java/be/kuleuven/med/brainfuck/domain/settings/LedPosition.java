package be.kuleuven.med.brainfuck.domain.settings;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LedPosition {

	private int x, y;
	
	public static LedPosition ledPositionFor(int x, int y) {
		return new LedPosition(x, y);
	}
	
	public LedPosition() { }

	public LedPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj != null && getClass() == obj.getClass()) {
			LedPosition other = (LedPosition) obj;
			result = other.x == this.x && other.y == this.y;
		}
		return result;
	}

	@Override
	public String toString() {
		return "LedPosition [x=" + x + ", y=" + y + "]";
	}
	
}
