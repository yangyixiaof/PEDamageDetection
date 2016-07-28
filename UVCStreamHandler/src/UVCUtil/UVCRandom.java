package UVCUtil;

public class UVCRandom {
	
	public static int RandomInt(int start, int stop)
	{
		return (int)(start+Math.random()*(stop-1+1));
	}
	
}
