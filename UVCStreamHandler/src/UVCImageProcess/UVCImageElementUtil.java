package UVCImageProcess;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.opencv_core.Mat;

public class UVCImageElementUtil {
	
	public static void setMatElement(Mat img, int row, int col, int channel, int value)
	{
		BytePointer bytePointer = img.ptr(row, col);
		bytePointer.put((long)channel, (byte)value);
	}
	
	public static int getMatElement(Mat img, int row, int col, int channel)
	{
		BytePointer bytePointer = img.ptr(row, col);
		int value = bytePointer.get(channel);
		if (value < 0) {
			value = value + 256;
		}
		return value;
	}
	
}