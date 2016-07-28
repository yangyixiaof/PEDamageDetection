package UVCImageProcess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_imgproc;

import UVCUI.UVCImageTempUI;
import UVCUtil.UVCBoundInfo;
import UVCUtil.UVCBoundSort;
import UVCUtil.UVCLineUtil;
import UVCUtil.UVCRandom;

public class UVCImageContours {
	
	public static Mat Erodes(final Mat img_ini, int times) {
		Mat turn_start = img_ini;
		for (int i = 0; i < times; i++) {
			Mat img_dilate = new Mat();
			opencv_imgproc.erode(turn_start, img_dilate,
					opencv_imgproc.getStructuringElement(opencv_imgproc.MORPH_CROSS, new Size(5, 5)));
			turn_start = img_dilate;
		}
		return turn_start;
	}
	
	public static Mat Dilates(final Mat img_ini, int times) {
		Mat turn_start = img_ini;
		for (int i = 0; i < times; i++) {
			Mat img_eliminate = new Mat();
			opencv_imgproc.dilate(turn_start, img_eliminate,
					opencv_imgproc.getStructuringElement(opencv_imgproc.MORPH_CROSS, new Size(5, 5)));
			turn_start = img_eliminate;
		}
		return turn_start;
	}
	
	public static Mat MultipleTryAndCanny(Mat img_ini, int times) {
		Mat img_edges = null;
		for (int i = 0; i < times; i++) {
			// eliminate small dots by corroding and expanding.
			// corrode.
			// Mat img_dilate = Erodes(img_ini, 2);
			// UVCImageUI.ShowImageInFrame(img_dilate, "Erode");
			// expand.
			// Mat img_eliminate = Dilates(img_dilate, 2);
			// UVCImageUI.ShowImageInFrame(img_eliminate, "Dilate");
			// Mat img_edges_input = img_eliminate;

			// canny.
			Mat img_edges_input = img_ini;
			Mat img_edges_canny = new Mat();
			opencv_imgproc.Canny(img_edges_input, img_edges_canny, 22, 32);

			// laplacian.
			// opencv_imgproc.Laplacian(img_smooth, img_edges_raw,
			// opencv_core.CV_16S, 3, 1, 0, opencv_core.BORDER_DEFAULT);
			// Mat img_edges = new Mat();
			// opencv_core.convertScaleAbs(img_edges_raw, img_edges);

			// sobel.
			// gradient X.
			// Mat img_x_src = img_smooth;
			// Mat img_x_src_raw = new Mat();
			// Mat img_x_src_res = new Mat();
			// opencv_imgproc.Sobel(img_x_src, img_x_src_raw,
			// opencv_core.CV_16S, 1, 0, 3, 1, 1, opencv_core.BORDER_DEFAULT);
			// opencv_core.convertScaleAbs(img_x_src_raw, img_x_src_res);
			// UVCImageUI.ShowImageInFrame(img_x_src_res, "X Sobel");
			// gradient Y.
			// Mat img_y_src = img_x_src_res;
			// Mat img_y_src_raw = new Mat();
			// Mat img_y_src_res = new Mat();
			// opencv_imgproc.Sobel(img_y_src, img_y_src_raw,
			// opencv_core.CV_16S, 1, 0, 3, 1, 0, opencv_core.BORDER_DEFAULT);
			// opencv_core.convertScaleAbs(img_y_src_raw, img_y_src_res);
			// UVCImageUI.ShowImageInFrame(img_y_src_res, "Y Sobel");
			// merge gradient X and Y.
			// Mat img_sobel = new Mat();
			// opencv_core.addWeighted(img_x_src_res, 0.5, img_y_src_res, 0.5,
			// 0, img_sobel);
			// Mat img_edges = img_sobel;

			// compute result.
			Mat temp_res = new Mat();
			// System.out.println("ini:" + img_ini.channels() + ";1:" +
			// img_edges.channels() + ";2:" + img_edges_raw.channels());
			if (img_edges == null) {
				img_edges = img_edges_canny;
			} else {
				opencv_core.bitwise_and(img_edges, img_edges_canny, temp_res);
				img_edges = temp_res;
			}
		}
		return img_edges;
	}
	
	public static UVCImageBound DeleteSmallContourArea(Mat img_bin, String description) {
		UVCImageBound uvcib = new UVCImageBound();
		final Scalar whitecolor = new Scalar(255, 255, 255, 255);
		Mat contour_area = Mat.zeros(img_bin.rows(), img_bin.cols(), opencv_core.CV_8UC3).asMat();
		Mat drawing = Mat.zeros(img_bin.rows(), img_bin.cols(), opencv_core.CV_8UC3).asMat();
		MatVector img_contour_mv = new MatVector();
		opencv_imgproc.findContours(img_bin, img_contour_mv, opencv_imgproc.CV_RETR_LIST,
				opencv_imgproc.CV_CHAIN_APPROX_NONE);
		long size = img_contour_mv.size();
		ArrayList<Integer> lens = new ArrayList<Integer>();
		for (int i = 0; i < size; i++) {
			int len = img_contour_mv.get(i).rows();
			if (len >= UVCImageMetaInfo.DeleteEdgeLength) {
				lens.add(len);
			}
		}
		Collections.sort(lens);
		int llen = lens.size();
		int nocloseidx = (int)((llen*1.0)/5.0*3.0);
		int noclosethreshold = lens.get(nocloseidx);
		noclosethreshold = Math.max(noclosethreshold, UVCImageMetaInfo.MinEdgeLength);
		
		int closeidx = (int)((llen*1.0)/5.0*4.0);
		int closethreshold = lens.get(closeidx);
		closethreshold = Math.max(closethreshold, UVCImageMetaInfo.CloseEdgeLength);
		
		// System.out.println("llen:" + lens.toString() + ";threshold:" + threshold);
		for (int i = 0; i < size; i++) {
			// draw different colors to the image.
			Scalar color = new Scalar(UVCRandom.RandomInt(0, 255), UVCRandom.RandomInt(0, 255),
					UVCRandom.RandomInt(0, 255), UVCRandom.RandomInt(0, 255));
			opencv_imgproc.drawContours(drawing, img_contour_mv, i, color);
			// compute and fill the rectangle of the contour with color white.
			Mat onecontour = img_contour_mv.get(i);
			// compute area.
			double carea = opencv_imgproc.contourArea(onecontour);
			int clen = onecontour.rows();
			if ((clen < noclosethreshold) || (carea > 0 && clen < closethreshold)) {
				// delete it.
				// opencv_imgproc.drawContours(contour_area, img_contour_mv, i,
				// whitecolor);
			} else {
				// draw it.
				opencv_imgproc.drawContours(contour_area, img_contour_mv, i, whitecolor);
				uvcib.AddContour(onecontour);
				// compute rect and fill it.
				// Rect mrect = opencv_imgproc.boundingRect(onecontour);
				// opencv_imgproc.rectangle(contour_area, mrect, whitecolor,
				// opencv_imgproc.CV_FILLED, opencv_core.LINE_8, 0);
			}
		}
		Mat contour_area_bin = new Mat();
		Mat contour_area_gray = new Mat();
		opencv_imgproc.cvtColor(contour_area, contour_area_gray, opencv_imgproc.COLOR_BGR2GRAY);
		opencv_imgproc.threshold(contour_area_gray, contour_area_bin, 1, 255, opencv_imgproc.CV_THRESH_BINARY);
		UVCImageTempUI.ShowImageInFrame(drawing, "Contour_Image" + "_" + description);
		UVCImageTempUI.ShowImageInFrame(contour_area_bin, "Contour_White_Image" + "_" + description);
		uvcib.setImg_bound_bin(contour_area_bin);
		return uvcib;
	}
	
	public static UVCImageNoSlashBound DeleteForwardSlash(UVCImageBound img_rl_bd) {
		UVCImageNoSlashBound result = new UVCImageNoSlashBound();
		Mat img_bin = img_rl_bd.getImg_bound_bin().clone();// new Mat();
		result.setImg_bound_bin(img_bin);
		ArrayList<Mat> contours = img_rl_bd.getContours();
		Iterator<Mat> citr = contours.iterator();
		while (citr.hasNext())
		{
			Mat cmat = citr.next();
			ArrayList<Point> bpoints = new ArrayList<Point>();
			int ilen = cmat.rows();
			int allrow = 0;
			int allcol = 0;
			int allnum = 0;
			for (int i = 0; i < ilen; i++) {
				if (i % 3 == 0)
				{
					if (allrow > 0 || allcol > 0 )
					{
						Point pt = new Point(allrow/allnum, allcol/allnum);
						bpoints.add(pt);
					}
					allrow = 0;
					allcol = 0;
					allnum = 0;
				}
				int rowidx = UVCImageElementUtil.getMatElement(cmat, i, 0, 4);
				int colidx = UVCImageElementUtil.getMatElement(cmat, i, 0, 0);
				allrow += rowidx;
				allcol += colidx;
				allnum++;
			}
			if ((allrow > 0 || allcol > 0) && allnum > 0)
			{
				Point pt = new Point(allrow/allnum, allcol/allnum);
				bpoints.add(pt);
			}
			
			PriorityQueue<Double> pqueue = new PriorityQueue<Double>();
			int bpsize = bpoints.size();
			for (int i=0;i<bpsize-UVCImageMetaInfo.LineSegmentWindow;i++)
			{
				List<Point> bps = bpoints.subList(i, i+UVCImageMetaInfo.LineSegmentWindow);
				double angle = UVCLineUtil.ComputeAngle(bps);
				if (angle < 0)
				{
					angle += 180.0;
				}
				if (angle < 0 || angle > 180.0)
				{
					System.err.println("Serious error, wrong angle. The system will exit.");
					System.exit(1);
				}
				pqueue.add(angle);
			}
			double allangle = 0;
			double allsize = 0;
			int size = Math.min(UVCImageMetaInfo.MaxSlopeSize, pqueue.size());
			for (int i=0;i<size;i++)
			{
				double angle = pqueue.poll();
				int tweight = (size-i)+1;
				allangle += tweight * angle;
				allsize += tweight;
			}
			double aveangle = allangle / allsize;
			if (aveangle <= 70.0 || aveangle >= 110.0) {
				// delete it.
				int cilen = cmat.rows();
				for (int i = 0; i < cilen; i++) {
					int rowidx = UVCImageElementUtil.getMatElement(cmat, i, 0, 4);
					int colidx = UVCImageElementUtil.getMatElement(cmat, i, 0, 0);
					UVCImageElementUtil.setMatElement(img_bin, rowidx, colidx, 0, 0);
				}
				result.AddDeleteContour(cmat);
			} else {
				result.AddContour(cmat);
			}
		}
		return result;
	}
	
	public static UVCBoundInfo ComputeBound(UVCImageNoSlashBound uvc_no_bd)
	{
		UVCBoundInfo uvcbi = new UVCBoundInfo();
		
		PriorityQueue<UVCBoundSort> leftmid = new PriorityQueue<UVCBoundSort>();
		PriorityQueue<UVCBoundSort> rightmid = new PriorityQueue<UVCBoundSort>();
		
		Mat umat = uvc_no_bd.getImg_bound_bin();
		int cols = umat.cols();
		int halfcols = cols / 2;
		ArrayList<Mat> contours = uvc_no_bd.getContours();
		Iterator<Mat> itr = contours.iterator();
		while (itr.hasNext())
		{
			Mat cmat = itr.next();
			int cilen = cmat.rows();
			int maxcolidx = 0;
			int mincolidx = cols * 10;
			for (int i = 0; i < cilen; i++) {
				int colidx = UVCImageElementUtil.getMatElement(cmat, i, 0, 0);
				if (maxcolidx < colidx)
				{
					maxcolidx = colidx;
				}
				if (mincolidx > colidx)
				{
					mincolidx = colidx;
				}
			}
			if (maxcolidx >= halfcols && halfcols >= mincolidx) {
				continue;
			} else {
				if (maxcolidx < halfcols)
				{
					leftmid.add(new UVCBoundSort(-maxcolidx, cmat));
				}
				if (mincolidx > halfcols)
				{
					rightmid.add(new UVCBoundSort(mincolidx, cmat));
				}
			}
		}
		int leftsize = Math.min(leftmid.size(), UVCImageMetaInfo.BoundWindow);
		int rightsize = Math.min(rightmid.size(), UVCImageMetaInfo.BoundWindow);
		
		double lefttotal = 0;
		for (int i=0;i<leftsize;i++)
		{
			UVCBoundSort lm = leftmid.poll();
			lefttotal -= lm.getCol();
			uvcbi.addLeftBound(lm.getCmat());
		}
		int left = (int)(lefttotal / (leftsize*1.0));
		
		double righttotal = 0;
		for (int i=0;i<rightsize;i++)
		{
			UVCBoundSort rm = rightmid.poll();
			righttotal += rm.getCol();
			uvcbi.addRightBound(rm.getCmat());
		}
		int right = (int)(righttotal / (rightsize*1.0));
		
		uvcbi.setLeftcol(left);
		uvcbi.setRightcol(right);
		return uvcbi;
	}
	
	public static Mat DeleteForwardSlashInSmallBin(UVCImageNoSlashBound img_no_slash, Mat img_small_bin) {
		Mat result = img_small_bin.clone();// new Mat(img_small_bin);
		ArrayList<Mat> dltcontours = img_no_slash.getDeletecontours();
		DeleteContours(result, dltcontours);
		return result;
	}
	
	private static void DeleteContours(Mat result, ArrayList<Mat> contours)
	{
		Iterator<Mat> citr = contours.iterator();
		while (citr.hasNext())
		{
			Mat cmat = citr.next();
			DeleteContour(result, cmat);
		}
	}
	
	private static void DeleteContour(Mat result, Mat cmat)
	{
		int cilen = cmat.rows();
		for (int i = 0; i < cilen; i++) {
			int rowidx = UVCImageElementUtil.getMatElement(cmat, i, 0, 4);
			int colidx = UVCImageElementUtil.getMatElement(cmat, i, 0, 0);
			UVCImageElementUtil.setMatElement(result, rowidx, colidx, 0, 0);
		}
	}

	public static ArrayList<Mat> FindSuitableContours(Mat img_no_bound_slash, UVCBoundInfo ubinfo) {
		int cols = img_no_bound_slash.cols();
		ArrayList<Mat> result = new ArrayList<Mat>();
		MatVector img_contour_mv = new MatVector();
		opencv_imgproc.findContours(img_no_bound_slash, img_contour_mv, opencv_imgproc.CV_RETR_LIST,
				opencv_imgproc.CV_CHAIN_APPROX_NONE);
		long size = img_contour_mv.size();
		for (int i = 0; i < size; i++) {
			Mat cmat = img_contour_mv.get(i);
			int cilen = cmat.rows();
			int maxcolidx = 0;
			int mincolidx = cols * 10;
			for (int j = 0; j < cilen; j++) {
				int colidx = UVCImageElementUtil.getMatElement(cmat, j, 0, 0);
				if (maxcolidx < colidx)
				{
					maxcolidx = colidx;
				}
				if (mincolidx > colidx)
				{
					mincolidx = colidx;
				}
			}
			if (IsInRange(mincolidx, maxcolidx, ubinfo))
			{
				result.add(cmat);
			}
		}
		return result;
	}
	
	private static boolean IsInRange(int mincolidx, int maxcolidx, UVCBoundInfo ubinfo)
	{
		if (mincolidx >= ubinfo.getLeftcol() && maxcolidx <= ubinfo.getRightcol())
		{
			/*if ((mincolidx-ubinfo.getLeftcol())*1.0/((maxcolidx-mincolidx)*1.0) <= 0.15)
			{
				return false;
			}
			if ((ubinfo.getRightcol()-maxcolidx)*1.0/((maxcolidx-mincolidx)*1.0) <= 0.15)
			{
				return false;
			}*/
			return true;
		}
		if (mincolidx < ubinfo.getLeftcol() && maxcolidx >= ubinfo.getLeftcol() && maxcolidx <= ubinfo.getRightcol())
		{
			if ((maxcolidx-ubinfo.getLeftcol())*1.0/((maxcolidx-mincolidx)*1.0) >= 0.85)
			{
				return true;
			}
		}
		if (mincolidx >= ubinfo.getLeftcol() && mincolidx <= ubinfo.getRightcol() && maxcolidx > ubinfo.getRightcol())
		{
			if ((ubinfo.getRightcol()-mincolidx)*1.0/((maxcolidx-mincolidx)*1.0) >= 0.85)
			{
				return true;
			}
		}
		return false;
	}

	public static Mat DeleteBoundInNoSlash(Mat img_no_bound_slash, UVCBoundInfo ubinfo) {
		Mat result = img_no_bound_slash.clone();// new Mat(img_no_bound_slash);
		ArrayList<Mat> lefts = ubinfo.getLeftbound();
		DeleteContours(result, lefts);
		ArrayList<Mat> rights = ubinfo.getRightbound();
		DeleteContours(result, rights);
		return result;
	}
	
}