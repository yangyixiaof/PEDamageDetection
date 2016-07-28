package UVCUtil;

import org.bytedeco.javacpp.opencv_core.Mat;

import UVCImageProcess.UVCImageElementUtil;
import UVCImageProcess.UVCImageMetaInfo;

public class UVCImageInfoPrinter {
	
	public static void PrintMatInfo(Mat img_edges)
	{
		for(int i=0;i<img_edges.rows();i++)
		{
			for(int j=0;j<img_edges.cols();j++)
			{
				System.err.println(UVCImageElementUtil.getMatElement(img_edges, i, j, 0));
			}
		}
	}

	public static void PrintMatInfoExcludeMargin(Mat img_edges) {
		for(int i=0;i<img_edges.rows();i++)
		{
			for(int j=0;j<img_edges.cols();j++)
			{
				int val = UVCImageElementUtil.getMatElement(img_edges, i, j, 0);
				if (val != 0 && val != 255)
				{
					System.err.println(val);
				}
			}
		}
	}
	
	public static void PrintImageTextInfoWithTimeStamp(String info)
	{
		if (UVCImageMetaInfo.PrintImage)
		{
			// SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// String timestring = time.format(new java.util.Date());
			// System.err.println(info + ";" + timestring);
		}
	}
	
}