package UVCTest;

import java.util.LinkedList;
import java.util.List;

public class UVCJavaTest {
	
	public static void main(String[] args) {
		List<Integer> uu = new LinkedList<Integer>();
		uu.add(5);
		List<Integer> aa = new LinkedList<Integer>();
		aa.addAll(uu.subList(1, 1));
		System.out.println("aa size:" + aa.size());
		
		System.err.println(Math.asin(0.99));
	}
	
}