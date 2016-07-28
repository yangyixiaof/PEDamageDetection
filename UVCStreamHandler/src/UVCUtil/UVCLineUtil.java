package UVCUtil;

import java.util.Iterator;
import java.util.List;

import org.bytedeco.javacpp.opencv_core.Point;

public class UVCLineUtil {
	
	public static double ComputeAngle(List<Point> ps) {
		Iterator<Point> pitr = ps.iterator();
		Point sp = pitr.next();
		double all = 0;
		int allweight = 0;
		int adweight = ps.size() / 3;
		int idxweight = 0;
		while (pitr.hasNext())
		{
			idxweight++;
			Point ep = pitr.next();
			double angle = ComputeAngle(sp, ep);
			int tweight = adweight + idxweight;
			all += (tweight * angle);
			allweight += tweight;
		}
		double aveangle = all / allweight;
		return aveangle;
	}
	
	public static double ComputeAngle(Point sp, Point ep)
	{
		double width = ep.y() - sp.y();
		double height = ep.x() - sp.x();
		double angle = 90.0;
		if (width == 0) {
			/*if (height > 0)
			{
				angle = -angle;
			}*/
		} else {
			double k = height / width;
			double sinValue = - k / (Math.sqrt(1 + k * k));
			double radian = Math.asin(sinValue);
			angle = radian * 180.0 / Math.PI;
			if (angle < 0)
			{
				angle = angle + 180.0;
			}
		}
		return ComputeAngleWithDirection(sp, ep, angle);
	}
	
	public static double ComputeAngleWithDirection(Point start, Point end, double angle)
	{
		double finalangle = angle;
		int esx = (int)(end.x() - start.x());
		int esy = (int)(end.y() - start.y());
		if (esx > 0 && esy > 0)
		{
			finalangle -= 180.0;
		}
		if (esx > 0 && esy < 0)
		{
			finalangle -= 180.0;
		}
		if (esx < 0 && esy < 0)
		{
			// do nothing.
		}
		if (esx < 0 && esy > 0)
		{
			// do nothing.
		}
		if (esx == 0 && esy > 0)
		{
			finalangle = 0;
		}
		if (esx == 0 && esy < 0)
		{
			finalangle = 180.0;
		}
		if (esx > 0 && esy == 0)
		{
			finalangle = -90.0;
		}
		if (esx < 0 && esy == 0)
		{
			finalangle = 90.0;
		}
		return finalangle;
	}
	
	/*public static double ComputeAngle(List<Point> ps) {
		int pointCount = ps.size();
		if (pointCount <= 1)
		{
			return 0;
		}
		Iterator<Point> psitr = ps.iterator();
		int xCount = 0;
		int yCount = 0;
		int xyCount = 0;
		int xxCount = 0;
		while (psitr.hasNext()) {
			Point p = psitr.next();
			int rowidx = (int) p.getX();
			int colidx = (int) p.getY();
			xCount += colidx;
			yCount += rowidx;
			xyCount += (colidx * rowidx);
			xxCount += (colidx * colidx);
		}
		double kz = (double) (pointCount * xyCount - xCount * yCount);
		double km = (double) (pointCount * xxCount - xCount * xCount);
		double angle = -90;
		if (km != 0) {
			double k = kz / km;
			double sinValue = -k / (Math.sqrt(1 + k * k));
			double radian = Math.asin(sinValue);
			angle = radian * 180.0 / Math.PI;
		}
		return angle;
	}
	
	public static double ComputeAngle(Mat cmat) {
		int pointCount = cmat.rows();
		int xCount = 0;
		int yCount = 0;
		int xyCount = 0;
		int xxCount = 0;
		for (int i = 0; i < pointCount; i++) {
			int rowidx = UVCImageElementUtil.getMatElement(cmat, i, 0, 4);
			int colidx = UVCImageElementUtil.getMatElement(cmat, i, 0, 0);
			xCount += colidx;
			yCount += rowidx;
			xyCount += (colidx * rowidx);
			xxCount += (colidx * colidx);
		}
		double k = (double) (pointCount * xyCount - xCount * yCount)
				/ (double) (pointCount * xxCount - xCount * xCount);
		double sinValue = -k / (Math.sqrt(1 + k * k));
		double radian = Math.asin(sinValue);
		double angle = radian * 180.0 / Math.PI;
		return angle;
	}
	
	public static UVCLineInfo LineFitting(List<Point> ps) {
		Mat line = new Mat();
		Mat points = UVCMatUtil.ListToMat(ps); // , points // new Mat(ps.size(), 1, opencv_core.CV_8UC2);
		
		opencv_imgproc.fitLine(points, line, opencv_imgproc.DIST_L1, 0, 0.01, 0.01);
		
		// System.out.println("line channels:" + line.channels() + ";line size:" + line.size());
		// System.out.println("rows:" + line.rows() + ";cols:" + line.cols());
		
		int vx = UVCImageElementUtil.getMatElement(line, 0, 0, 0);
		int vy = UVCImageElementUtil.getMatElement(line, 1, 0, 0);
		int x = UVCImageElementUtil.getMatElement(line, 2, 0, 0);
		int y = UVCImageElementUtil.getMatElement(line, 3, 0, 0);
		return new UVCLineInfo(vx, vy, x, y);
	}*/
	
}