package UVCImageProcess;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;

import UVCUI.UVCImageTempUI;
import UVCUtil.UVCImageInfoPrinter;
import UVCUtil.UVCLineUtil;
import UVCUtil.UVCMatUtil;
import UVCUtil.UVCPointsUtil;

public class UVCImageFeatureDetection {

	public static Mat DetectPVCRLBounds(UVCImageBound img_bound) {
		// UVCTestUtil.ShowImageBound(img_bound);
		ArrayList<Point> alltrimidxlist = new ArrayList<Point>();
		ArrayList<Point> boundpoints = new ArrayList<Point>();
		Mat img_bd = img_bound.getImg_bound_bin();
		final Mat img_test = img_bd.clone();// new Mat(img_bd);
		ArrayList<Mat> contours = img_bound.getContours();
		Iterator<Mat> citr = contours.iterator();
		int cidx = 0;
		int testidx = 0;
		while (citr.hasNext()) {
			Mat cmat = citr.next();
			ArrayList<Point> bpoints = new ArrayList<Point>();
			int ilen = cmat.rows();
			for (int i = 0; i < ilen; i++) {
				int rowidx = UVCImageElementUtil.getMatElement(cmat, i, 0, 4);
				int colidx = UVCImageElementUtil.getMatElement(cmat, i, 0, 0);
				Point pt = new Point(rowidx, colidx);
				bpoints.add(pt);
			}
			
			ArrayList<Integer> trimidxlist = new ArrayList<Integer>();
			int trimidx = 0;
			for (int i=UVCImageMetaInfo.HalfCheckMinRightAngleWindow;i<ilen-UVCImageMetaInfo.HalfCheckMinRightAngleWindow;i++) {
				// testing
				// if (testidx == 65)
				// {
				//	System.err.println("HaHa, it is here.");
				// }
				int adweight = UVCImageMetaInfo.HalfCheckMinRightAngleWindow/2;
				int idxweight = 0;
				double allangle = -0.0001;
				double allweight = 0;
				for (int w=UVCImageMetaInfo.HalfCheckMinRightAngleWindow;w<=UVCImageMetaInfo.HalfCheckMaxRightAngleWindow;w++)
				{
					int lis = i - w;
					lis = Math.max(0, lis);
					lis = Math.max(trimidx, lis);
					int gapsize1 = i-lis;
					int ris = i + w;
					ris = Math.min(ilen-1, ris);
					int gapsize2 = ris-i;
					int gapsize = Math.min(gapsize1, gapsize2);
					if (gapsize < UVCImageMetaInfo.HalfCheckMinRightAngleWindow)
					{
						continue;
					}
					List<Point> pre = bpoints.subList(i-gapsize, i+1);
					List<Point> reversepre = UVCPointsUtil.ReverseList(pre);
					double preangle = UVCLineUtil.ComputeAngle(reversepre);
					
					List<Point> post = bpoints.subList(i, i+gapsize+1);
					double postangle = UVCLineUtil.ComputeAngle(post);
					
					double gapangle = ((preangle - postangle) % 360.0 + 360.0) % 360.0;
					if (gapangle > 180.0)
					{
						gapangle = 360.0 - gapangle;
					}
					idxweight++;
					int tweight = adweight + idxweight;
					allangle += tweight * gapangle;
					allweight += tweight;
				}
				testidx++;
				if (allangle >= 0)
				{
					double aveangle = allangle / (allweight*1.0);
					if (aveangle <= UVCImageMetaInfo.MaxVerticalAngle)
					{
						Point bp = bpoints.get(i);
						UVCImageElementUtil.setMatElement(img_test, (int)bp.x(), (int)bp.y(), 0, 127);
						UVCImageTempUI.ShowImageInFrameWithIdentification(img_test, "Image Right Corner Test.");
						UVCImageInfoPrinter.PrintImageTextInfoWithTimeStamp("im_max_angle:" + aveangle + ";testidx:" + testidx);
						// UVCImageTempUI.WaitForSeconds(1000);
						trimidxlist.add(i);
						trimidx = i+1;
					}
				}
			}
			
			int startidx = 0;
			Iterator<Integer> titr = trimidxlist.iterator();
			while (titr.hasNext())
			{
				int stopidx = titr.next();
				alltrimidxlist.add(bpoints.get(stopidx));
				List<Point> analyps = bpoints.subList(startidx, stopidx);
				TrimHorizontalLinePoints(analyps, boundpoints);
				startidx = stopidx;
			}
			int alllen = trimidxlist.size();
			if (startidx < alllen)
			{
				TrimHorizontalLinePoints(bpoints.subList(startidx, alllen), boundpoints);
			}
			cidx++;
			
			// testing
			// System.out.println("Current Handled:" + cidx);
			// if (cidx >= 1)
			// {
			//	break;
			// }
		}
		assert contours.size() == cidx;
		
		Mat img_trimid = img_bd.clone();// new Mat(img_bd);
		UVCMatUtil.ListToMatChannel1(alltrimidxlist, img_trimid, 127);
		UVCImageTempUI.ShowImageInFrame(img_trimid, "Image Vertical Points");
		
		Mat img_vert = Mat.zeros(img_bd.rows(), img_bd.cols(), opencv_core.CV_8UC1).asMat();
		UVCMatUtil.ListToMatChannel1(boundpoints, img_vert, 255);
		UVCImageTempUI.ShowImageInFrame(img_vert, "Image All Partially Vertical Lines");
		
		return img_vert;
	}
	
	private static void TrimHorizontalLinePoints(List<Point> analyps, List<Point> boundpoints)
	{
		double anaangle = UVCLineUtil.ComputeAngle(analyps);
		// UVCLineInfo analine = UVCLineUtil.LineFitting(analyps);
		// anaangle = analine.getAngle();
		if (anaangle < 0)
		{
			anaangle = (-anaangle) % 180.0;
		}
		if (anaangle > 90.0)
		{
			anaangle = 180.0 - anaangle;
		}
		if (anaangle <= UVCImageMetaInfo.MaxHorizontalAngle) {
			// delete it.
		} else {
			// retain it.
			boundpoints.addAll(analyps.subList(1, analyps.size()));
		}
	}
	
}