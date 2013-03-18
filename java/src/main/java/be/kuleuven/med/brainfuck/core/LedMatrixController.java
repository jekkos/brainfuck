package be.kuleuven.med.brainfuck.core;

import java.util.List;

import org.jdesktop.application.TaskService;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.beans.BeanAdapter;

import be.kuleuven.med.brainfuck.io.SerialPortConnector;
import be.kuleuven.med.brainfuck.task.AbstractTask;
import be.kuleuven.med.brainfuck.view.LedMatrixView;

public class LedMatrixController {

	private static final String UPDATE_SERIAL_PORTS_TASK = "updateSerialPorts";

	private static final String INIT_SERIAL_PORT_TASK = "initSerialPort";

	private LedMatrixView ledMatrixView;
	
	private LedMatrixModel ledMatrixModel;
	
	private SerialPortConnector serialConnector;
	
	private TaskService taskService;
	
	public LedMatrixController(TaskService taskService, LedMatrixModel ledMatrixModel) {
		this.ledMatrixModel = ledMatrixModel;
		this.taskService = taskService;
		// setup bindings here
		initializeSerialPort(ledMatrixModel.getSelectedSerialPortName());
	}
	
	public void initView(LedMatrixView ledMatrixView) {
		this.ledMatrixView = ledMatrixView;
		BeanAdapter<LedMatrixModel> ledMatrixModelAdapter = new BeanAdapter<LedMatrixModel>(ledMatrixModel);
		//Bindings.bind(ledMatrixView.getSerialPortNamesBox(), "", ledMatrixModelAdapter.getValueModel("serialPortNames"));
		Bindings.bind(ledMatrixView.getRowTextField(), ledMatrixModelAdapter.getValueModel("width"));
		Bindings.bind(ledMatrixView.getColumnTextField(), ledMatrixModelAdapter.getValueModel("height"));
		// setup bindings with model here
	}
	
	public void updateLedMatrix() {
		// should send data to arduino as well??
		ledMatrixView.drawLedMatrix(ledMatrixModel.getWidth(), ledMatrixModel.getHeight());
	}
	
	public void initializeSerialPort(final String serialPort) {
		if (serialPort != null && !"".equals(serialPort)) {
			taskService.execute(new AbstractTask<Void, Void>(INIT_SERIAL_PORT_TASK) {
				
				protected Void doInBackground() throws Exception {
					message("startMessage", serialPort);
					serialConnector.close();
					serialConnector.initialize(serialPort);
					// should be updating the view on EDT
					ledMatrixModel.setSelectedSerialPortName(serialPort);
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
				List<String> result = serialConnector.getSerialPortNames();
				// should be updating the view on EDT
				ledMatrixModel.setSerialPortNames(result);
				message("endMessage");
				return null;
			}
			
		});
	}
	
}

