package be.kuleuven.med.brainfuck.core;

import java.util.List;

import org.jdesktop.application.TaskService;

import be.kuleuven.med.brainfuck.io.SerialPortConnector;
import be.kuleuven.med.brainfuck.task.AbstractTask;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.beans.PropertyConnector;

public class LedMatrixController {

	private static final String UPDATE_SERIAL_PORTS_TASK = "updateSerialPorts";

	private static final String INIT_SERIAL_PORT_TASK = "initSerialPort";

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
		PropertyConnector.connectAndUpdate(ledMatrixModelAdapter.getValueModel("width"), Integer.valueOf(ledMatrixView.getWidth()), "text");
		PropertyConnector.connectAndUpdate(ledMatrixModelAdapter.getValueModel("height"), Integer.valueOf(ledMatrixView.getHeight()), "text");
	}
	
	public void updateLedMatrix() {
		// should send data to arduino as well??
		ledMatrixView.drawLedMatrix(ledMatrixModel.getWidth(), ledMatrixModel.getHeight());
	}
	
	public void initializeSerialPort() {
		final String serialPort = ledMatrixModel.getSelectedSerialPortName();
		if (serialPort != null && !"".equals(serialPort)) {
			taskService.execute(new AbstractTask<Void, Void>(INIT_SERIAL_PORT_TASK) {
				
				protected Void doInBackground() throws Exception {
					message("startMessage", serialPort);
					serialPortConnector.close();
					serialPortConnector.initialize(serialPort);
					// should be updating the view on EDT
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

