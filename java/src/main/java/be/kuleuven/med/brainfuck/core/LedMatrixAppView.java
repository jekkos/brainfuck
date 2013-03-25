package be.kuleuven.med.brainfuck.core;

import static be.kuleuven.med.brainfuck.LedMatrixApp.SAVE_SETTINGS_ACTION;
import static be.kuleuven.med.brainfuck.core.LedMatrixAppController.INIT_SERIAL_PORT_ACTION;
import static be.kuleuven.med.brainfuck.core.LedMatrixAppController.UPDATE_LED_MATRIX_ACTION;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.application.ResourceMap;

import be.kuleuven.med.brainfuck.entity.LedPosition;

public class LedMatrixAppView extends JPanel {

	private static final long serialVersionUID = 1L;

	private final LedMatrixView ledMatrixView;

	private final JTextField columnTextField;

	private final JTextField rowTextField;

	private final JTextField columnPinTextField;

	private final JTextField rowPinTextField;

	private final JComboBox<String> serialPortNamesBox;

	private JButton updateLedMatrixButton;


	public LedMatrixAppView(final LedMatrixAppController ledMatrixController) {
		final ActionMap actionMap = ledMatrixController.getApplicationActionMap();
		final ResourceMap resourceMap = ledMatrixController.getResourceMap();
		
		JPanel rightPanel = new JPanel(new MigLayout("nogrid", "[right]10", "10"));
		JPanel leftPanel = new JPanel(new MigLayout());

		serialPortNamesBox = new JComboBox<String>();
		rightPanel.add(serialPortNamesBox);
		JButton initSerialPortNamesButton = new JButton(actionMap.get(INIT_SERIAL_PORT_ACTION));
		rightPanel.add(initSerialPortNamesButton, "wrap");

		rightPanel.add(new JLabel(resourceMap.getString("widthLabel.text")));
		rowTextField = createFormattedTextField();
		rightPanel.add(rowTextField, "w 40, wrap");

		rightPanel.add(new JLabel(resourceMap.getString("heightLabel.text")));
		columnTextField = createFormattedTextField();
		rightPanel.add(columnTextField, "w 40, wrap");

		updateLedMatrixButton = new JButton(actionMap.get(UPDATE_LED_MATRIX_ACTION));
		rightPanel.add(updateLedMatrixButton, "wrap");

		rightPanel.add(new JSeparator(SwingConstants.HORIZONTAL), "wrap");
		rightPanel.add(new JLabel(resourceMap.getString("rowPinLabel.text")));
		rowPinTextField = createFormattedTextField();
		rightPanel.add(rowPinTextField, "wrap, w 40, gapy 20");
		rightPanel.add(new JLabel(resourceMap.getString("columnPinLabel.text")));
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
	
	private JTextField createFormattedTextField() {
		return new JTextField();
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
