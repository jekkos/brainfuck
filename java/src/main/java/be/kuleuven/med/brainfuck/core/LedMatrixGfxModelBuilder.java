package be.kuleuven.med.brainfuck.core;

import java.awt.geom.Ellipse2D;
import java.util.List;
import java.util.Map;

import be.kuleuven.med.brainfuck.entity.LedPosition;
import be.kuleuven.med.brainfuck.settings.LedMatrixSettings;
import be.kuleuven.med.brainfuck.settings.LedSettings;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class LedMatrixGfxModelBuilder {
	
	private final LedMatrixGfxModel ledMatrixModel;
	
	public LedMatrixGfxModelBuilder(LedMatrixSettings ledMatrixSettings) {
		// popuplate internal data structure
		ledMatrixModel = new LedMatrixGfxModel(ledMatrixSettings, buildMatrix(ledMatrixSettings));
	}
	
	public LedMatrixGfxModelBuilder(LedMatrixGfxModel ledMatrixModel) {
		this.ledMatrixModel = ledMatrixModel;
	}
	
	private Map<LedPosition, LedSettings> buildMatrix(LedMatrixSettings ledMatrixSettings) {
		Map<LedPosition, LedSettings> result = Maps.newHashMap();
		for(LedSettings ledSettings : ledMatrixSettings.getLedSettingsList()) {
			result.put(ledSettings.getLedPosition(), ledSettings);
		}
		return result;
	}
	
	private LedMatrixSettings getLedMatrixSettings() {
		return ledMatrixModel.getLedMatrixSettings();
	}
	
	private void removeLedSettings(int width, int height) {
		int oldWidth = getLedMatrixSettings().getWidth();
		int oldHeight = getLedMatrixSettings().getHeight();
		for (int i = 0; i < oldWidth; i++) {
			boolean removeRow = i >= width;
			for (int j = 0; j < oldHeight; j++) {
				boolean removeColumn = j >= height;
				LedPosition ledPosition = LedPosition.ledPositionFor(i, j);
				LedSettings ledSettings = ledMatrixModel.getLedSettings(ledPosition);
				if (ledSettings != null && (removeRow || removeColumn)) {
					ledMatrixModel.removeLedSettings(ledSettings);
				}
			}
		}
	}
	
	private void addLedSettings(int width, int height) {
		// go throguh all available settings and create new entries
		for (int i = 0; i < width; i ++) {
			for (int j = 0; j < height; j ++) {
				LedPosition ledPosition = LedPosition.ledPositionFor(i, j);
				if (ledMatrixModel.getLedSettings(ledPosition) == null) {
					LedSettings ledSettings = new LedSettings(ledPosition);
					ledMatrixModel.addLedSettings(ledSettings);
				}
			}
		}
	}
	
	public LedMatrixGfxModelBuilder resizeMatrix(int width, int height) {
		removeLedSettings(width, height);
		addLedSettings(width, height);
		//FIXME build shape list.. need to associate it with a position
		//shapeList = buildShapeMatrix(width, height);
		// add or remove nodes..
		getLedMatrixSettings().setWidth(width);
		getLedMatrixSettings().setHeight(height);
		return this;
	}
	
	private int getLedDiameterX(int nbRows) {
		return getWidth() / nbRows - nbRows*10;
	}
	
	private int getLedDiameterY(int nbColumns) {
		return getHeight() / nbColumns - nbColumns*10;
	}
	
	private int getPosX(int x, int nbRows) {
		return getLedDiameterX(nbRows)*x + (x+1)*20;
	}
	
	private int getPosY(int y, int nbColumns) {
		return getLedDiameterY(nbColumns)*y + (y+1)*20;
	}
	
	private List<Ellipse2D.Double> buildShapeList(int width, int height) {
		List<Ellipse2D.Double> result = Lists.newArrayList();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int size = Math.min(getLedDiameterX(width), getLedDiameterY(height));
				result.add(new Ellipse2D.Double(getPosX(i, width), getPosY(j, height), size, size));
			}
		}
		return result;
	}
	
	public int getWidth() {
		return getLedMatrixSettings().getWidth();
	}

	public int getHeight() {
		return getLedMatrixSettings().getHeight();
	}
	
	public LedMatrixGfxModel build() {
		return ledMatrixModel;
	}

}
