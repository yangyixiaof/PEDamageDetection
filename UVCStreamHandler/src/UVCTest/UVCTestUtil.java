package UVCTest;

import java.util.ArrayList;
import java.util.Iterator;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.opencv_core.Mat;

import UVCImageProcess.UVCImageBound;
import UVCImageProcess.UVCImageElementUtil;
import UVCUI.UVCImageTempUI;
import UVCUtil.UVCRandom;

public class UVCTestUtil {
	
	/**
	 * test function to clear all pixels.
	 */
	public static void TestBlackAll(Mat img_bin)
	{
		Mat img_rs = img_bin.clone();// new Mat(img_bin);
		for(int i=0;i<img_rs.rows();i++)
		{
			for(int j=0;j<img_rs.cols();j++)
			{
				BytePointer bytePointer = img_rs.ptr(i, j);
				bytePointer.put((long)0, (byte)255);
			}
		}
		UVCImageTempUI.ShowImageInFrame(img_rs, "Mat Rs.");
	}
	
	public static void ShowImageBound(UVCImageBound img_bound)
	{
		ArrayList<Mat> ps = img_bound.getContours();
		Mat img_bound_bin = img_bound.getImg_bound_bin();
		Iterator<Mat> itr = ps.iterator();
		int idx = 0;
		while (itr.hasNext())
		{
			idx++;
			Mat img_bound_bin_one = img_bound_bin.clone();// new Mat(img_bound_bin);
			Mat imat = itr.next();
			// System.out.println("imat channels:" + imat.channels());
			int ilen = imat.rows();
			for (int i=0;i<ilen;i++)
			{
				int rowidx = UVCImageElementUtil.getMatElement(imat, i, 0, 4);
				int colidx = UVCImageElementUtil.getMatElement(imat, i, 0, 0);
				// System.out.println("col:" + rowidx + ";row:" + colidx);
				UVCImageElementUtil.setMatElement(img_bound_bin_one, rowidx, colidx, 0, UVCRandom.RandomInt(0, 255));
			}
			UVCImageTempUI.ShowImageInFrame(img_bound_bin_one, "Img_Bound_Image" + idx);
		}
	}
	
	public static void main(String[] args) {
		int llen = 7;
		double lidx = (llen*1.0)/10.0*9.0;
		System.err.println(lidx);
		System.err.println((int)lidx);
	}
	
}