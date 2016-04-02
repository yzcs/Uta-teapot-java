

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

// Action listener for mouse
class DragListener extends MouseAdapter implements MouseMotionListener {
	/**
	 * 
	 */
	private final View3DCanvas view3dCanvas;
	/**
	 * @param view3dCanvas
	 */
	
	
	DragListener(View3DCanvas view3dCanvas) {
		this.view3dCanvas = view3dCanvas;
		
	}
	int lastX, lastY;
	public void mousePressed(MouseEvent e) {
		lastX = e.getX();
		lastY = e.getY();
		
	}
	public void mouseMoved(MouseEvent e) {}
	// update pitch and yaw angles when the mouse is dragged.
	public void mouseDragged(MouseEvent e) {
		this.view3dCanvas.yaw += e.getX() - lastX;
		this.view3dCanvas.pitch += e.getY() - lastY;
		lastX = e.getX();
		lastY = e.getY();
		this.view3dCanvas.repaint();
		
	}
}