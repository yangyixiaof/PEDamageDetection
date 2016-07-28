package UVCUI;

import java.util.Map;
import java.util.TreeMap;

import javax.swing.WindowConstants;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;

import UVCImageProcess.UVCImageMetaInfo;

public class UVCImageTempUI {
	
	private static Map<String, CanvasFrame> cfmap = new TreeMap<String, CanvasFrame>();
	
	public static void ShowImageInFrame(final Mat image, final String caption) {
		if (UVCImageMetaInfo.PrintImage)
		{
			final CanvasFrame canvas = new CanvasFrame(caption, 1.0);
			canvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			final OpenCVFrameConverter<?> converter = new OpenCVFrameConverter.ToMat();
			canvas.showImage(converter.convert(image));
		}
	}
	
	public static void ShowImageInFrameWithIdentification(final Mat image, final String caption)
	{
		if (UVCImageMetaInfo.PrintImage)
		{
			CanvasFrame canvas = cfmap.get(caption);
			if (canvas == null)
			{
				canvas = new CanvasFrame(caption, 1.0);
				canvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				cfmap.put(caption, canvas);
			}
			final OpenCVFrameConverter<?> converter = new OpenCVFrameConverter.ToMat();
			canvas.showImage(converter.convert(image));
		}
	}

	public static void WaitForSeconds(int seconds) {
		if (UVCImageMetaInfo.PrintImage)
		{
			try {
				Thread.sleep(seconds);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void Clear()
	{
		cfmap.clear();
	}
	
}