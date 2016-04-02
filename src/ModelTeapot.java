

import java.awt.*;
import java.awt.event.*;
import java.io.*;

// Main class
public class ModelTeapot extends Frame {
	private static final long serialVersionUID = 1L;
	
	
	// Constructor
	public ModelTeapot(String file) {
		super("Utah Teapot");
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			int num = Integer.parseInt(line);
			Bezier patches[] = new Bezier[num];
			for ( int i=0; i<num; i++ )
				patches[i] = Bezier.loadBezier(br);
			add(new View3DCanvas(patches));
		} catch ( Exception e ) {
			System.out.println(e);
		}
		addWindowListener(new ExitListener());
	}
	
	class ExitListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		String filename = "teapot.bpt";
		//filename = "test.bpt";
		ModelTeapot window = new ModelTeapot(filename);
		window.setSize(800, 800);
		window.setVisible(true);
	}
}
