package UVCUtil;

import java.util.ArrayList;

import org.bytedeco.javacpp.opencv_core.Mat;

public class UVCBoundInfo {
	
	private int leftcol = -1;
	private int rightcol = -1;
	
	private ArrayList<Mat> leftbound = new ArrayList<Mat>();
	private ArrayList<Mat> rightbound = new ArrayList<Mat>();
	
	public UVCBoundInfo() {
	}
	
	public int getLeftcol() {
		return leftcol;
	}
	
	public void setLeftcol(int leftcol) {
		this.leftcol = leftcol;
	}
	
	public int getRightcol() {
		return rightcol;
	}
	
	public void setRightcol(int rightcol) {
		this.rightcol = rightcol;
	}
	
	public void addLeftBound(Mat cmat)
	{
		leftbound.add(cmat);
	}

	public ArrayList<Mat> getLeftbound() {
		return leftbound;
	}

	public void setLeftbound(ArrayList<Mat> leftbound) {
		this.leftbound = leftbound;
	}
	
	public void addRightBound(Mat cmat)
	{
		rightbound.add(cmat);
	}

	public ArrayList<Mat> getRightbound() {
		return rightbound;
	}

	public void setRightbound(ArrayList<Mat> rightbound) {
		this.rightbound = rightbound;
	}
	
}