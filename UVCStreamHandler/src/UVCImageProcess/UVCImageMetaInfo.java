package UVCImageProcess;

public class UVCImageMetaInfo {
	
	public final static boolean PrintImage = true;
	public final static int MinEdgeLength = 20;
	public final static int BoundWindow = 2;
	public final static int MaxSlopeSize = 4;
	public final static int LineSegmentWindow = 5;
	public final static int HalfCheckMinRightAngleWindow = 2;
	public final static int HalfCheckMaxRightAngleWindow = 6;
	// public final static double MinVerticalAngle = 65.0;
	public final static double MaxVerticalAngle = 115.0;
	public final static double MaxHorizontalAngle = 45.0;
	public final static int HalfProcessWidth = 1120;
	public final static int HalfProcessHeight = 840;
	public final static int FinalProcessWidth = 200;
	public final static int FinalProcessHeight = 150;
	public final static int CloseEdgeLength = 4*(FinalProcessHeight/3);
	public final static int DeleteEdgeLength = 8;
	
}