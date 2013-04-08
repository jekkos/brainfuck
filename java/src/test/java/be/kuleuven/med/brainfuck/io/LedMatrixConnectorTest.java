package be.kuleuven.med.brainfuck.io;

import static be.kuleuven.med.brainfuck.io.LedMatrixConnector.RETURN;

import java.io.ByteArrayOutputStream;

import junit.framework.TestCase;
import be.kuleuven.med.brainfuck.entity.LedPosition;
import be.kuleuven.med.brainfuck.settings.LedSettings;
import be.kuleuven.med.brainfuck.settings.SerialPortSettings;


public class LedMatrixConnectorTest extends TestCase {

	private LedMatrixConnector ledMatrixConnector;
	private ByteArrayOutputStream byteArrayOutputStream;
	
	public void setUp() {
		ledMatrixConnector = new LedMatrixConnector(new SerialPortSettings(), 13);
		byteArrayOutputStream = new ByteArrayOutputStream();	
		ledMatrixConnector.setOutput(byteArrayOutputStream);
	}
	
	public void testDisableAllLeds() throws Exception {
		ledMatrixConnector.disableAllLeds();
		assertEquals("00al LOW" + RETURN, byteArrayOutputStream.toString());
	}
	
	public void testInitializeConnector() throws Exception {
		
	}
	
	public void testToggleLeds() throws Exception {
		LedSettings ledSettings = new LedSettings(LedPosition.ledPositionFor(1, 0));
		ledSettings.setRowPin(10);
		ledSettings.setColumnPin(9);
		ledMatrixConnector.toggleLed(ledSettings, true);
		StringBuilder result = new StringBuilder("00dw010HIGH");
		result.append(RETURN);
		result.append("00dw009HIGH");
		result.append(RETURN);
		assertEquals(result.toString(), byteArrayOutputStream.toString());
	}
	
}
