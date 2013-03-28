package be.kuleuven.med.brainfuck.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Double;
import java.util.Set;

import javax.swing.JPanel;

import be.kuleuven.med.brainfuck.entity.LedPosition;

import com.google.common.collect.Sets;

public class LedMatrixGfxView extends JPanel {

	private static final long serialVersionUID = 1L;

	private Ellipse2D.Double[][] ellipseMatrix;
	
	private Set<Ellipse2D.Double> selectedEllipses = Sets.newHashSet();

	public LedMatrixGfxView(final LedMatrixController ledMatrixController) {	
		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent event) {
				// TODO could implement multi select here.. but will need to review the bean's model
				LedPosition ledPosition = selectLed(event, true);
				repaint();
				ledMatrixController.updateSelectedLed(ledPosition, true);
			}
			
		});
	}

	public LedMatrixGfxView(int width, int height) {
		ellipseMatrix = new Ellipse2D.Double[width][height];
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics g = graphics.create();
		g.setColor(Color.WHITE);
		g.fillRect(0,  0, getWidth(), getHeight());
		for (int i = 0; ellipseMatrix != null && i < ellipseMatrix.length; i++) {
			for (int j = 0; j < ellipseMatrix[0].length; j++) {
				Double shape = ellipseMatrix[i][j];
				if (selectedEllipses.contains(shape)) {
					g.setColor(Color.BLUE);
					g.fillOval((int) shape.x, (int) shape.y, (int) shape.width, (int) shape.height);
				} else {
					g.setColor(Color.BLACK);
					g.drawOval((int) shape.x, (int) shape.y, (int) shape.width, (int) shape.height);
				}
			}
		}
		g.dispose();
	}
	
	public LedPosition selectLed(MouseEvent event, boolean clear) {
		int x = event.getX();
		int y = event.getY();
		if (clear) {
			selectedEllipses.clear();
			// shift was pressed?? multi select enabled
		} else {
			
		}
		for (int i = 0; i < ellipseMatrix.length; i++) {
			for (int j = 0; j < ellipseMatrix[0].length; j++) {
				Double shape = ellipseMatrix[i][j];
				if (shape.contains(x, y)) {
					selectedEllipses.add(shape);
					return LedPosition.ledPositionFor(i, j);
				}
			}
		}
		return null;
	}

	public void setMatrixSize(int width, int height) {
		ellipseMatrix = buildShapeMatrix(width, height);
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
	
	private Ellipse2D.Double[][] buildShapeMatrix(int width, int height) {
		Ellipse2D.Double[][] result = new Ellipse2D.Double[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int size = Math.min(getLedDiameterX(width), getLedDiameterY(height));
				result[i][j] = new Ellipse2D.Double(getPosX(i, width), getPosY(j, height), size, size);
			}
		}
		return result;
	}

}