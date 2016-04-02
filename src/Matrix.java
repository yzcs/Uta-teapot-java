public class Matrix {
	private int column = 0;
	private int row = 0;
	private double[][] data = null;
	
	public Matrix(int row, int column){
		this.row = row;
		this.column = column;
		data = new double[row][column];
	}
	public Matrix(double[][] arr){
		this.row = arr.length;
		this.column = arr[0].length;
		data = arr.clone();
	}
	
	public void setElement(int row, int column, double data){
		this.data[row][column] = data;
	}
	
	public double getElement(int row, int column){
		return data[row][column];
	}

	public Vertex multiple(Vertex from) {
		double x = from.x * data[0][0] + from.y * data[0][1] + from.z * data[0][2] + 1 * data[0][3];
		double y = from.x * data[1][0] + from.y * data[1][1] + from.z * data[1][2] + 1 * data[1][3];
		double z = from.x * data[2][0] + from.y * data[2][1] + from.z * data[2][2] + 1 * data[2][3];
		double w = from.x * data[3][0] + from.y * data[3][1] + from.z * data[3][2] + 1 * data[3][3];
		return new Vertex(x / w, y / w, z / w);
	}
	
	public Matrix multiple(Matrix b){
		assert((column == b.row) && (row == b.column));
		
		Matrix ret = new Matrix(row, b.column);
		for(int i = 0; i < row; i++){
			for(int j = 0; j < b.column; j++){
				double result = 0;
				for(int m = 0; m < b.column; m++){
					result += data[i][m] * b.data[j][m]; 
				}
				ret.data[i][j] = result;
			}
		}
		
		return ret;
	}
	
	public int getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}
	
	public Matrix inverse(){
		if((row == 2) && column == 2){
			double val = data[0][0] * data[1][1] - data[1][0] * data[0][1];
			if(val == 0)
				System.out.println("WARNING");
			Matrix r = new Matrix(2, 2);
			r.setElement(0, 0, data[1][1] / val);
			r.setElement(0, 1,  -data[0][1] / val);
			r.setElement(1, 0, -data[1][0] / val);
			r.setElement(1, 1, data[0][0] / val);
			return r;
		}
		throw new RuntimeException("matrix inverse");
	}
	
}
