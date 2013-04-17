package be.kuleuven.med.brainfuck.model.test;

import java.util.Collections;

import junit.framework.TestCase;
import be.kuleuven.med.brainfuck.domain.settings.LedMatrixSettings;
import be.kuleuven.med.brainfuck.domain.settings.LedPosition;
import be.kuleuven.med.brainfuck.domain.settings.LedSettings;
import be.kuleuven.med.brainfuck.domain.settingsbuilder.LedMatrixSettingsBuilder;
import be.kuleuven.med.brainfuck.model.LedMatrixGfxModel;
import be.kuleuven.med.brainfuck.model.LedMatrixGfxSelectionModel;
import be.kuleuven.med.brainfuck.modelbuilder.LedMatrixGfxModelBuilder;
import be.kuleuven.med.brainfuck.modelbuilder.LedMatrixGfxSelectionModelBuilder;

public class LedMatrixGfxSelectionModelTest extends TestCase {

	public void testIncreaseSelection() throws Exception {
		LedMatrixSettings ledMatrixSettings = new LedMatrixSettingsBuilder().withSize(3, 2).build();
		LedMatrixGfxModel ledMatrixModel = new LedMatrixGfxModelBuilder(ledMatrixSettings).build();
		LedSettings ledSettings0 = ledMatrixModel.getLedSettings(LedPosition.ledPositionFor(1, 0));
		ledSettings0.setRowPin(10);
		ledSettings0.setColumnPin(5);
		LedMatrixGfxSelectionModel ledMatrixGfxSelectionModel = 
				LedMatrixGfxSelectionModelBuilder.of(ledSettings0);
		// just one selected, should be equal
		assertEquals(Collections.singleton(ledSettings0), ledMatrixGfxSelectionModel.getSelectedLedSettings());
		assertTrue(ledMatrixGfxSelectionModel.isColumnSelected());
		assertTrue(ledMatrixGfxSelectionModel.isRowSelected());
		// add two to the selection
		LedPosition ledPosition1 = LedPosition.ledPositionFor(1, 1);
		LedSettings ledSettings1 = ledMatrixModel.getLedSettings(ledPosition1);
		ledSettings1.setRowPin(2);
		ledSettings1.setColumnPin(5);
		ledMatrixGfxSelectionModel = new LedMatrixGfxSelectionModelBuilder(ledMatrixGfxSelectionModel)
			.addRemoveLedSettings(ledSettings1).build();
		assertEquals(2, ledMatrixGfxSelectionModel.getSelectedLedSettings().size());
		assertTrue(ledMatrixGfxSelectionModel.isColumnSelected());
		assertFalse(ledMatrixGfxSelectionModel.isRowSelected());
		
		assertEquals(Integer.valueOf(5), ledMatrixGfxSelectionModel.getColumnPin());
		// no common row pin.. will show 0
		assertNull(ledMatrixGfxSelectionModel.getRowPin());
		
		assertTrue(ledMatrixGfxSelectionModel.getSelectedLedSettings().contains(ledSettings0));
		assertTrue(ledMatrixGfxSelectionModel.getSelectedLedSettings().contains(ledSettings1));
	}
	
	public void testDecreaseSelection() throws Exception {
		LedMatrixSettings ledMatrixSettings = new LedMatrixSettingsBuilder().withSize(3, 2).build();
		LedMatrixGfxModel ledMatrixModel = new LedMatrixGfxModelBuilder(ledMatrixSettings).build();
		LedMatrixGfxSelectionModelBuilder ledMatrixGfxSelectionModelBuilder = new LedMatrixGfxSelectionModelBuilder();
		for (int i = 0; i < ledMatrixSettings.getWidth(); i ++) {
			for (int j = 0; j < ledMatrixSettings.getHeight(); j++) {
				LedPosition ledPosition = LedPosition.ledPositionFor(i, j);
				LedSettings ledSettings = ledMatrixModel.getLedSettings(ledPosition);
				ledMatrixGfxSelectionModelBuilder.addRemoveLedSettings(ledSettings);
			}
		}
		
		LedMatrixGfxSelectionModel ledMatrixGfxSelectionModel = ledMatrixGfxSelectionModelBuilder.build(); 
		assertFalse(ledMatrixGfxSelectionModel.isRowSelected());
		assertFalse(ledMatrixGfxSelectionModel.isColumnSelected());
		
		LedPosition ledPosition0 = LedPosition.ledPositionFor(1, 1);
		LedSettings ledSettings0 = ledMatrixModel.getLedSettings(ledPosition0);
		ledMatrixGfxSelectionModel = new LedMatrixGfxSelectionModelBuilder(ledMatrixGfxSelectionModel).addRemoveLedSettings(ledSettings0).build();
		assertFalse(ledMatrixGfxSelectionModel.isSelected(ledSettings0));
		
		LedPosition ledPosition1 = LedPosition.ledPositionFor(1, 0);
		LedSettings ledSettings1 = ledMatrixModel.getLedSettings(ledPosition1);
		assertTrue(ledMatrixGfxSelectionModel.isSelected(ledSettings1));
		// more than one row and column selected.. should be both false
		assertFalse(ledMatrixGfxSelectionModel.isRowSelected());
		assertFalse(ledMatrixGfxSelectionModel.isColumnSelected());
		// no common pins in this case
		assertNull(ledMatrixGfxSelectionModel.getRowPin());
		assertNull(ledMatrixGfxSelectionModel.getColumnPin());
	}
	
}
