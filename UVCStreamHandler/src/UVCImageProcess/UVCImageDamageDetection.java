package UVCImageProcess;

import java.util.ArrayList;
import java.util.Iterator;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;

import UVCUI.UVCImageTempUI;
import UVCUtil.UVCBoundInfo;
import UVCUtil.UVCPointsUtil;

public class UVCImageDamageDetection {

	String imagepath = null;

	public UVCImageDamageDetection(String imagepath) {
		this.imagepath = imagepath;
	}
	
	public void ProcessImage() {
		final Mat img_src = opencv_imgcodecs.imread(imagepath);
		UVCImageTempUI.ShowImageInFrame(img_src, "Input");
		
		Mat img_src_small = new Mat();
		Mat img_src_expand = UVCImageContours.Dilates(img_src, 4);
		opencv_imgproc.resize(img_src_expand, img_src_small, new Size(UVCImageMetaInfo.HalfProcessWidth, UVCImageMetaInfo.HalfProcessHeight));
		
		// grey
		// Mat img_gray = new Mat();
		// opencv_imgproc.cvtColor(img_src, img_gray, opencv_imgproc.CV_BGR2GRAY);
		// opencv_imgproc.applyColorMap(img_src, img_gray, opencv_imgproc.COLORMAP_BONE);
		// UVCImageTestUI.ShowImageInFrame(img_src, "Gray");
		
		// no smooth.
		// Mat img_smooth = img_gray;
		
		// smooth.
		// Mat img_smooth = new Mat();
		// opencv_imgproc.medianBlur(img_gray, img_smooth, 3);
		// opencv_imgproc.GaussianBlur(img_gray, img_smooth, new Size(3, 3), 0, 0, opencv_core.BORDER_DEFAULT);
		// UVCImageTestUI.ShowImageInFrame(img_smooth, "Smooth");
		
		Mat img_edges = new Mat();
		opencv_imgproc.Canny(img_src_small, img_edges, 22, 32);
		
		// Mat img_edges = UVCImageContours.MultipleTryAndCanny(img_smooth, 2);
		// UVCImageTestUI.ShowImageInFrame(img_edges, "Edges");
		// Mat img_edges_blur = img_edges;
		
		Mat img_edges_blur = new Mat();
		opencv_imgproc.blur(img_edges, img_edges_blur, new Size(10, 30));
		UVCImageTempUI.ShowImageInFrame(img_edges_blur, "Smooth_Median");
		// UVCImageInfo.PrintMatInfoExcludeMargin(img_edges_blur);
		
		Mat img_edges_bin = new Mat();
		opencv_imgproc.threshold(img_edges_blur, img_edges_bin, 20, 255, opencv_imgproc.CV_THRESH_BINARY);
		UVCImageTempUI.ShowImageInFrame(img_edges_bin, "Edges");
		
		// Mat img_edges_refine = ScatteredPointsDeletion(img_edges);
		// UVCImageUI.ShowImageInFrame(img_edges_refine, "Edges_Refine");
		
		Mat img_edges_refine = UVCImageContours.Dilates(img_edges_bin, 4);
		UVCImageTempUI.ShowImageInFrame(img_edges_refine, "Binary_Dilate");
		
		Mat img_small = new Mat();
		opencv_imgproc.resize(img_edges_refine, img_small, new Size(UVCImageMetaInfo.FinalProcessWidth, UVCImageMetaInfo.FinalProcessHeight));
		Mat img_small_bin = new Mat();
		opencv_imgproc.threshold(img_small, img_small_bin, 1, 255, opencv_imgproc.CV_THRESH_BINARY);
		UVCImageTempUI.ShowImageInFrame(img_small_bin, "Small_Image");
		
		UVCImageBound img_bound = UVCImageContours.DeleteSmallContourArea(img_small_bin, "Small_Bin.");
		Mat img_rl_bound = UVCImageFeatureDetection.DetectPVCRLBounds(img_bound);
		UVCImageBound img_rl_bound_refine = UVCImageContours.DeleteSmallContourArea(img_rl_bound, "Small_Bin_Refine.");
		UVCImageTempUI.ShowImageInFrame(img_rl_bound_refine.getImg_bound_bin(), "RL_Bound_Mat");
		
		UVCImageNoSlashBound img_no_slash = UVCImageContours.DeleteForwardSlash(img_rl_bound_refine);
		UVCImageTempUI.ShowImageInFrame(img_no_slash.getImg_bound_bin(), "No_Slash");
		
		UVCBoundInfo ubinfo = UVCImageContours.ComputeBound(img_no_slash);
		Mat img_no_bound_slash = UVCImageContours.DeleteForwardSlashInSmallBin(img_no_slash, img_small_bin);
		Mat img_no_bound_slash_no_edge = UVCImageContours.DeleteBoundInNoSlash(img_no_bound_slash, ubinfo);
		
		{
			// testing block, could delete all.
			Mat img_no_bound_slash_no_edge_copy = img_no_bound_slash_no_edge.clone();// new Mat(img_no_bound_slash_no_edge);
			System.err.println("left col:" + ubinfo.getLeftcol() + ";right col:" + ubinfo.getRightcol());
			opencv_imgproc.line(img_no_bound_slash_no_edge_copy, new Point(ubinfo.getLeftcol(), 0), new Point(ubinfo.getLeftcol(), img_no_bound_slash_no_edge.rows()-1), new Scalar(255,0,0,0));
			opencv_imgproc.line(img_no_bound_slash_no_edge_copy, new Point(ubinfo.getRightcol(), 0), new Point(ubinfo.getRightcol(), img_no_bound_slash_no_edge.rows()-1), new Scalar(255,0,0,0));
			UVCImageTempUI.ShowImageInFrame(img_no_bound_slash_no_edge_copy, "Most_Refine");
			UVCImageTempUI.ShowImageInFrame(img_no_bound_slash_no_edge, "Most_Refine_Edges");
		}
		
		ArrayList<Mat> mats = UVCImageContours.FindSuitableContours(img_no_bound_slash_no_edge, ubinfo);
		
		final Mat img_src_copy = img_src.clone();
		int src_rows = img_src_copy.rows();
		int src_cols = img_src_copy.cols();
		int small_rows = img_no_bound_slash.rows();
		int small_cols = img_no_bound_slash.cols();
		
		// draw all rects.
		Iterator<Mat> itr = mats.iterator();
		while (itr.hasNext())
		{
			Mat cmat = itr.next();
			Rect rect = opencv_imgproc.boundingRect(cmat);
			if (rect.area() > 20)
			{
				opencv_imgproc.rectangle(img_no_bound_slash, rect, new Scalar(255,0,0,0));
				Point tl = rect.tl();
				Point br = rect.br();
				Point ntl = UVCPointsUtil.ResizePoint(tl, src_rows, src_cols, small_rows, small_cols);
				Point nbr = UVCPointsUtil.ResizePoint(br, src_rows, src_cols, small_rows, small_cols);
				
				opencv_imgproc.rectangle(img_src_copy, ntl, nbr, new Scalar(255,0,0,255), 3, opencv_core.LINE_8, 0);
			}
		}
		
		opencv_imgproc.line(img_no_bound_slash, new Point(ubinfo.getLeftcol(), 0), new Point(ubinfo.getLeftcol(), img_no_bound_slash_no_edge.rows()-1), new Scalar(255,0,0,0));
		opencv_imgproc.line(img_no_bound_slash, new Point(ubinfo.getRightcol(), 0), new Point(ubinfo.getRightcol(), img_no_bound_slash_no_edge.rows()-1), new Scalar(255,0,0,0));
		UVCImageTempUI.ShowImageInFrame(img_no_bound_slash, "Final_Small_Image");
		UVCImageTempUI.ShowImageInFrame(img_src_copy, "Image_Error_Detection");
		
		/*Mat pointset = new Mat();
		ArrayList<Mat> contours = img_rl_bound_refine.getContours();
		Iterator<Mat> citr = contours.iterator();
		while (citr.hasNext())
		{
			Mat tcontour = citr.next();
			pointset.push_back(tcontour);
		}
		
		RotatedRect rr = opencv_imgproc.fitEllipse(pointset);
		Mat img_rl_bound_bin = img_rl_bound_refine.getImg_bound_bin();
		Point2f pts = new Point2f(4);
		rr.points(pts);
		
		System.err.println("capacity:" + pts.capacity());
		System.err.println("limit:" + pts.limit());
		Point2f i0pos = pts.position(0);
		Point2f i1pos = pts.position(1);
		Point2f i3pos = pts.position(1);
		Point2f i2pos = pts.position(2);
		Point2f i5pos = pts.position(3);
		System.err.println("same?" + (i0pos == i3pos));
		System.err.println("one x:" + (int)i0pos.x() + ";one y:" + (int)i0pos.y() + ";two x:" + (int)i1pos.x() + ";two y:" + (int)i1pos.y());
		for (int i = 0; i < 4; i++)
		{
		//	opencv_imgproc.line(img_rl_bound_bin, new Point((int)ipos.x(), (int)ipos.y()), new Point((int)i1pos.x(), (int)i1pos.y()), new Scalar(0,255,0,0));
		}
		UVCImageTempUI.ShowImageInFrame(img_rl_bound_bin, "RL_Bound_Mat");*/
		
		// Mat img_circles = new Mat();
		// opencv_imgproc.HoughCircles(img_rl_bound_bin, img_circles, opencv_imgproc.HOUGH_GRADIENT, 1, 1);
		// System.err.println("img_circles: channels:" + img_circles.channels() + ";rows:" + img_circles.rows() + ";cols:" + img_circles.cols());
		
		// UVCImageFeatureDetection.DetectAllLines(img_small_bin);
		
		// ArrayList<UVCBound> uvcbds = ScanToFindPartialVerticalBound(img_small);
		// Mat bound_mat = CreateBinaryMatFromPoints(uvcbds, img_small.rows(), img_small.cols());
		// UVCImageTestUI.ShowImageInFrame(bound_mat, "Bound_Mat");
	}
	
	public static void main(String[] args) {
		UVCImageDamageDetection uidd = new UVCImageDamageDetection("data/test1_wrong.jpg");
		uidd.ProcessImage();
	}
	
}