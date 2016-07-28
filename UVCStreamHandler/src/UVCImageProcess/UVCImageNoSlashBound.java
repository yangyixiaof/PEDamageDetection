package UVCImageProcess;

import java.util.ArrayList;

import org.bytedeco.javacpp.opencv_core.Mat;

public class UVCImageNoSlashBound extends UVCImageBound{
	
	private ArrayList<Mat> deletecontours = new ArrayList<Mat>();
	
	public void AddDeleteContour(Mat contour)
	{
		getDeletecontours().add(contour);
	}

	public ArrayList<Mat> getDeletecontours() {
		return deletecontours;
	}

	public void setDeletecontours(ArrayList<Mat> deletecontours) {
		this.deletecontours = deletecontours;
	}
	
	@Override
	public void Clear() {
		super.Clear();
		deletecontours.clear();
	}
	
}
