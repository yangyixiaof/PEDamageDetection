package UVCUtil;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.bytedeco.javacpp.opencv_core.Point;

public class UVCPointsUtil {
	
	/*public static Point ComputeCenter(List<Point> points)
	{
		double allx = 0;
		double ally = 0;
		Iterator<Point> pitr = points.iterator();
		while (pitr.hasNext())
		{
			Point p = pitr.next();
			allx += p.getX();
			ally += p.getY();
		}
		return new Point((int)(allx/(points.size()*1.0)), (int)(ally/(points.size()*1.0)));
	}*/
	
	public static List<Point> ReverseList(List<Point> ps)
	{
		List<Point> plist = new LinkedList<Point>();
		Iterator<Point> pitr = ps.iterator();
		while (pitr.hasNext())
		{
			Point p = pitr.next();
			plist.add(0, p);
		}
		return plist;
	}
	
	public static Point ResizePoint(Point p, int src_rows, int src_cols, int small_rows, int small_cols)
	{
		double rowrate = (small_rows*1.0)/(src_rows*1.0);
		double colrate = (small_cols*1.0)/(src_cols*1.0);
		return new Point((int)(p.x()/colrate), (int)(p.y()/rowrate));
	}
	
}