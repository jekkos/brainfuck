package be.kuleuven.med.brainfuck.connector.test;

import junit.framework.TestCase;
import be.kuleuven.med.brainfuck.connector.ThorlabsDC2100Connector;
import be.kuleuven.med.brainfuck.connector.ThorlabsDC2100Connector.OperationMode;
import be.kuleuven.med.brainfuck.domain.SerialPortSettings;

public class ThorlabsDC2100ConnectorTest extends TestCase {

	private static final String COM4 = "COM4";
	
	private ThorlabsDC2100Connector thorlabsConnector;

	@Override
	public void setUp() {
		thorlabsConnector = new ThorlabsDC2100Connector(new SerialPortSettings());
		thorlabsConnector.initialize(COM4);
	}
	
	@Override
	protected void tearDown() throws Exception {
		thorlabsConnector.close();
	}	

	public void testConstantCurrentMode() {
		assertTrue(thorlabsConnector.setOperationMode(OperationMode.CONSTANT_CURRENT));
		assertTrue(thorlabsConnector.setConstantCurrent(20));
		assertEquals(20, thorlabsConnector.getConstantCurrent());
	}

	public void testPwmMode() {
		assertTrue(thorlabsConnector.setOperationMode(OperationMode.PWM));
		assertTrue(thorlabsConnector.setPwmCurrent(25));
		assertEquals(25, thorlabsConnector.getPwmCurrent());
		assertTrue(thorlabsConnector.setPwmCounts(5));
		assertEquals(5, thorlabsConnector.getPwmCounts());
		assertTrue(thorlabsConnector.setPwmFrequency(2));
		assertEquals(2, thorlabsConnector.getPwmFrequency());
	}
	
	public void testCurrentLimiting() {
		assertTrue(thorlabsConnector.setOperationMode(OperationMode.CONSTANT_CURRENT));
		assertTrue(thorlabsConnector.setCurrentLimit(25));
		assertEquals(25, thorlabsConnector.getCurrentLimit());
		// TODO is this expected behavior?
		assertTrue(thorlabsConnector.setConstantCurrent(30));
	}
	
	public void testLedOnOff() {
		assertTrue(thorlabsConnector.setLedOn(true));
		assertTrue(thorlabsConnector.isLedOn());
	}
	
}
