

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

// Resize listener for updating canvas size
class ResizeListener extends ComponentAdapter {
	/**
	 * 
	 */
	private final View3DCanvas view3dCanvas;

	/**
	 * @param view3dCanvas
	 */
	ResizeListener(View3DCanvas view3dCanvas) {
		this.view3dCanvas = view3dCanvas;
	}

	public void componentResized(ComponentEvent e) {
		this.view3dCanvas.width = this.view3dCanvas.getWidth();
		this.view3dCanvas.height = this.view3dCanvas.getHeight();
		this.view3dCanvas.scale = Math.min(this.view3dCanvas.width/2, this.view3dCanvas.height/2);
	}
}