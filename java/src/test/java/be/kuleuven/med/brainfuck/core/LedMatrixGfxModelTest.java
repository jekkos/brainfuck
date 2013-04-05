package be.kuleuven.med.brainfuck.core;

import junit.framework.TestCase;
import be.kuleuven.med.brainfuck.entity.LedPosition;
import be.kuleuven.med.brainfuck.settings.LedMatrixSettings;
import be.kuleuven.med.brainfuck.settings.LedMatrixSettingsBuilder;
import be.kuleuven.med.brainfuck.settings.LedSettings;

public class LedMatrixGfxModelTest extends TestCase {
	
	public void testDecreaseLedMatrixResize() throws Exception {
		LedMatrixSettings ledMatrixSettings = new LedMatrixSettingsBuilder().withSize(3, 2).build();
		LedMatrixGfxModel ledMatrixModel = new LedMatrixGfxModelBuilder(ledMatrixSettings).build();
		
		assertEquals(ledMatrixSettings.getWidth(), ledMatrixModel.getWidth());
		assertEquals(ledMatrixSettings.getHeight(), ledMatrixModel.getHeight());
		
		ledMatrixModel = new LedMatrixGfxModelBuilder(ledMatrixModel).resizeMatrix(1,  1).build();
		assertEquals(ledMatrixSettings.getWidth(), 1);
		assertEquals(ledMatrixSettings.getHeight(), 1);
		assertEquals(1, ledMatrixSettings.getLedSettingsList().size());
		// fetch some led settings
		LedSettings ledSettings = ledMatrixModel.getLedSettings(LedPosition.ledPositionFor(0,0));
		assertNotNull(ledSettings);
		assertEquals(0, ledSettings.getX());
		assertEquals(0, ledSettings.getY());

		assertNull(ledMatrixModel.getLedSettings(LedPosition.ledPositionFor(2, 0)));
	}
	
	public void testIncreaseLedMatrixSize() throws Exception {
		LedMatrixSettings ledMatrixSettings = new LedMatrixSettingsBuilder().withSize(3, 2).build();
		LedMatrixGfxModel ledMatrixGfxModel = new LedMatrixGfxModelBuilder(ledMatrixSettings).build();
		LedSettings ledSettings = ledMatrixGfxModel.getLedSettings(LedPosition.ledPositionFor(0, 1));
		ledSettings.setColumnPin(3);
		ledSettings.setRowPin(15);
		
		assertEquals(ledMatrixSettings.getWidth(), ledMatrixGfxModel.getWidth());
		assertEquals(ledMatrixSettings.getHeight(), ledMatrixGfxModel.getHeight());
		
		ledMatrixGfxModel = new LedMatrixGfxModelBuilder(ledMatrixGfxModel).resizeMatrix(2,  5).build();
		assertEquals(ledMatrixSettings.getWidth(), 2);
		assertEquals(ledMatrixSettings.getHeight(), 5);
		assertEquals(10, ledMatrixSettings.getLedSettingsList().size());
		// fetch some led settings
		ledSettings = ledMatrixGfxModel.getLedSettings(LedPosition.ledPositionFor(1,4));
		assertNotNull(ledSettings);
		assertEquals(1, ledSettings.getX());
		assertEquals(4, ledSettings.getY());
		assertEquals(0, ledSettings.getRowPin());
		assertEquals(0, ledSettings.getColumnPin());
	
		ledSettings = ledMatrixGfxModel.getLedSettings(LedPosition.ledPositionFor(0, 1));
		assertEquals(15, ledSettings.getRowPin());
		assertEquals(3, ledSettings.getColumnPin());
	}
	
}
