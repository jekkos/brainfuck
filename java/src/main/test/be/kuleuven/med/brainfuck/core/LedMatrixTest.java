package be.kuleuven.med.brainfuck.core;

import junit.framework.TestCase;
import be.kuleuven.med.brainfuck.entity.LedMatrix;
import be.kuleuven.med.brainfuck.entity.LedPosition;
import be.kuleuven.med.brainfuck.settings.LedMatrixSettings;
import be.kuleuven.med.brainfuck.settings.LedMatrixSettingsBuilder;
import be.kuleuven.med.brainfuck.settings.LedSettings;

public class LedMatrixTest extends TestCase {
	
	public void testDecreaseLedMatrixResize() throws Exception {
		LedMatrixSettings ledMatrixSettings = new LedMatrixSettingsBuilder().withSize(3, 2).build();
		LedMatrix ledMatrix = new LedMatrix(ledMatrixSettings);
		
		assertEquals(ledMatrixSettings.getWidth(), ledMatrix.getWidth());
		assertEquals(ledMatrixSettings.getHeight(), ledMatrix.getHeight());
		
		ledMatrix.resizeMatrix(1,  1);
		assertEquals(ledMatrixSettings.getWidth(), 1);
		assertEquals(ledMatrixSettings.getHeight(), 1);
		assertEquals(1, ledMatrixSettings.getLedSettings().size());
		// fetch some led settings
		LedSettings ledSettings = ledMatrix.getLedSettings(LedPosition.ledPositionFor(0,0));
		assertNotNull(ledSettings);
		assertEquals(0, ledSettings.getX());
		assertEquals(0, ledSettings.getY());

		assertNull(ledMatrix.getLedSettings(LedPosition.ledPositionFor(2, 0)));
	}
	
	public void testIncreaseLedMatrixSize() throws Exception {
		LedMatrixSettings ledMatrixSettings = new LedMatrixSettingsBuilder().withSize(3, 2).build();
		LedMatrix ledMatrix = new LedMatrix(ledMatrixSettings);
		LedSettings ledSettings = ledMatrix.getLedSettings(LedPosition.ledPositionFor(0, 1));
		ledSettings.setColumnPin(3);
		ledSettings.setRowPin(15);
		
		assertEquals(ledMatrixSettings.getWidth(), ledMatrix.getWidth());
		assertEquals(ledMatrixSettings.getHeight(), ledMatrix.getHeight());
		
		ledMatrix.resizeMatrix(2,  5);
		assertEquals(ledMatrixSettings.getWidth(), 2);
		assertEquals(ledMatrixSettings.getHeight(), 5);
		assertEquals(10, ledMatrixSettings.getLedSettings().size());
		// fetch some led settings
		ledSettings = ledMatrix.getLedSettings(LedPosition.ledPositionFor(1,4));
		assertNotNull(ledSettings);
		assertEquals(1, ledSettings.getX());
		assertEquals(4, ledSettings.getY());
		assertEquals(0, ledSettings.getRowPin());
		assertEquals(0, ledSettings.getColumnPin());
	
		ledSettings = ledMatrix.getLedSettings(LedPosition.ledPositionFor(0, 1));
		assertEquals(15, ledSettings.getRowPin());
		assertEquals(3, ledSettings.getColumnPin());
	}
	
}
