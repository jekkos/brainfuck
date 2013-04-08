package be.kuleuven.med.brainfuck.core;

import static be.kuleuven.med.brainfuck.LedMatrixApp.SAVE_SETTINGS_ACTION;
import static be.kuleuven.med.brainfuck.core.LedMatrixController.INIT_SERIAL_PORT_ACTION;
import static be.kuleuven.med.brainfuck.core.LedMatrixController.START_EXPERIMENT_ACTION;
import static be.kuleuven.med.brainfuck.core.LedMatrixController.TOGGLE_LED_ACTION;
import static be.kuleuven.med.brainfuck.core.LedMatrixController.UPDATE_LED_MATRIX_ACTION;

import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.application.ResourceMap;

import be.kuleuven.med.brainfuck.settings.LedSettings;

public class LedMatrixPanelView extends JPanel {

	private static final long serialVersionUID = 1L;

	private final JTextField columnTextField;

	private final JTextField rowTextField;

	private final JTextField columnPinTextField;

	private final JTextField rowPinTextField;

	private final JComboBox<String> serialPortNamesBox;

	private JButton updateLedMatrixButton;

	private JSlider intensitySlider;

	private JToggleButton toggleLedButton;
	
	private JToggleButton startExperimentButton;

	private JTextField flickerFrequencyTextField;

	private JTextField secondsToRunTextField;

	public LedMatrixPanelView(final LedMatrixController ledMatrixController) {
		super(new MigLayout("nogrid, push, al right, insets 10"));
		final ActionMap actionMap = ledMatrixController.getApplicationActionMap();
		final ResourceMap resourceMap = ledMatrixController.getResourceMap();
		
		// add serial port controls
		serialPortNamesBox = new JComboBox<String>();
		add(serialPortNamesBox);
		JButton initSerialPortNamesButton = new JButton(actionMap.get(INIT_SERIAL_PORT_ACTION));
		add(initSerialPortNamesButton, "wrap");
		
		// add led matrix controls
		add(new JLabel(resourceMap.getString("widthLabel.text")));
		rowTextField = createFormattedTextField();
		add(rowTextField, "w 40, wrap");
		add(new JLabel(resourceMap.getString("heightLabel.text")));
		columnTextField = createFormattedTextField();
		add(columnTextField, "w 40, wrap");
		updateLedMatrixButton = new JButton(actionMap.get(UPDATE_LED_MATRIX_ACTION));
		add(updateLedMatrixButton, "wrap");
		
		// add led setting fields
		add(new JSeparator(SwingConstants.HORIZONTAL), "wrap");
		add(new JLabel(resourceMap.getString("rowPinLabel.text")));
		rowPinTextField = createFormattedTextField();
		add(rowPinTextField, "wrap, w 40, gapy 10");
		add(new JLabel(resourceMap.getString("columnPinLabel.text")));
		columnPinTextField = createFormattedTextField();
		add(columnPinTextField, "wrap, w 40");
		
		// add led controls
		intensitySlider = new JSlider(LedSettings.MIN_INTENSITY, LedSettings.MAX_INTENSITY, LedSettings.MAX_INTENSITY);
		intensitySlider.setOrientation(JSlider.HORIZONTAL);
		intensitySlider.addChangeListener(new ChangeListener() {
			
			public void stateChanged(ChangeEvent event) {
				ledMatrixController.adjustIntensity(event);
			}
			
		});
		add(intensitySlider, "w 150, wrap");
		toggleLedButton = new JToggleButton(actionMap.get(TOGGLE_LED_ACTION));
		add(toggleLedButton, "wrap");
		
		// add experiment controls
		add(new JSeparator(SwingConstants.HORIZONTAL), "wrap");
		add(new JLabel(resourceMap.getString("flickerFrequencyLabel.text")));
		flickerFrequencyTextField = createFormattedTextField();
		add(flickerFrequencyTextField, "wrap, w 40");
		add(new JLabel(resourceMap.getString("secondsToRunLabel.text")));
		secondsToRunTextField = createFormattedTextField();
		add(secondsToRunTextField, "wrap, w 40");
		startExperimentButton = new JToggleButton(actionMap.get(START_EXPERIMENT_ACTION));
		add(startExperimentButton, "wrap");
		// add save settings button
		add(new JSeparator(SwingConstants.HORIZONTAL), "wrap");
		add(new JButton(actionMap.get(SAVE_SETTINGS_ACTION)));
	}
	
	private JTextField createFormattedTextField() {
		return new JTextField();
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

	public JSlider getIntensitySlider() {
		return intensitySlider;
	}

	public JToggleButton getToggleLedButton() {
		return toggleLedButton;
	}

	public JTextField getFlickerFrequencyTextField() {
		return flickerFrequencyTextField;
	}

	public JTextField getSecondsToRunTextField() {
		return secondsToRunTextField;
	}

	public JToggleButton getStartExperimentButton() {
		return startExperimentButton;
	}
	
}
