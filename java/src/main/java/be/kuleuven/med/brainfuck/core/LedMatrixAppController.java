package be.kuleuven.med.brainfuck.core;

import static be.kuleuven.med.brainfuck.core.LedMatrixAppModel.ARDUINO_INITIALIZED;
import static be.kuleuven.med.brainfuck.core.LedMatrixAppModel.COLUMN_PIN;
import static be.kuleuven.med.brainfuck.core.LedMatrixAppModel.HEIGHT;
import static be.kuleuven.med.brainfuck.core.LedMatrixAppModel.ROW_PIN;
import static be.kuleuven.med.brainfuck.core.LedMatrixAppModel.WIDTH;

import java.util.List;

import org.jdesktop.application.Action;
import org.jdesktop.application.Task;
import org.jdesktop.application.Task.BlockingScope;

import be.kuleuven.med.brainfuck.bsaf.AppComponent;
import be.kuleuven.med.brainfuck.entity.LedMatrix;
import be.kuleuven.med.brainfuck.entity.LedPosition;
import be.kuleuven.med.brainfuck.io.SerialPortConnector;
import be.kuleuven.med.brainfuck.settings.LedSettings;
import be.kuleuven.med.brainfuck.task.AbstractTask;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.beans.PropertyConnector;
import com.jgoodies.binding.value.ConverterFactory;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.common.base.Strings;

@AppComponent
public class LedMatrixAppController {

	public static final String UPDATE_SERIAL_PORTS_TASK = "updateSerialPorts";

	public static final String INIT_SERIAL_PORT_ACTION = "initializeSerialPort";
	
	public static final String CLOSE_SERIAL_PORT_ACTION = "closeSerialPort";
	
	public static final String UPDATE_LED_MATRIX_ACTION = "updateLedMatrix";
	
	private LedMatrixAppView ledMatrixAppView;
	
	private LedMatrixAppModel ledMatrixAppModel;
	
	private SerialPortConnector serialPortConnector;
	
	private LedMatrix ledMatrix;
	
	public LedMatrixAppController(LedMatrixAppModel ledMatrixAppModel, LedMatrix ledMatrix, SerialPortConnector serialPortConnector) {
		this.ledMatrixAppModel = ledMatrixAppModel;
		this.serialPortConnector = serialPortConnector;
		// setup bindings here
		updateSerialPortNames();
	}
	
	public void initView(LedMatrixAppView ledMatrixAppView) {
		this.ledMatrixAppView = ledMatrixAppView;
		Bindings.bind(ledMatrixAppView.getSerialPortNamesBox(), ledMatrixAppModel.getSerialPortSelectionInList());
		// init width and height
		BeanAdapter<LedMatrixAppModel> ledMatrixModelAdapter = new BeanAdapter<LedMatrixAppModel>(ledMatrixAppModel);
		PropertyConnector.connectAndUpdate(ledMatrixModelAdapter.getValueModel(HEIGHT), ledMatrixAppView.getColumnTextField(), "value");
		PropertyConnector.connectAndUpdate(ledMatrixModelAdapter.getValueModel(WIDTH), ledMatrixAppView.getRowTextField(), "value");
		ValueModel arduinoReleased = ConverterFactory.createBooleanNegator(ledMatrixModelAdapter.getValueModel(ARDUINO_INITIALIZED));
		PropertyConnector.connectAndUpdate(arduinoReleased, ledMatrixAppView.getSerialPortNamesBox(), "enabled");
		// connect pin mappings for row and columns
		// TODO would be cooler if one could bind to the selected led's property directly in this case
		PropertyConnector.connectAndUpdate(ledMatrixModelAdapter.getValueModel(ROW_PIN), ledMatrixAppView.getRowPinTextField(), "value");
		PropertyConnector.connectAndUpdate(ledMatrixModelAdapter.getValueModel(COLUMN_PIN), ledMatrixAppView.getColumnPinTextField(), "value");
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
	public Task<?, ?> initializeSerialPort() {
		final String serialPort = ledMatrixAppModel.getSelectedSerialPortName();
		if (!Strings.isBlank(serialPort) && !ledMatrixAppModel.isArduinoInitialized()) {
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

