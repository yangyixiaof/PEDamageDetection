package UVCImageProcess;

import java.util.ArrayList;

import org.bytedeco.javacpp.opencv_core.Mat;

public class UVCImageBound {
	
	private Mat img_bound_bin = null;
	private ArrayList<Mat> contours = new ArrayList<Mat>();
	
	public UVCImageBound() {
	}
	
	public void AddContour(Mat mt)
	{
		getContours().add(mt);
	}
	
	public void Clear()
	{
		getContours().clear();
	}

	public ArrayList<Mat> getContours() {
		return contours;
	}

	public Mat getImg_bound_bin() {
		return img_bound_bin;
	}

	public void setImg_bound_bin(Mat img_bound_bin) {
		this.img_bound_bin = img_bound_bin;
	}
	
}