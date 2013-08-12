package be.kuleuven.med.brainfuck.connector.test;

import static be.kuleuven.med.brainfuck.connector.LedMatrixConnector.RETURN;

import java.io.ByteArrayOutputStream;

import junit.framework.TestCase;
import be.kuleuven.med.brainfuck.connector.LedMatrixConnector;
import be.kuleuven.med.brainfuck.domain.LedPosition;
import be.kuleuven.med.brainfuck.domain.LedSettings;
import be.kuleuven.med.brainfuck.domain.SerialPortSettings;


public class LedMatrixConnectorTest extends TestCase {

	private static final String AW_10_LOW = LedMatrixConnector.buildAnalogWrite(false, 10, 0);
	private static final String AW_10_HIGH = LedMatrixConnector.buildAnalogWrite(false, 10, 30);

	private static final String DW_11_LOW = LedMatrixConnector.buildDigitalWrite(false, 11);
	private static final String DW_11_HIGH = LedMatrixConnector.buildDigitalWrite(true, 11);
	private static final String DW_9_LOW = LedMatrixConnector.buildDigitalWrite(false, 9);
	private static final String DW_10_LOW = LedMatrixConnector.buildDigitalWrite(false, 10);
	private static final String DW_9_HIGH = LedMatrixConnector.buildDigitalWrite(true, 9);
	private static final String DW_10_HIGH = LedMatrixConnector.buildDigitalWrite(true, 10);
	
	private LedMatrixConnector ledMatrixConnector;
	private ByteArrayOutputStream byteArrayOutputStream;
	
	public void setUp() {
		ledMatrixConnector = new LedMatrixConnector(new SerialPortSettings(), 13);
		byteArrayOutputStream = new ByteArrayOutputStream();	
		ledMatrixConnector.setOutput(byteArrayOutputStream);
	}
	
//	public void testDisableAllLeds() throws Exception {
//		ledMatrixConnector.disableAllLeds();
//		assertEquals("00al LOW" + RETURN, byteArrayOutputStream.toString());
//	}
	
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
		// columns will use digital switching (p-channel)
		result.append(DW_11_HIGH).append(RETURN);
		assertEquals(result.toString(), byteArrayOutputStream.toString());
		// turn led off
		ledMatrixConnector.toggleLed(ledSettings, false);
		result.append(AW_10_LOW).append(RETURN);
		result.append(DW_11_LOW).append(RETURN);
		assertEquals(result.toString(), byteArrayOutputStream.toString());
	}
	
}
