package intray.gui.mainframe;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class IntrayMainFrameDragger extends MouseAdapter {

	private final IntrayMainFrame frame;
	private Point mouseDownCompCoords = null;
	private boolean isActive;
	private int offset_x = 0;
	private int offset_y = 0;

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		this.isActive = active;
	}

	public IntrayMainFrameDragger(IntrayMainFrame frame) {
		this.frame = frame;
	}

	public IntrayMainFrameDragger(IntrayMainFrame intrayMainFrame, int offset_x, int offset_y) {
		this.frame = intrayMainFrame;
		this.offset_x = offset_x;
		this.offset_y = offset_y;
	}

	public void mouseReleased(MouseEvent e) {
		mouseDownCompCoords = null;
	}

	public void mousePressed(MouseEvent e) {
		mouseDownCompCoords = e.getPoint();

	}

	public void mouseDragged(MouseEvent e) {
		if (isActive) {
			Point currCoords = e.getLocationOnScreen();
			if (mouseDownCompCoords == null) {
				mouseDownCompCoords = e.getPoint();
			}
			try {
				int coord_x = currCoords.x - mouseDownCompCoords.x;
				int coord_y = currCoords.y - mouseDownCompCoords.y;
				frame.setLocation(coord_x + offset_x, coord_y + offset_y);
				frame.updatePreferencesLocation(coord_x + offset_x, coord_y + offset_y);
			} catch (Exception e2) {
				System.out.println("frame.setLocation exception");
			}
		}
	}
}
