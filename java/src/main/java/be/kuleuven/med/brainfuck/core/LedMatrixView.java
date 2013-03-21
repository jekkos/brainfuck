package be.kuleuven.med.brainfuck.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Double;

import javax.swing.JPanel;

public class LedMatrixView extends JPanel {

	private static final long serialVersionUID = 1L;

	private Ellipse2D.Double[][] ellipseMatrix;

	public LedMatrixView() {	}

	public LedMatrixView(int width, int height) {
		ellipseMatrix = new Ellipse2D.Double[width][height];
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics g = graphics.create();
		g.setColor(Color.WHITE);
		g.fillRect(0,  0, getWidth(), getHeight());
		for (int i = 0; i < ellipseMatrix.length; i++) {
			for (int j = 0; j < ellipseMatrix[0].length; j++) {
				Double shape = ellipseMatrix[i][j];
				g.drawOval((int) shape.x, (int) shape.y, (int) shape.width, (int) shape.height);
			}
		}
		g.dispose();
	}
	
	public Shape calculateMatrixIndices(MouseEvent event) {
		int x = event.getX();
		int y = event.getY();
		for (int i = 0; i < ellipseMatrix.length; i++) {
			for (int j = 0; j < ellipseMatrix[0].length; j++) {
				Double shape = ellipseMatrix[i][j];
				if (shape.contains(x, y)) {
					return shape;
				}
			}
		}
		return null;
	}

	public void setMatrixSize(int width, int height) {
		ellipseMatrix = buildShapeMatrix(width, height);
	}

	private Ellipse2D.Double[][] buildShapeMatrix(int width, int height) {
		Ellipse2D.Double[][] result = new Ellipse2D.Double[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				result[i][j] = new Ellipse2D.Double(10+20*i, 10+20*j, 20, 20);
			}
		}
		return result;
	}

}