

// Vertex (3D point) definition
class Vertex {
	double x, y, z;
	// constructors
	public Vertex(double a, double b, double c) {
		x = a; y = b; z = c;
	}
	// parse a vertex from a line of string.
	public static Vertex parseVertex(String input) {
		String tokens[] = input.split(" ");
		double x = Double.parseDouble(tokens[0]);
		double y = Double.parseDouble(tokens[1]);
		double z = Double.parseDouble(tokens[2]);
		return new Vertex(x, y, z);
	}
	
	public Vertex multiple(double num){
		return new Vertex(x * num, y * num, z * num);
	}
	
	public Vertex add(Vertex v){
		return new Vertex(x + v.x, y + v.y, z + v.z);
	}
	
	@Override
	public String toString(){
		return String.format(
				"x=%f y=%f z=%f",
				x, y, z
				);
	}
	
	public Vertex normalize() {
		double length = Math.sqrt(x * x + y * y + z * z);
		return new Vertex(x / length, y / length, z / length);
	}
	
	public Vertex cross(Vertex b) {
		return new Vertex(y * b.z - z * b.y, z * b.x - x * b.z, x * b.y - y * b.x);
	}
	
	public Vertex negative(){
		return new Vertex(-x, -y, -z);
	}
	
	public double dot(Vertex b){
		return x * b.x + y * b.y + z * b.z;
	}
}