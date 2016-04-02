

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// Action listener for keyboard
class ArrowListener extends KeyAdapter {
	/**
	 * 
	 */
	private View3DCanvas view3dCanvas = null;

	/**
	 * @param view3dCanvas
	 */
	public ArrowListener(View3DCanvas view3dCanvas) {
		this.view3dCanvas = view3dCanvas;
	}

	public void keyPressed(KeyEvent e) {
		if ( e.getKeyCode() == KeyEvent.VK_DOWN && this.view3dCanvas.focal>5 )
			this.view3dCanvas.focal --;
		else if ( e.getKeyCode() == KeyEvent.VK_UP && this.view3dCanvas.focal<50 )
			this.view3dCanvas.focal ++;
		else if ( e.getKeyCode() == KeyEvent.VK_LEFT && this.view3dCanvas.size>1 )
			this.view3dCanvas.size --;
		else if ( e.getKeyCode() == KeyEvent.VK_RIGHT && this.view3dCanvas.size<20 )
			this.view3dCanvas.size ++;
		this.view3dCanvas.repaint();
	}
}