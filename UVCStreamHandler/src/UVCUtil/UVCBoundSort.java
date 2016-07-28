package UVCUtil;

import org.bytedeco.javacpp.opencv_core.Mat;

public class UVCBoundSort implements Comparable<UVCBoundSort>{
	
	private Integer col = -1;
	private Mat cmat = null;
	
	public UVCBoundSort(int col, Mat cmat) {
		this.setCol(col);
		this.setCmat(cmat);
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public Mat getCmat() {
		return cmat;
	}

	public void setCmat(Mat cmat) {
		this.cmat = cmat;
	}

	@Override
	public int compareTo(UVCBoundSort o) {
		return col.compareTo(o.col);
	}
	
}