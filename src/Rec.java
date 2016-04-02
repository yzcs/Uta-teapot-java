

import java.awt.Polygon;

public class Rec {
	Vertex vertics[] = new Vertex[4];
	
	public Rec(Vertex[] vertics){
		this.vertics = vertics;
	}
	
	public boolean isFrontFacing() {
		return (vertics[0].z>0 || vertics[1].z>0 || vertics[2].z>0 || vertics[3].z > 0);
	}
	
	public Polygon toPolygon(int width, int height, View3DCanvas view) {
		Polygon poly = new Polygon();
		//double scale = Math.min(width/1.3,height/2.1);
		for ( int n=0 ; n<vertics.length ; n++ ) {
			Vertex v = view.world2Raster(vertics[n]);
			//poly.addPoint((int)(v.x*scale+width/2), (int)(height-10-v.y*scale));
			poly.addPoint((int)(v.x), (int)(v.y));
		}
		return poly;
	}
}
