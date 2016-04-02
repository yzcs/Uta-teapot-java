

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;

class View3DCanvas extends Canvas {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// the Bezier surface to display.
	Bezier patches[];
	// pitch and paw angles for world to camera transform
	int pitch, yaw;
	// focal length and image size for camera to screen transform
	int focal, size;
	// canvas size for screen to raster transform
	int width, height, scale;
	// initialize the 3D view canvas
	
	private Vertex view = new Vertex(0, 0, 1);
	private Vertex up = new Vertex(1, 0, 0);
	
	public Vertex getView(){
		return this.view;
	}
	
	public Vertex getUp(){
		return this.up;
	}
	
	public void setView(Vertex view){
		this.view = view;
	}
	
	public void setUp(Vertex up){
		this.up = up;
	}
	
	public View3DCanvas(Bezier p[]) {
		patches = p;
		focal = 5; size = 10;
		DragListener drag = new DragListener(this);
		addMouseListener(drag);
		addMouseMotionListener(drag);
		addKeyListener(new ArrowListener(this));
		addComponentListener(new ResizeListener(this));
	}
	
	// Demo transform between world and camera coordinates
	public Vertex world2Camera(Vertex from) {
		double phi = (double)pitch / 100;
		double theta = (double)yaw / 100;
		/**
		 * build rotation matrix
		 */
		Matrix rx = new Matrix(4, 4);
		Matrix ry = new Matrix(4, 4);
		rx.setElement(0, 0, 1);
		rx.setElement(0, 1, 0);
		rx.setElement(0, 2, 0);
		rx.setElement(0, 3, 0);
		rx.setElement(1, 0, 0);
		rx.setElement(1, 1, Math.cos(theta));
		rx.setElement(1, 2, Math.sin(theta));
		rx.setElement(1, 3, 0);
		rx.setElement(2, 0, 0);
		rx.setElement(2, 1, Math.sin(theta));
		rx.setElement(2, 2, Math.cos(theta));
		rx.setElement(2, 3, 0);
		rx.setElement(3, 0, 0);
		rx.setElement(3, 1, 0);
		rx.setElement(3, 2, 0);
		rx.setElement(3, 3, 1);
		
		ry.setElement(0, 0, Math.cos(phi));
		ry.setElement(0, 1, 0);
		ry.setElement(0, 2, Math.sin(phi));
		ry.setElement(0, 3, 0);
		ry.setElement(1, 0, 0);
		ry.setElement(1, 1, 1);
		ry.setElement(1, 2, 0);
		ry.setElement(1, 3, 0);
		ry.setElement(2, 0, -Math.sin(phi));
		ry.setElement(2, 1, 0);
		ry.setElement(2, 2, Math.cos(phi));
		ry.setElement(2, 3, 0);
		ry.setElement(3, 0, 0);
		ry.setElement(3, 1, 0);
		ry.setElement(3, 2, 0);
		ry.setElement(3, 3, 1);
		
		Vertex view = ry.multiple(this.view);	
		
		
		/**
		 * up(rotate about z): rotation of the camera. the surface of this camera
		 */
		Vertex up = ry.multiple(this.up);
		
		assert(view.dot(up) == 0);
		
		Vertex dz = view.normalize();
		Vertex dx = dz.cross(up);
		dx = dx.normalize();
		Vertex dy = dx.cross(dz);
		
		Matrix mat = new Matrix(4, 4);
		mat.setElement(0, 0, dx.x);
		mat.setElement(0, 1, dx.y);
		mat.setElement(0, 2, dx.z);
		mat.setElement(1, 0, dy.x);
		mat.setElement(1, 1, dy.y);
		mat.setElement(1, 2, dy.z);
		mat.setElement(2, 0, dz.x);
		mat.setElement(2, 1, dz.y);
		mat.setElement(2, 2, dz.z);
		mat.setElement(3, 3, 1.0);
		
		Matrix t = new Matrix(4, 4);
		t.setElement(0, 0, 1);
		t.setElement(0, 1, 0);
		t.setElement(0, 2, 0);
		t.setElement(0, 3, 0);
		t.setElement(1, 0, 0);
		t.setElement(1, 1, 1);
		t.setElement(1, 2, 0);
		t.setElement(1, 3, 0);
		t.setElement(2, 0, 0);
		t.setElement(2, 1, 0);
		t.setElement(2, 2, 1);
		t.setElement(2, 3, 1.3);
		t.setElement(3, 0, 0);
		t.setElement(3, 1, 0);
		t.setElement(3, 2, 0);
		t.setElement(3, 3, 1);
				
		//Vertex to = mat.multipler(from.negative());
		Vertex to = mat.multiple(t.multiple(from.negative()));
		return to;
	}
	
	// Demo transform between camera and screen coordinates (scaled parallel projection)
	public Vertex camera2Screen(Vertex from) {
		double[][] arrp = {
				{1.0 / size, 0, 0, 0},
				{0, 1.0 / size, 0, 0, 0},
				{0, 0, 0, 0},
				{0, 0, 1.0 / focal, 1}
		};
		return new Matrix(arrp).multiple(from);
	}
	
	
	// Transform between screen and raster coordinates
	public Vertex screen2Raster(Vertex from) {
		return new Vertex(from.x*scale+width/2, -from.y*scale+height/2, 0);
	}
	
	// Shortcut
	public Vertex world2Raster(Vertex from){
		//return screen2Raster(camera2Screen(world2Camera(from)));
		Vertex v = camera2Screen(world2Camera(from));
		return new Vertex(v.x*scale+width/2, -v.y*scale+height/2, v.z);
	}
	
	private Vertex arealCoord(Vertex A, Vertex B, Vertex C, Vertex P){
		Matrix mabac = new Matrix(2, 2);
		mabac.setElement(0, 0, B.x - A.x);
		mabac.setElement(0, 1, C.x - A.x);
		mabac.setElement(1, 0, B.y - A.y);
		mabac.setElement(1, 1,C.y - A.y);
		
		Matrix mapm = new Matrix(2, 1);
		mapm.setElement(0, 0, P.x - A.x);
		mapm.setElement(1, 0, P.y - A.y);
		
		Matrix r = mabac.inverse().multiple(mapm);
		double beta = r.getElement(0, 0);
		double gamma = r.getElement(1, 0);
		double alpha = 1 - beta - gamma;
		
		return new Vertex(alpha, beta, gamma);
	}
	
	public void paint(Graphics g) {
		// display current parameter values
		g.setColor(Color.blue);
		g.drawString("Pitch = "+pitch+", Yaw = "+yaw+", Focal = "+focal+", Size = "+size, 10, 20);
		// draw the Bezier surface.
		g.setColor(Color.black);
		
		List<Rec> primitives = new LinkedList<Rec>();
		for ( int i=0; i<patches.length; i++ )
			patches[i].paint(primitives, this);
		

		
		double[][] zbuffer = new double[width][height];
		for(int i = 0;i < width; i++){
			for(int j = 0; j < height; j++){
				zbuffer[i][j] = Double.MIN_VALUE;
			}
		}
		
		double near = Double.MAX_VALUE;
		double far = Double.MIN_VALUE;
		for(Rec t: primitives){
			if(t.isFrontFacing()){
				for(int i = 0; i < t.vertics.length; i++){
					double z = t.vertics[i].z;
					if(z < near)
						near = z;
					if(z > far)
						far = z;
				}
			}
		}
		
		for(Rec t: primitives){
			if (t.isFrontFacing()){
				Polygon p = t.toPolygon(width, height, this);
				Rectangle boundingBox = p.getBounds();
				
				for(int i = boundingBox.x; i <= boundingBox.x + boundingBox.width; i++){
					for(int j = boundingBox.y; j <= boundingBox.y + boundingBox.height; j++){
						Vertex areal = null;
						if(p.contains(i, j)){
							areal = arealCoord(
									new Vertex(p.xpoints[0], p.ypoints[0], 0),
									new Vertex(p.xpoints[1], p.ypoints[1], 0),
									new Vertex(p.xpoints[2], p.ypoints[2], 0),
									new Vertex(i, j, 0)
									);
							double z = t.vertics[0].z * areal.x + t.vertics[1].z * areal.y + t.vertics[2].z * areal.z;
							
							try{
							if(z > zbuffer[i][j]){
								zbuffer[i][j] = z;
							}
							}
							catch(Exception e){
								//System.out.println("out of bound");
							}
						}
					}
				}
			}
		}
		
		for(int i = 0; i < zbuffer.length; i++){
			for(int j = 0; j < zbuffer[0].length; j++){
				int c = (int)((zbuffer[i][j] - near) * 255 / (far - near));
				if(c > 255)
					c = 255;
				if(c < 0)
					c = 0;
				//srcImage.setRGB(i, j, new Color(c, c, c).getRGB());
				g.setColor(new Color(c, c, c));
				g.fillRect(i, j, 1, 1);
			}
		}
		
		System.out.println("done");
	}
}
