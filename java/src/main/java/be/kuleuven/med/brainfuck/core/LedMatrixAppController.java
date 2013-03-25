package be.kuleuven.med.brainfuck.core;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import org.jdesktop.application.Action;
import org.jdesktop.application.Task;
import org.jdesktop.application.Task.BlockingScope;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.swingbinding.SwingBindings;

import be.kuleuven.med.brainfuck.bsaf.AppComponent;
import be.kuleuven.med.brainfuck.entity.LedMatrix;
import be.kuleuven.med.brainfuck.entity.LedPosition;
import be.kuleuven.med.brainfuck.io.SerialPortConnector;
import be.kuleuven.med.brainfuck.settings.LedSettings;
import be.kuleuven.med.brainfuck.task.AbstractTask;

@AppComponent
public class LedMatrixAppController {

	public static final String UPDATE_SERIAL_PORTS_TASK = "updateSerialPorts";

	public static final String INIT_SERIAL_PORT_ACTION = "initializeSerialPort";

	public static final String CLOSE_SERIAL_PORT_ACTION = "closeSerialPort";

	public static final String UPDATE_LED_MATRIX_ACTION = "updateLedMatrix";

	private final static BeanProperty<JComponent, Boolean> ENABLED = BeanProperty.create("enabled");
	
	private final static BeanProperty<JComponent, String> TEXT = BeanProperty.create("text");
	
	private LedMatrixAppView ledMatrixAppView;

	private LedMatrixAppModel ledMatrixAppModel;

	private SerialPortConnector serialPortConnector;

	private LedMatrix ledMatrix;

	public LedMatrixAppController(LedMatrixAppModel ledMatrixAppModel, LedMatrix ledMatrix, SerialPortConnector serialPortConnector) {
		this.ledMatrixAppModel = ledMatrixAppModel;
		this.ledMatrix = ledMatrix;
		this.serialPortConnector = serialPortConnector;
		// setup bindings here
		updateSerialPortNames();
	}

	public void initView(LedMatrixAppView ledMatrixAppView) {
		this.ledMatrixAppView = ledMatrixAppView;
		
		BindingGroup bindingGroup = new BindingGroup();
		Binding<LedMatrixAppModel, Boolean, ? extends JComponent, Boolean> enabledBinding = null;
		Binding<LedMatrixAppModel, Integer, ? extends JComponent, String> valueBinding = null;
		// bind width and height matrix properties
		BeanProperty<LedMatrixAppModel, Integer> widthProperty = BeanProperty.create("width");
		valueBinding = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, ledMatrixAppModel, widthProperty, ledMatrixAppView.getRowTextField(), TEXT);
		bindingGroup.addBinding(valueBinding);
		BeanProperty<LedMatrixAppModel, Integer> heightProperty = BeanProperty.create("height");
		valueBinding = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, ledMatrixAppModel, heightProperty, ledMatrixAppView.getColumnTextField(), TEXT);
		bindingGroup.addBinding(valueBinding);
		// bind row and column pin numbers
		ELProperty<LedMatrixAppModel, Boolean> itemSelectedProperty = ELProperty.create("${selectedLedSettings != null}");
		BeanProperty<LedMatrixAppModel, Integer> pinRowProperty = BeanProperty.create("selectedLedSettings.rowPin");
		valueBinding = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, ledMatrixAppModel, pinRowProperty, ledMatrixAppView.getRowPinTextField(), TEXT);
		valueBinding.setTargetNullValue(0);
		bindingGroup.addBinding(valueBinding);
		enabledBinding = Bindings.createAutoBinding(UpdateStrategy.READ, ledMatrixAppModel, itemSelectedProperty, ledMatrixAppView.getRowPinTextField(), ENABLED);
		bindingGroup.addBinding(enabledBinding);
		BeanProperty<LedMatrixAppModel, Integer> pinColumnProperty = BeanProperty.create("selectedLedSettings.columnPin");
		valueBinding = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, ledMatrixAppModel, pinColumnProperty, ledMatrixAppView.getColumnPinTextField(), TEXT);
		valueBinding.setTargetNullValue(0);
		bindingGroup.addBinding(valueBinding);
		enabledBinding = Bindings.createAutoBinding(UpdateStrategy.READ, ledMatrixAppModel, itemSelectedProperty, ledMatrixAppView.getColumnPinTextField(), ENABLED);
		bindingGroup.addBinding(enabledBinding);
		// bind serial port info
		BeanProperty<LedMatrixAppModel, List<String>> serialPortNamesProperty = BeanProperty.create("serialPortNames");
		SwingBindings.createJComboBoxBinding(UpdateStrategy.READ_WRITE, ledMatrixAppModel, serialPortNamesProperty, ledMatrixAppView.getSerialPortNamesBox());
		BeanProperty<JComboBox<?>, String> selectedElementProperty = BeanProperty.create("selectedElement");
		BeanProperty<LedMatrixAppModel, String> selectedSerialPortNameProperty = BeanProperty.create("selectedSerialPortName");
		Binding<LedMatrixAppModel, String, JComboBox<?>, String> selectedElementBinding = 
				Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, ledMatrixAppModel, selectedSerialPortNameProperty, ledMatrixAppView.getSerialPortNamesBox(), selectedElementProperty);
		bindingGroup.addBinding(selectedElementBinding);
		// bind serial port select box enabled state
		ELProperty<LedMatrixAppModel, Boolean> arduinoInitialized = ELProperty.create("${!arduinoInitialized}");
		enabledBinding = Bindings.createAutoBinding(UpdateStrategy.READ, ledMatrixAppModel, arduinoInitialized, ledMatrixAppView.getSerialPortNamesBox(), ENABLED);
		bindingGroup.addBinding(enabledBinding);
		bindingGroup.bind();
	}

	@Action
	public void updateLedMatrix() {
		// should regenerate led matrix??
		ledMatrix.resizeMatrix(ledMatrixAppModel.getWidth(), ledMatrixAppModel.getHeight());
		// should send data to arduino as well??
		ledMatrixAppView.drawLedMatrix(ledMatrix.getWidth(), ledMatrix.getHeight());
	}

	public void updateSelectedLed(LedPosition ledPosition) {
		if (ledPosition != null) {
			LedSettings ledSettings = ledMatrix.getLedSettings(ledPosition);
			ledMatrixAppModel.setSelectedLedSettings(ledSettings);
			// set selected led based on passed coordinates
			// so all subsequent input can be bound to this led..
		}
	}

	@Action(block=BlockingScope.APPLICATION)
	public Task<?, ?> initializeSerialPort(ActionEvent event) {
		JButton source = (JButton) event.getSource();
		boolean isSelected = source.isSelected();
		StringBuilder stringBuilder = new StringBuilder(INIT_SERIAL_PORT_ACTION + ".Action.");
		stringBuilder.append(isSelected ? "text" : "selectedText");
		source.setText(getResourceMap().getString(stringBuilder.toString()));
		final String serialPort = ledMatrixAppModel.getSelectedSerialPortName();
		if (serialPort != null && !"".equals(serialPort) && !ledMatrixAppModel.isArduinoInitialized()) {
			return new AbstractTask<Void, Void>(INIT_SERIAL_PORT_ACTION) {

				protected Void doInBackground() throws Exception {
					message("startMessage", serialPort);
					serialPortConnector.initialize(serialPort);
					// will disable enabled state of in the gui..
					ledMatrixAppModel.setArduinoInitialized(true);
					// should be updating the view on EDT
					message("endMessage");
					return null;
				}

			};
		} else {
			return new AbstractTask<Void, Void>(CLOSE_SERIAL_PORT_ACTION) {

				protected Void doInBackground() throws Exception {
					message("startMessage", serialPort);
					serialPortConnector.close();
					ledMatrixAppModel.setArduinoInitialized(false);
					message("endMessage");
					return null;
				}

			};
		}
	}

	@Action(block=BlockingScope.APPLICATION)
	public Task<?, ?> updateSerialPortNames() {
		return new AbstractTask<Void, Void>(UPDATE_SERIAL_PORTS_TASK) {

			protected Void doInBackground() throws Exception {
				message("startMessage");
				List<String> serialPortNames = serialPortConnector.getSerialPortNames();
				String selectedSerialPortName = serialPortConnector.getSelectedSerialPortName();
				// should be updating the view on EDT
				ledMatrixAppModel.setSerialPortNames(serialPortNames);
				ledMatrixAppModel.setSelectedSerialPortName(selectedSerialPortName);
				message("endMessage");
				return null;
			}

		};
	}

}

