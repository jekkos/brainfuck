package be.kuleuven.med.brainfuck.core;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class LedMatrixView extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final Canvas ledCanvas;
	
	private JTextField columnTextField;

	private JTextField rowTextField;

	private JComboBox<String> serialPortNamesBox;

	public LedMatrixView(final LedMatrixController ledMatrixController) {
		JPanel rightPanel = new JPanel(new MigLayout("nogrid", "[right]10", "10"));
		JPanel leftPanel = new JPanel(new MigLayout());
		leftPanel.add(new Canvas());
		
		serialPortNamesBox = new JComboBox<String>();
		rightPanel.add(serialPortNamesBox);
		JButton updateSerialPortNamesButton = new JButton("Ok");
		updateSerialPortNamesButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				ledMatrixController.initializeSerialPort();
			}
			
		});
		rightPanel.add(updateSerialPortNamesButton, "wrap");
		
		rightPanel.add(new JLabel("Width"));
		rowTextField = new JFormattedTextField(0);
		rightPanel.add(rowTextField, "w 40, wrap");
		
		rightPanel.add(new JLabel("Height"));
		columnTextField = new JFormattedTextField(0);
		rightPanel.add(columnTextField, "wrap");
		
		JButton updateLedMatrixButton = new JButton("Generate");
		updateLedMatrixButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				ledMatrixController.updateLedMatrix();
			}
			
		});
		rightPanel.add(updateLedMatrixButton, "wrap");
		
		rightPanel.add(new JSeparator());
		rightPanel.add(new JLabel("PIN for column"));
		rightPanel.add(rowTextField, "wrap, w 40, gapy 50");
		rightPanel.add(new JLabel("PIN for row"));
		rightPanel.add(rowTextField, "wrap, w 40");
		
		ledCanvas = new Canvas();
		ledCanvas.setBackground(Color.WHITE);
		leftPanel.add(ledCanvas, "wmin 300, hmin 300, grow");
		this.add(leftPanel);
		this.add(rightPanel);
	}
	
	/**
	 * Draw the led matrix.. will be called on the EDT
	 * @param ledMatrix
	 */
	public void drawLedMatrix(int width, int height) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Graphics graphics = ledCanvas.getGraphics();
				graphics.setColor(Color.WHITE);
				graphics.fillRect(0,  0, ledCanvas.getWidth(), ledCanvas.getHeight());
				graphics.setColor(Color.BLACK);
				graphics.drawArc(10 + 20 * i, 10 + 20 * j, 15, 15, 0, 360);
			}
		}
	}
	
/*	public void updateSerialPortNames(List<String> serialPortNames) {
		DefaultComboBoxModel<String> defaultModel = new DefaultComboBoxModel<String>();
		for(String serialPortName : serialPortNames) {
			defaultModel.addElement(serialPortName);
	}
	*/
	public Canvas getLedCanvas() {
		return ledCanvas;
	}

	public JTextField getColumnTextField() {
		return columnTextField;
	}

	public JTextField getRowTextField() {
		return rowTextField;
	}
	
	public JComboBox<String> getSerialPortNamesBox() {
		return serialPortNamesBox;
	}

	/*@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (LedMatrixModel.SERIAL_PORT_NAMES.equals(evt.getPropertyName())) {
			@SuppressWarnings("unchecked")
			List<String> newValue = (List<String>) evt.getNewValue();
			updateSerialPortNames(newValue);
		}
	}*/
	
}
