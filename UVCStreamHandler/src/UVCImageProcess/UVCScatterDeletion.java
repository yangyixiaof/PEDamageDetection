package UVCImageProcess;

import org.bytedeco.javacpp.opencv_core.Mat;

public class UVCScatterDeletion {
	
	/**
	 * img_bin must be the binary image.
	 * @param img_bin
	 */
	public static Mat ScatteredPointsDeletion(Mat img_bin)
	{
		Mat img_rs = img_bin.clone();// new Mat(img_bin);
		for(int i=0;i<img_bin.rows();i++)
		{
			for(int j=0;j<img_bin.cols();j++)
			{
				boolean deleted = JudgeDeleted(img_bin, i, j);
				if (!deleted)
				{
					deleted = JudgeAndSetToBlackFromOnePointAndOneWindow(img_bin, img_rs, i, j, 11, 1);
				}
				if (!deleted)
				{
					deleted = JudgeAndSetToBlackFromOnePointAndOneWindow(img_bin, img_rs, i, j, 21, 5);
				}
			}
		}
		return img_rs;
	}
	
	private static boolean JudgeDeleted(Mat img_bin, int i, int j)
	{
		int val = UVCImageElementUtil.getMatElement(img_bin, i, j, 0);
		if (val == 0)
		{
			return true;
		}
		return false;
	}
	
	private static boolean JudgeAndSetToBlackFromOnePointAndOneWindow(Mat img_bin, Mat img_ones, int i, int j, final int onewsize, int threshold)
	{
		boolean deleted = false;
		final int onehalfsize = onewsize/2;
		int rows = img_bin.rows();
		int cols = img_bin.cols();
		
		int leftcol = Math.max(j-onehalfsize, 0);
		int rightcol = Math.min(j+onehalfsize, cols); // exclude
		int uprow = Math.max(i-onehalfsize, 0);
		int downrow = Math.min(i+onehalfsize, rows); // exclude
		
		int count = 0;
		for (int ri=uprow;ri<downrow;ri++)
		{
			for (int cj=leftcol;cj<rightcol;cj++)
			{
				int val = UVCImageElementUtil.getMatElement(img_bin, ri, cj, 0);
				if (val  > 0)
				{
					count++;
				}
			}
		}
		if (count <= threshold)
		{
			UVCImageElementUtil.setMatElement(img_ones, i, j, 0, 0);
			deleted = true;
		}
		return deleted;
	}
	
}