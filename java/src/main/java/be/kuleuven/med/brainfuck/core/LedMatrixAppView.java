package be.kuleuven.med.brainfuck.core;

import static be.kuleuven.med.brainfuck.LedMatrixApp.SAVE_SETTINGS_ACTION;
import static be.kuleuven.med.brainfuck.core.LedMatrixAppController.GENERATE_LED_MATRIX_ACTION;
import static be.kuleuven.med.brainfuck.core.LedMatrixAppController.INIT_SERIAL_PORT_ACTION;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;

import javax.swing.ActionMap;
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
		final ActionMap actionMap = ledMatrixController.getApplicationActionMap();
		JPanel rightPanel = new JPanel(new MigLayout("nogrid", "[right]10", "10"));
		JPanel leftPanel = new JPanel(new MigLayout());

		serialPortNamesBox = new JComboBox<String>();
		rightPanel.add(serialPortNamesBox);
		JButton initSerialPortNamesButton = new JButton("Ok");
		initSerialPortNamesButton.addActionListener(actionMap.get(INIT_SERIAL_PORT_ACTION));
		rightPanel.add(initSerialPortNamesButton, "wrap");

		rightPanel.add(new JLabel("Width"));
		NumberFormat integerInstance = NumberFormat.getIntegerInstance();
		rowTextField = new JFormattedTextField(integerInstance);
		rightPanel.add(rowTextField, "w 40, wrap");

		rightPanel.add(new JLabel("Height"));
		columnTextField = new JFormattedTextField(integerInstance);
		rightPanel.add(columnTextField, "w 40, wrap");

		JButton updateLedMatrixButton = new JButton("Generate");
		updateLedMatrixButton.addActionListener(actionMap.get(GENERATE_LED_MATRIX_ACTION));
		rightPanel.add(updateLedMatrixButton, "wrap");

		rightPanel.add(new JSeparator(SwingConstants.HORIZONTAL), "wrap");
		rightPanel.add(new JLabel("PIN for column"));
		rowPinTextField = new JFormattedTextField(integerInstance);
		rightPanel.add(rowPinTextField, "wrap, w 40, gapy 20");
		rightPanel.add(new JLabel("PIN for row"));
		columnPinTextField = new JFormattedTextField(integerInstance);
		rightPanel.add(columnPinTextField, "wrap, w 40");
		rightPanel.add(new JSeparator(SwingConstants.HORIZONTAL), "wrap");
		rightPanel.add(new JButton(actionMap.get(SAVE_SETTINGS_ACTION)));
		
		ledMatrixView = new LedMatrixView();
		ledMatrixView.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent event) {
				Point index = ledMatrixView.selectLed(event);
				ledMatrixView.repaint();
				ledMatrixController.updateSelectedLed(index);
			}
			
		});
		
		// add led matrix view 
		leftPanel.add(ledMatrixView, "wmin 300, hmin 300, grow");
		this.add(leftPanel);
		this.add(rightPanel);
	}

	public void drawLedMatrix(int width, int height) {
		ledMatrixView.setMatrixSize(width, height);
		ledMatrixView.repaint();
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

	
}
