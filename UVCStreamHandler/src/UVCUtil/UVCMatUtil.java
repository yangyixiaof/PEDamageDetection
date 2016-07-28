package UVCUtil;

import java.util.Iterator;
import java.util.List;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;

import UVCImageProcess.UVCImageElementUtil;

public class UVCMatUtil {
	
	public static Mat ListToMat(List<Point> ps)
	{
		Mat mat = new Mat(ps.size(), 2, opencv_core.CV_32SC1);
		Iterator<Point> pitr = ps.iterator();
		int idx = 0;
		while (pitr.hasNext())
		{
			Point p = pitr.next();
			UVCImageElementUtil.setMatElement(mat, idx, 0, 0, (int)p.x());
			UVCImageElementUtil.setMatElement(mat, idx, 1, 0, (int)p.y());
			idx++;
		}
		return mat;
	}
	
	public static void ListToMatChannel2(List<Point> ps, Mat points)
	{
		Iterator<Point> pitr = ps.iterator();
		int idx = 0;
		while (pitr.hasNext())
		{
			Point p = pitr.next();
			UVCImageElementUtil.setMatElement(points, idx, 0, 0, (int)p.x());
			UVCImageElementUtil.setMatElement(points, idx, 0, 1, (int)p.y());
			idx++;
		}
	}
	
	public static void ListToMatChannel1(List<Point> ps, Mat pic, int color)
	{
		Iterator<Point> pitr = ps.iterator();
		while (pitr.hasNext())
		{
			Point p = pitr.next();
			UVCImageElementUtil.setMatElement(pic, (int)p.x(), (int)p.y(), 0, color);
		}
	}
	
}