package be.kuleuven.med.brainfuck.core;

import java.awt.Canvas;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

public class LedMatrixAppView extends JPanel {

	private static final long serialVersionUID = 1L;

	private final LedMatrixView ledMatrixView;

	private final JTextField columnTextField;

	private final JTextField rowTextField;

	private final JTextField columnPinTextField;

	private final JTextField rowPinTextField;

	private final JComboBox<String> serialPortNamesBox;

	public LedMatrixAppView(final LedMatrixAppController ledMatrixController) {
		JPanel rightPanel = new JPanel(new MigLayout("nogrid", "[right]10", "10"));
		JPanel leftPanel = new JPanel(new MigLayout());
		leftPanel.add(new Canvas());

		serialPortNamesBox = new JComboBox<String>();
		rightPanel.add(serialPortNamesBox);
		JButton updateSerialPortNamesButton = new JButton("Ok");
		updateSerialPortNamesButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				ledMatrixController.updateSerialPortNames();
			}

		});
		rightPanel.add(updateSerialPortNamesButton, "wrap");

		rightPanel.add(new JLabel("Width"));
		rowTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		rightPanel.add(rowTextField, "w 40, wrap");

		rightPanel.add(new JLabel("Height"));
		columnTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		rightPanel.add(columnTextField, "w 40, wrap");

		JButton updateLedMatrixButton = new JButton("Generate");
		updateLedMatrixButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				ledMatrixController.updateLedMatrix();
			}

		});
		rightPanel.add(updateLedMatrixButton, "wrap");

		rightPanel.add(new JSeparator(SwingConstants.HORIZONTAL), "wrap");
		rightPanel.add(new JLabel("PIN for column"));
		rowPinTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		rightPanel.add(rowPinTextField, "wrap, w 40, gapy 50");
		rightPanel.add(new JLabel("PIN for row"));
		columnPinTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		rightPanel.add(columnPinTextField, "wrap, w 40");

		ledMatrixView = new LedMatrixView();
		ledMatrixView.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent event) {
				Shape indices = ledMatrixView.calculateMatrixIndices(event);
				ledMatrixController.updateSelectedLed(indices);
			}
			
		});
		leftPanel.add(ledMatrixView, "wmin 300, hmin 300, grow");
		this.add(leftPanel);
		this.add(rightPanel);
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

	public JTextField getColumnPinTextField() {
		return columnPinTextField;
	}

	public JTextField getRowPinTextField() {
		return rowPinTextField;
	}

	public void drawLedMatrix(int width, int height) {
		ledMatrixView.setMatrixSize(width, height);
	}
	
}
