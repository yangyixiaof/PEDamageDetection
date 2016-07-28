package UVCTest;

import java.util.ArrayList;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;

import UVCImageProcess.UVCImageElementUtil;
import UVCUtil.UVCLineUtil;

public class UVCLineTest {
	
	public void TestTwoNode()
	{
		Mat tmat = Mat.zeros(10, 10, opencv_core.CV_8UC1).asMat();
		Point p1 = new Point(1, 1);
		Point p2 = new Point(7, 7);
		// Point p3 = new Point(3, 5);
		// Point p4 = new Point(4, 7);
		// Point p5 = new Point(5, 9);
		// Point p6 = new Point(8, 7);
		// Point exact = new Point(7, 7);
		ArrayList<Point> ps = new ArrayList<Point>();
		ps.add(p1);
		ps.add(p2);
		// ps.add(p3);
		// ps.add(p4);
		// ps.add(p5);
		// ps.add(p6);
		// ps.add(exact);
		double angle = UVCLineUtil.ComputeAngle(ps);
		System.err.println("angle:" + angle);
		UVCImageElementUtil.setMatElement(tmat, (int)p1.x(), (int)p1.y(), 0, 255);
		UVCImageElementUtil.setMatElement(tmat, (int)p2.x(), (int)p2.y(), 0, 255);
		// UVCImageElementUtil.setMatElement(tmat, (int)p3.getX(), (int)p3.getY(), 0, 255);
		// UVCImageElementUtil.setMatElement(tmat, (int)p4.getX(), (int)p4.getY(), 0, 255);
		// UVCImageElementUtil.setMatElement(tmat, (int)p5.getX(), (int)p5.getY(), 0, 255);
		// UVCImageElementUtil.setMatElement(tmat, (int)exact.getX(), (int)exact.getY(), 0, 255);
		
		// double aangle = UVCLineUtil.ComputeAngle(p1, p2);
		// UVCLineInfo lf = UVCLineUtil.LineFitting(ps);
		// System.err.println("lf:" + lf);
		// System.err.println("angle:" + lf.getAngle());
		// System.err.println("angle:" + aangle);
		/*Point p4 = new Point(3, (int)(lf.getB()+lf.getA()*4));
		Point p5 = new Point(6, (int)(lf.getB()+lf.getA()*6));
		UVCImageElementUtil.setMatElement(tmat, (int)p4.getX(), (int)p4.getY(), 0, 254);
		UVCImageElementUtil.setMatElement(tmat, (int)p5.getX(), (int)p5.getY(), 0, 254);
		UVCImageTestUI.ShowImageInFrame(tmat, "angle test");*/
	}
	
	public static void main(String[] args) {
		UVCLineTest uvclt = new UVCLineTest();
		uvclt.TestTwoNode();
	}
	
}