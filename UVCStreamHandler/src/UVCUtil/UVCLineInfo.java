package UVCUtil;

public class UVCLineInfo {
	
	private double vx = -1;
	private double vy = -1;
	private double x = -1;
	private double y = -1;
	private double angle = -1;
	
	public UVCLineInfo(double vx, double vy, double x, double y) {
		this.vx = vx;
		this.vy = vy;
		this.x = x;
		this.y = y;
		double k = vy/vx;
		double sinValue = -k / (Math.sqrt(1 + k*k));
		double radian = Math.asin(sinValue);
		double angle = radian * 180.0 / Math.PI;
		this.angle = angle;
	}
	
	public int ComputeXAccordToY(double ty)
	{
		int tx = (int)(((ty-y)*vx/vy) + x);
		return tx;
	}
	
	public int ComputeYAccordToX(double tx)
	{
		int ty = (int)(((tx-x)*vy/vx) + y);
		return ty;
	}
	
	public double getAngle() {
		return angle;
	}
	
	@Override
	public String toString() {
		return "vx:" + vx + ";vy:" + vy + ";x:" + x + ";y:" + y;
	}
	
}