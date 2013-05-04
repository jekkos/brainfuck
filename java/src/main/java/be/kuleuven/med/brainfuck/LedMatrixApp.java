package be.kuleuven.med.brainfuck;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.util.EventObject;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.ActionMap;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.Task;
import org.jdesktop.application.Task.BlockingScope;
import org.jdesktop.application.TaskService;
import org.jdesktop.application.utils.AppHelper;

import be.kuleuven.med.brainfuck.connector.LedMatrixConnector;
import be.kuleuven.med.brainfuck.connector.SerialPortConnector;
import be.kuleuven.med.brainfuck.connector.ThorlabsConnector;
import be.kuleuven.med.brainfuck.connector.ThorlabsDC2100Connector;
import be.kuleuven.med.brainfuck.controller.LedMatrixController;
import be.kuleuven.med.brainfuck.controller.SettingsManager;
import be.kuleuven.med.brainfuck.domain.settings.ExperimentSettings;
import be.kuleuven.med.brainfuck.domain.settings.LedMatrixSettings;
import be.kuleuven.med.brainfuck.model.LedMatrixGfxModel;
import be.kuleuven.med.brainfuck.model.LedMatrixPanelModel;
import be.kuleuven.med.brainfuck.modelbuilder.LedMatrixGfxModelBuilder;
import be.kuleuven.med.brainfuck.task.AbstractTask;
import be.kuleuven.med.brainfuck.view.LedMatrixGfxView;
import be.kuleuven.med.brainfuck.view.LedMatrixPanelView;

import com.google.common.collect.Lists;

public class LedMatrixApp extends SingleFrameApplication {

	public static final String SAVE_SETTINGS_ACTION = "saveSettings";

	public static final String DISPOSE_SERIAL_PORT_CONNECTORS_ACTION = "disposeSerialPortConnectors";

	public static final String APP_VERSION = "Application.version";
	public static final String APP_TITLE = "Application.title";
	public static final String APP_NAME = "Application.name";
	public static final String APP_BUILD_DATE = "Application.build.date";
	public static final String APP_MAIN_FONT = "Application.mainFont";

	private static final Logger LOGGER = Logger.getLogger(LedMatrixApp.class);

	private SettingsManager settingsManager;

	private LedMatrixController ledMatrixController;
	
	private LedMatrixGfxModel ledMatrixGfxModel;

	private List<SerialPortConnector> serialPortConnectors = Lists.newArrayList(); 

	/*
	 * Main method launching the application. 
	 * After logging has been set up, the application will launch using the swing application framework (SAF).
	 */
	public static void main(String[] args) {
		SettingsManager.configureLog4j();
		LOGGER.log(Level.INFO, "Detected platform is " + AppHelper.getPlatform());
		launch(LedMatrixApp.class, args);
	}

	/**
	 * A convenient static getter for the application instance.
	 * @return the instance of RunwalkVideoApp
	 */
	public static LedMatrixApp getApplication() {
		return Application.getInstance(LedMatrixApp.class);
	}


	/** {@inheritDoc} */ 
	@Override
	protected void initialize(String[] args) { 
		LOGGER.log(Level.INFO, "Starting " + getTitle());
		// register an exception handler on the EDT
		settingsManager = new SettingsManager(getContext().getLocalStorage().getDirectory());
		settingsManager.loadSettings();
		int maxPortNumber = settingsManager.getLedMatrixSettings().getMaxPortNumber();
		LedMatrixConnector ledMatrixConnector = new LedMatrixConnector(settingsManager.getLedMatrixPortSettings(), maxPortNumber);
		serialPortConnectors.add(ledMatrixConnector);
		ThorlabsConnector thorlabsConnector = new ThorlabsDC2100Connector(settingsManager.getThorLabsConnectorSettings());
		serialPortConnectors.add(thorlabsConnector);
		LedMatrixSettings ledMatrixSettings = settingsManager.getLedMatrixSettings();
		ExperimentSettings experimentSettings = settingsManager.getExperimentSettings();
		ledMatrixGfxModel = new LedMatrixGfxModelBuilder(ledMatrixSettings).build();
		LedMatrixPanelModel ledMatrixPanelModel = new LedMatrixPanelModel(ledMatrixSettings, experimentSettings);
		ledMatrixController = new LedMatrixController(ledMatrixPanelModel, ledMatrixGfxModel, ledMatrixConnector, thorlabsConnector);
		getContext().getTaskService().execute(ledMatrixController.updateSerialPortNames());
	}

	/**
	 * Initialize and show the application GUI.
	 */
	@Override
	protected void startup() {//
		JPanel mainPanel = new JPanel(new MigLayout("fill, nogrid, flowx, insets 10"));
		LedMatrixGfxView ledMatrixGfxView = new LedMatrixGfxView(ledMatrixController, ledMatrixGfxModel);
		LedMatrixPanelView ledMatrixControlsView = new LedMatrixPanelView(ledMatrixController);
		// init the view
		ledMatrixController.initViews(ledMatrixControlsView, ledMatrixGfxView);
		// add to the panels
		mainPanel.add(ledMatrixGfxView);
		mainPanel.add(ledMatrixControlsView);
		getMainFrame().add(mainPanel);
		show(getMainFrame());
		getMainFrame().pack();
		ledMatrixController.updateLedMatrix();
	}

	@Override
	public void exit(final EventObject event) {
		if (getContext().getTaskService().isTerminated()) {
			LOGGER.debug("Taskservice terminated. byebye...");
			super.exit(event);
		} else {
			ResourceMap resourceMap = getContext().getResourceMap();
			int result = JOptionPane.showConfirmDialog(getMainFrame(), 
					resourceMap.getString("quit.confirmDialog.text"), 
					resourceMap.getString("quit.Action.text"), JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				LOGGER.debug("Shutdown initiated...");
				ApplicationActionMap actionMap = getContext().getActionMap();
				executeAction(actionMap, DISPOSE_SERIAL_PORT_CONNECTORS_ACTION);
				executeAction(actionMap, SAVE_SETTINGS_ACTION);
				awaitShutdown(event);
			}
		}
	}

	/**
	 * This method will look for an {@link Action} specified with the given key in the given {@link ActionMap}
	 * and invoke its {@link Action#actionPerformed(ActionEvent)} method.
	 *
	 * @param actionMap The {@link ActionMap} containing the {@link Action} to be executed
	 * @param actionKey The key of the {@link Action} to be executed
	 */
	public void executeAction(ActionMap actionMap, String actionKey) {
		javax.swing.Action action = actionMap.get(actionKey);
		if (action != null) {
			ActionEvent actionEvent = new ActionEvent(getMainFrame(), ActionEvent.ACTION_PERFORMED, actionKey);
			action.actionPerformed(actionEvent);
		}
	}

	@Action(block=BlockingScope.APPLICATION)
	public Task<Void, Void> disposeSerialConnectors() {
		return new AbstractTask<Void, Void>(DISPOSE_SERIAL_PORT_CONNECTORS_ACTION) {

			protected Void doInBackground() throws Exception {
				for(SerialPortConnector serialPortConnector : serialPortConnectors) {
					serialPortConnector.close();
				}
				return null;
			}

		};
	}
	

	@Action(block=BlockingScope.APPLICATION)
	public Task<Void, Void> saveSettings() {
		return new AbstractTask<Void, Void>(SAVE_SETTINGS_ACTION) {

			protected Void doInBackground() throws Exception {
				settingsManager.saveSettings();
				return null;
			}

		};
	}

	/**
	 * Start a new {@link Thread} and wait until the {@link TaskService} is completely 
	 * terminated before exiting the application.
	 */
	private void awaitShutdown(final EventObject event) {
		new Thread(new Runnable() {

			public void run() {
				LOGGER.debug("Taskservice shutting down...");
				getContext().getTaskService().shutdown();
				while(!getContext().getTaskService().getTasks().isEmpty()) {
					try {
						getContext().getTaskService().awaitTermination(10, TimeUnit.SECONDS);
						LOGGER.debug("Waiting for tasks on EDT to end...");
						new Robot().waitForIdle();
					} catch (AWTException e) {
						LOGGER.error(e);
					} catch (InterruptedException e) {
						LOGGER.error(e);
					}
				}
				exit(event);
			}

		}, "AwaitShutdownThread").start();
	}
	
	public String getTitle() {
		return getResourceString(APP_TITLE);
	}

	public String getName() {
		return getResourceString(APP_NAME);
	}

	public String getVersionString() {
		return getResourceString(APP_NAME) + "-" + getResourceString(APP_VERSION) + "-" + getResourceString(APP_BUILD_DATE);
	}

	private String getResourceString(String resourceName) {
		return getContext().getResourceMap().getString(resourceName);
	}

}
