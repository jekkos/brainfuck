package be.kuleuven.med.brainfuck.io;

import static be.kuleuven.med.brainfuck.io.LedMatrixConnector.RETURN;

import java.io.ByteArrayOutputStream;

import junit.framework.TestCase;
import be.kuleuven.med.brainfuck.entity.LedPosition;
import be.kuleuven.med.brainfuck.settings.LedSettings;
import be.kuleuven.med.brainfuck.settings.SerialPortSettings;


public class LedMatrixConnectorTest extends TestCase {

	private static final String AW_11_LOW = "00aw011000";
	private static final String AW_10_LOW = "00aw010000";
	private static final String AW_10_HIGH = "00aw010030";
	private static final String AW_11_HIGH = "00aw011030";
	private static final String DW_9_LOW = "00dw009 LOW";
	private static final String DW_10_LOW = "00dw010 LOW";
	private static final String DW_9_HIGH = "00dw009HIGH";
	private static final String DW_10_HIGH = "00dw010HIGH";
	
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
	
	public void testToggleLedDigital() throws Exception {
		LedSettings ledSettings = new LedSettings(LedPosition.ledPositionFor(1, 0));
		ledSettings.setRowPin(10);
		ledSettings.setColumnPin(9);
		ledMatrixConnector.toggleLed(ledSettings, true);
		StringBuilder result = new StringBuilder(DW_10_HIGH).append(RETURN);
		result.append(DW_9_HIGH).append(RETURN);
		assertEquals(result.toString(), byteArrayOutputStream.toString());
		// turn led off
		ledMatrixConnector.toggleLed(ledSettings, false);
		result.append(DW_10_LOW).append(RETURN);
		result.append(DW_9_LOW).append(RETURN);
		assertEquals(result.toString(), byteArrayOutputStream.toString());
	}
	
	public void testToggleLedAnalog() throws Exception {
		LedSettings ledSettings = new LedSettings(LedPosition.ledPositionFor(1, 0));
		ledSettings.setRowPin(10);
		ledSettings.setColumnPin(11);
		ledSettings.setIntensity(30);
		ledMatrixConnector.toggleLed(ledSettings, true);
		StringBuilder result = new StringBuilder(AW_10_HIGH).append(RETURN);
		result.append(AW_11_HIGH).append(RETURN);
		assertEquals(result.toString(), byteArrayOutputStream.toString());
		// turn led off
		ledMatrixConnector.toggleLed(ledSettings, false);
		result.append(AW_10_LOW).append(RETURN);
		result.append(AW_11_LOW).append(RETURN);
		assertEquals(result.toString(), byteArrayOutputStream.toString());
	}
	
}
