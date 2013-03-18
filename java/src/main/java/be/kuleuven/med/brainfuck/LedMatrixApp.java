package be.kuleuven.med.brainfuck;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.EventObject;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.TaskService;
import org.jdesktop.application.utils.AppHelper;

import be.kuleuven.med.brainfuck.core.LedMatrixController;
import be.kuleuven.med.brainfuck.core.LedMatrixModel;
import be.kuleuven.med.brainfuck.entity.LedMatrix;
import be.kuleuven.med.brainfuck.io.SettingsManager;
import be.kuleuven.med.brainfuck.view.LedMatrixView;

public class LedMatrixApp extends SingleFrameApplication {

	public static final String APP_VERSION = "Application.version";
	public static final String APP_TITLE = "Application.title";
	public static final String APP_NAME = "Application.name";
	public static final String APP_BUILD_DATE = "Application.build.date";
	public static final String APP_MAIN_FONT = "Application.mainFont";

	private final static Logger LOGGER = Logger.getLogger(LedMatrixApp.class);

	private SettingsManager settingsManager;
	
	private LedMatrixController ledMatrixController;

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
		LedMatrix ledMatrix = settingsManager.getLedMatrix();
		LedMatrixModel ledMatrixModel = new LedMatrixModel(ledMatrix);
		ledMatrixController = new LedMatrixController(getContext().getTaskService(), ledMatrixModel);
	}

	/**
	 * Initialize and show the application GUI.
	 */
	@Override
	protected void startup() {
		JPanel mainPanel = new JPanel(new MigLayout("fill, nogrid, flowy, insets 10"));
		LedMatrixView ledMatrixView = new LedMatrixView(ledMatrixController);
		ledMatrixController.initView(ledMatrixView);
		mainPanel.add(ledMatrixView);
		//StatusPanel statusPanel = new StatusPanel();
		//mainPanel.add(statusPanel, "height 30!, gapleft push");
		getMainFrame().add(mainPanel);
		show(getMainFrame());
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
				//executeAction(getApplicationActionMap(), "uploadLogFiles");
				//executeAction(getApplicationActionMap(), SAVE_SETTINGS_ACTION);
				awaitShutdown(event);
			}
		}
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
