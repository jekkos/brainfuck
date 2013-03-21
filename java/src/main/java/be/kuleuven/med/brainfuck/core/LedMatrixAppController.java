package be.kuleuven.med.brainfuck.core;

import static be.kuleuven.med.brainfuck.core.LedMatrixAppModel.COLUMN_PIN;
import static be.kuleuven.med.brainfuck.core.LedMatrixAppModel.HEIGHT;
import static be.kuleuven.med.brainfuck.core.LedMatrixAppModel.ROW_PIN;
import static be.kuleuven.med.brainfuck.core.LedMatrixAppModel.WIDTH;

import java.awt.Shape;
import java.util.List;

import org.jdesktop.application.TaskService;

import be.kuleuven.med.brainfuck.io.SerialPortConnector;
import be.kuleuven.med.brainfuck.task.AbstractTask;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.beans.PropertyConnector;
import com.jgoodies.binding.value.ConverterFactory;
import com.jgoodies.binding.value.ValueModel;

public class LedMatrixAppController {

	private static final String UPDATE_SERIAL_PORTS_TASK = "updateSerialPorts";

	private static final String INIT_SERIAL_PORT_TASK = "initSerialPort";
	
	private static final String CLOSE_SERIAL_PORT_TASK = "closeSerialPort";
	
	private LedMatrixAppView ledMatrixView;
	
	private LedMatrixAppModel ledMatrixModel;
	
	private SerialPortConnector serialPortConnector;
	
	private TaskService taskService;
	
	public LedMatrixAppController(TaskService taskService, LedMatrixAppModel ledMatrixModel, SerialPortConnector serialPortConnector) {
		this.ledMatrixModel = ledMatrixModel;
		this.taskService = taskService;
		this.serialPortConnector = serialPortConnector;
		// setup bindings here
		updateSerialPortNames();
	}
	
	public void initView(LedMatrixAppView ledMatrixView) {
		this.ledMatrixView = ledMatrixView;
		Bindings.bind(ledMatrixView.getSerialPortNamesBox(), ledMatrixModel.getSerialPortSelectionInList());
		// init width and height
		BeanAdapter<LedMatrixAppModel> ledMatrixModelAdapter = new BeanAdapter<LedMatrixAppModel>(ledMatrixModel);
		PropertyConnector.connectAndUpdate(ledMatrixModelAdapter.getValueModel(HEIGHT), ledMatrixView.getRowTextField(), "value");
		PropertyConnector.connectAndUpdate(ledMatrixModelAdapter.getValueModel(WIDTH), ledMatrixView.getColumnTextField(), "value");
		ValueModel arduinoReleased = ConverterFactory.createBooleanNegator(ledMatrixModelAdapter.getValueModel("arduinoInitialized"));
		PropertyConnector.connectAndUpdate(arduinoReleased, ledMatrixView.getSerialPortNamesBox(), "enabled");
		// connect pin mappings for row and columns
		PropertyConnector.connectAndUpdate(ledMatrixModelAdapter.getValueModel(ROW_PIN), ledMatrixView.getRowPinTextField(), "value");
		PropertyConnector.connectAndUpdate(ledMatrixModelAdapter.getValueModel(COLUMN_PIN), ledMatrixView.getColumnPinTextField(), "value");
	}
	
	public void updateLedMatrix() {
		// should send data to arduino as well??
		ledMatrixView.drawLedMatrix(ledMatrixModel.getWidth(), ledMatrixModel.getHeight());
	}
	
	public void updateSelectedLed(Shape indices) {
		if (indices != null) {
			// set selected led based on passed coordinates
			// so all subsequent input can be bound to this led..
		}
	}
	
	public void initializeSerialPort() {
		final String serialPort = ledMatrixModel.getSelectedSerialPortName();
		if (serialPort != null && !"".equals(serialPort) && !ledMatrixModel.isArduinoInitialized()) {
			taskService.execute(new AbstractTask<Void, Void>(INIT_SERIAL_PORT_TASK) {
				
				protected Void doInBackground() throws Exception {
					message("startMessage", serialPort);
					serialPortConnector.initialize(serialPort);
					// will disable enabled state of in the gui..
					ledMatrixModel.setArduinoInitialized(true);
					// should be updating the view on EDT
					message("endMessage");
					return null;
				}
				
			});
		} else {
			taskService.execute(new AbstractTask<Void, Void>(CLOSE_SERIAL_PORT_TASK) {

				protected Void doInBackground() throws Exception {
					message("startMessage", serialPort);
					serialPortConnector.close();
					ledMatrixModel.setArduinoInitialized(false);
					message("endMessage");
					return null;
				}
				
			});
		}
	}
	
	public void updateSerialPortNames() {
		taskService.execute(new AbstractTask<Void, Void>(UPDATE_SERIAL_PORTS_TASK) {

			protected Void doInBackground() throws Exception {
				message("startMessage");
				List<String> serialPortNames = serialPortConnector.getSerialPortNames();
				String selectedSerialPortName = serialPortConnector.getSelectedSerialPortName();
				// should be updating the view on EDT
				ledMatrixModel.setSerialPortNames(serialPortNames);
				ledMatrixModel.setSelectedSerialPortName(selectedSerialPortName);
				message("endMessage");
				return null;
			}
			
		});
	}

}

