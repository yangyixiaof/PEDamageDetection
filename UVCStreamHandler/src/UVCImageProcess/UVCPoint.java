package UVCImageProcess;

public class UVCPoint {
	
	private int row = -1;
	private int col = -1;
	
	public UVCPoint(int row, int col) {
		this.setRow(row);
		this.setCol(col);
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}
	
}
