

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

// Triangles definition
class Bezier {
	// the 4 by 4 control points
	Vertex controls[][] = null;
	
	// load one Bezier surface from file.
	public static Bezier loadBezier(BufferedReader reader) throws IOException {
		Bezier b = new Bezier();
		String[] bSize = reader.readLine().split(" ");
		int bX = Integer.parseInt(bSize[0]);
		int bY = Integer.parseInt(bSize[1]);
		b.controls = new Vertex[bY][bX];
		for ( int i=0; i<bY; i++ ) {
			String tokens[] = reader.readLine().split("\t");
			for ( int j=0; j<bX; j++ )
				b.controls[i][j] = Vertex.parseVertex(tokens[j]);
		}
		return b;
	}
	
	public void paint(List<Rec> primitives, View3DCanvas view) {
		int num_u = 8, num_v = 8;
		Vertex ps[][] = new Vertex[num_u][num_v];
		for(int i = 0; i < num_u; i++){
			for(int j = 0; j < num_v; j++){
				double u = (double)i / (num_u - 1);
				double v = (double)j / (num_v - 1);
				ps[i][j] = bezierSurface(u, v, controls);
			}
		}
		
			
		for(int i = 0; i < ps.length - 1; i++){
			for(int j = 0; j < ps[0].length - 1; j++){
				primitives.add(new Rec(new Vertex[]{
					(ps[i][j]),
					(ps[i][j + 1]),
					(ps[i + 1][j])
					}));
				primitives.add(new Rec(new Vertex[]{
					(ps[i][j + 1]),
					(ps[i + 1][j + 1]),
					(ps[i + 1][j])
					}));
			}
		}
	}
	
	/**
	 * calculate bezier surface with parameter u and v
	 * @param u
	 * @param v
	 * @param k
	 * @return
	 */
	private Vertex bezierSurface(double u, double v, Vertex[][] k){
		Vertex p = new Vertex(0, 0, 0);
		int m = k.length - 1;
		int n = k[0].length - 1; 
				
		for(int i = 0; i <= n; i++){
			for(int j = 0; j <= m; j++){
				double item1 = bernsteinPolynomial(n, i, u);
				double item2 = bernsteinPolynomial(m, j, v);
				p = p.add(k[i][j].multiple(item1 * item2));
			}
		}
		
		return p;
	}
	
	/**
	 * calculate bernstein polynomial
	 * @param n
	 * @param i
	 * @param u
	 * @return
	 */
	private double bernsteinPolynomial(int n, int i, double u){
		long bi = binomialCoefficient(n, i);
		double item2 = Math.pow(u, i);
		double item3 = Math.pow(1 - u, n - i);
		return bi * item2 * item3;
	}
	
	/**
	 * calculate binomial coefficient
	 * @param n
	 * @param i
	 * @return
	 */
	private long binomialCoefficient(int n, int i){
		long factorialN = factorial(n);
		long factorialI = factorial(i);
		long factorialNmI = factorial(n - i);
		return factorialN / (factorialI * factorialNmI);
	}
	
	/**
	 * return factorial of given number
	 * @param n
	 * @return factorial of n
	 */
	private long factorial(int n){
		if(n <= 1)
			return 1;
		return n * factorial(n - 1);
	}
}