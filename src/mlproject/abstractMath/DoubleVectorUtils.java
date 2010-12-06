package mlproject.abstractMath;

public class DoubleVectorUtils {
	public static double abs(Double[] v) {
		double total = 0;
		for(int i = 0; i < v.length; i++) total += v[i]*v[i];
		return Math.sqrt(total);
	}
	
	public static Double[] mult(Double scalar, Double[] v) {
		Double[] retVal = new Double[v.length];
		for(int i = 0; i < v.length; i++) retVal[i] = scalar * v[i];
		return retVal;
	}
	
	public static Double[] add(Double[] v1, Double[] v2) {
		Double[] retVal = new Double[v1.length];
		for(int i = 0; i < v1.length; i++) retVal[i] = v1[i] + v2[i];
		return retVal;
	}
	
	public static double dist(Double[] v1, Double[] v2) {
		return abs(add(v1, mult(-1.0, v2)));
	}
	
}
