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

	private Ellipse2D.Double[][] shapes;
	
	private Set<Ellipse2D.Double> selectedEllipses = Sets.newHashSet();

	private LedMatrixGfxModel ledMatrixGfxModel;

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

	public LedMatrixGfxView(LedMatrixGfxModel ledMatrixGfxModel) {
		this.ledMatrixGfxModel = ledMatrixGfxModel;
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics g = graphics.create();
		g.setColor(Color.WHITE);
		g.fillRect(0,  0, getWidth(), getHeight());
		for (int i = 0; shapes != null && i < shapes.length; i++) {
			for (int j = 0; j < shapes[0].length; j++) {
				Double shape = shapes[i][j];
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
		for (int i = 0; i < shapes.length; i++) {
			for (int j = 0; j < shapes[0].length; j++) {
				Double shape = shapes[i][j];
				if (shape.contains(x, y)) {
					selectedEllipses.add(shape);
					return LedPosition.ledPositionFor(i, j);
				}
			}
		}
		return null;
	}

}