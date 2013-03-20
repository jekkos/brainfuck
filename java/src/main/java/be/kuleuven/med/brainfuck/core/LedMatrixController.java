package be.kuleuven.med.brainfuck.core;

import java.util.List;

import org.jdesktop.application.TaskService;

import be.kuleuven.med.brainfuck.io.SerialPortConnector;
import be.kuleuven.med.brainfuck.task.AbstractTask;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.beans.PropertyConnector;
import com.jgoodies.binding.value.ConverterFactory;
import com.jgoodies.binding.value.ValueModel;

public class LedMatrixController {

	private static final String UPDATE_SERIAL_PORTS_TASK = "updateSerialPorts";

	private static final String INIT_SERIAL_PORT_TASK = "initSerialPort";
	
	private static final String CLOSE_SERIAL_PORT_TASK = "closeSerialPort";
	
	private LedMatrixView ledMatrixView;
	
	private LedMatrixModel ledMatrixModel;
	
	private SerialPortConnector serialPortConnector;
	
	private TaskService taskService;
	
	public LedMatrixController(TaskService taskService, LedMatrixModel ledMatrixModel, SerialPortConnector serialPortConnector) {
		this.ledMatrixModel = ledMatrixModel;
		this.taskService = taskService;
		this.serialPortConnector = serialPortConnector;
		// setup bindings here
		updateSerialPortNames();
	}
	
	public void initView(LedMatrixView ledMatrixView) {
		this.ledMatrixView = ledMatrixView;
		Bindings.bind(ledMatrixView.getSerialPortNamesBox(), ledMatrixModel.getSerialPortSelectionInList());
		// init width and height
		BeanAdapter<LedMatrixModel> ledMatrixModelAdapter = new BeanAdapter<LedMatrixModel>(ledMatrixModel);
		PropertyConnector.connectAndUpdate(ledMatrixModelAdapter.getValueModel("width"), ledMatrixView.getRowTextField(), "value");
		PropertyConnector.connectAndUpdate(ledMatrixModelAdapter.getValueModel("height"), ledMatrixView.getColumnTextField(), "value");
		ValueModel arduinoReleased = ConverterFactory.createBooleanNegator(ledMatrixModelAdapter.getValueModel("arduinoInitialized"));
		PropertyConnector.connectAndUpdate(arduinoReleased, ledMatrixView.getSerialPortNamesBox(), "enabled");
	}
	
	public void updateLedMatrix() {
		// should send data to arduino as well??
		ledMatrixView.drawLedMatrix(ledMatrixModel.getWidth(), ledMatrixModel.getHeight());
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

