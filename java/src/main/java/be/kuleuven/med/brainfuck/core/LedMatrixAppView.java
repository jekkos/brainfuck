package be.kuleuven.med.brainfuck.core;

import static be.kuleuven.med.brainfuck.LedMatrixApp.SAVE_SETTINGS_ACTION;
import static be.kuleuven.med.brainfuck.core.LedMatrixAppController.INIT_SERIAL_PORT_ACTION;
import static be.kuleuven.med.brainfuck.core.LedMatrixAppController.UPDATE_LED_MATRIX_ACTION;

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
import javax.swing.text.NumberFormatter;

import net.miginfocom.swing.MigLayout;
import be.kuleuven.med.brainfuck.entity.LedPosition;

import com.jgoodies.common.format.EmptyNumberFormat;

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
		rowTextField = createFormattedTextField();
		rightPanel.add(rowTextField, "w 40, wrap");

		rightPanel.add(new JLabel("Height"));
		columnTextField = createFormattedTextField();
		rightPanel.add(columnTextField, "w 40, wrap");

		JButton updateLedMatrixButton = new JButton("Generate");
		updateLedMatrixButton.addActionListener(actionMap.get(UPDATE_LED_MATRIX_ACTION));
		rightPanel.add(updateLedMatrixButton, "wrap");

		rightPanel.add(new JSeparator(SwingConstants.HORIZONTAL), "wrap");
		rightPanel.add(new JLabel("PIN for column"));
		rowPinTextField = createFormattedTextField();
		rightPanel.add(rowPinTextField, "wrap, w 40, gapy 20");
		rightPanel.add(new JLabel("PIN for row"));
		columnPinTextField = createFormattedTextField();
		rightPanel.add(columnPinTextField, "wrap, w 40");
		rightPanel.add(new JSeparator(SwingConstants.HORIZONTAL), "wrap");
		rightPanel.add(new JButton(actionMap.get(SAVE_SETTINGS_ACTION)));
		
		ledMatrixView = new LedMatrixView();
		ledMatrixView.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent event) {
				LedPosition ledPosition = ledMatrixView.selectLed(event);
				ledMatrixView.repaint();
				ledMatrixController.updateSelectedLed(ledPosition);
			}
			
		});
		
		// add led matrix view 
		leftPanel.add(ledMatrixView, "wmin 300, hmin 300, grow");
		this.add(leftPanel);
		this.add(rightPanel);
	}
	
	private JFormattedTextField createFormattedTextField() {
		NumberFormatter numberFormatter =
	            new NumberFormatter(new EmptyNumberFormat(NumberFormat.getIntegerInstance(), 0));
	        numberFormatter.setValueClass(Integer.class);
	        return new JFormattedTextField(numberFormatter);
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
