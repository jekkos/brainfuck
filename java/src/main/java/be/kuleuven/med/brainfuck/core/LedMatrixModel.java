package be.kuleuven.med.brainfuck.core;

import java.util.List;

import org.jdesktop.application.AbstractBean;

import be.kuleuven.med.brainfuck.entity.LedMatrix;
import be.kuleuven.med.brainfuck.settings.LedMatrixSettings;

import com.jgoodies.binding.list.SelectionInList;

public class LedMatrixModel extends AbstractBean {
	
	public static final String HEIGHT = "height";

	public static final String WIDTH = "width";

	private LedMatrix ledMatrix;

	private SelectionInList<String> serialPortNameSelectionInList;
	
	public LedMatrixModel(LedMatrixSettings ledMatrixSettings) {
		this.ledMatrix = ledMatrixSettings.getLedMatrix();
		serialPortNameSelectionInList = new SelectionInList<String>();
	}
	
    public int getWidth() {
		return ledMatrix.getWidth();
	}

	public void setWidth(int width) {
		firePropertyChange(WIDTH, ledMatrix.getWidth(), width);
		ledMatrix.setWidth(width);
	}

	public int getHeight() {
		return ledMatrix.getHeight();
	}
	
	public void setHeight(int height) {
		firePropertyChange(HEIGHT, ledMatrix.getHeight(), height);
		ledMatrix.setHeight(height);
	}
	
	public SelectionInList<String> getSerialPortSelectionInList() {
		return serialPortNameSelectionInList;
	}

 	public List<String> getSerialPortNames() {
		return this.serialPortNameSelectionInList.getList();
	}

	public void setSerialPortNames(List<String> serialPortNames) {
		this.serialPortNameSelectionInList.setList(serialPortNames);
	}
	
	public String getSelectedSerialPortName() {
		return serialPortNameSelectionInList.getSelection();
	}

	public void setSelectedSerialPortName(String selectedSerialPortName) {
		serialPortNameSelectionInList.setSelection(selectedSerialPortName);
	}

}
