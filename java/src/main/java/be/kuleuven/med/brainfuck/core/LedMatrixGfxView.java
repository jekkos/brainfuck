package be.kuleuven.med.brainfuck.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JPanel;

import be.kuleuven.med.brainfuck.entity.LedPosition;
import be.kuleuven.med.brainfuck.settings.LedSettings;

import com.google.common.collect.Lists;

public class LedMatrixGfxView extends JPanel {

	private static final long serialVersionUID = 1L;

	private LedMatrixGfxModel ledMatrixGfxModel;
	
	private List<Shape> shapeList;

	public LedMatrixGfxView(final LedMatrixController ledMatrixController, LedMatrixGfxModel ledMatrixGfxModel) {	
		this.ledMatrixGfxModel = ledMatrixGfxModel;
		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent event) {
				// TODO could implement multi select here.. but will need to review the bean's model
				LedPosition ledPosition = findLedPosition(event.getX(), event.getY());
				ledMatrixController.updateSelectedLed(ledPosition, true);
			}
			
		});
	}

	private LedPosition findLedPosition(int x, int y) {
		for (Shape shape : shapeList) {
			if (shape.contains(x, y)) {
				return LedPosition.ledPositionFor(x, y);
			}
		}
		return null;
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		List<Shape> shapes = Lists.newArrayList();
		super.paintComponent(graphics);
		Graphics g = graphics.create();
		g.setColor(Color.WHITE);
		g.fillRect(0,  0, getWidth(), getHeight());
		int width = ledMatrixGfxModel.getWidth();
		int height = ledMatrixGfxModel.getHeight();
		int size = getLedDiameter(width, height);
		for (Entry<LedPosition, LedSettings> ledSettingsEntry : ledMatrixGfxModel.getLedSettingsMap().entrySet()) {
			LedSettings ledSettings = ledSettingsEntry.getValue();
			LedPosition ledPosition = ledSettingsEntry.getKey();
			boolean selected = ledMatrixGfxModel.isSelected(ledSettings);
			boolean illuminated = ledMatrixGfxModel.isIlluminated(ledSettings);
			// set a nice color..
			int red = selected ? 255 : 0;
			int blue = illuminated ? ledSettings.getIntensity() : 0;
			g.setColor(new Color(0, red, blue));
			Ellipse2D.Double shape = new Ellipse2D.Double(getPosX(ledPosition.getX(), width), getPosY(ledPosition.getY(), height), size, size);
			shapes.add(shape);
			Rectangle rectangle = shape.getBounds();
			g.fillOval(rectangle.x, rectangle.y, size, size);
		}
		g.dispose();
		this.shapeList = shapes;
	}
	
	private int getLedDiameter(int width, int height) {
		return Math.min(getLedDiameterX(width), getLedDiameterY(height));
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
	
}