package mlproject.abstractMath;

public class DoubleVectorUtils {
	public static double abs(Double[] v) {
		double total = 0;
		for(int i = 0; i < v.length; i++) total += v[i]*v[i];
		return Math.sqrt(total);
	}
	
	public static Double[] mult(Double scalar, Double[] v) {
		Double[] retVal = new Double[v.length];
		for(int i = 0; i < v.length; i++) {
			if(v[i] == null) v[i] = new Double(0);
			retVal[i] = scalar * v[i];	
		}
		return retVal;
	}
	
	public static Double[] add(Double[] v1, Double[] v2) {
		Double[] retVal = new Double[v1.length];
		for(int i = 0; i < v1.length; i++) {
			if(v1[i] == null) v1[i] = 0.0;
			if(v2[i] == null) v2[i] = 0.0;
			retVal[i] = v1[i] + v2[i];
		}
		return retVal;
	}
	
	public static double dist(Double[] v1, Double[] v2) {
		return abs(add(v1, mult(-1.0, v2)));
	}
	public static double sum(Double[] vector){
		double result = 0;
		for(Double v : vector) result += v;
		return result;
	}
	
	public static void divideAll(Double[] vector, double divisor ){
		for(int i = 0; i < vector.length; i++){
			if(divisor == 0.0){
				vector[i] = 0.0;
			} else{
				vector[i] = vector[i] / divisor;
			}
			
		}
	}
	
	/**
	 * @param population an array, the population
	 * @return the variance
	 */
	public double variance(Double[] population) {
		long n = 0;
		double mean = 0;
		double s = 0.0;

		for (double x : population) {
			n++;
			double delta = x - mean;
			mean += delta / n;
			s += delta * (x - mean);
		}
		// if you want to calculate std deviation
		// of a sample change this to (s/(n-1))
		return (s / n);
	}

	/**
	 * @param population an array, the population
	 * @return the standard deviation
	 */
	public double standard_deviation(Double[] population) {
		return Math.sqrt(variance(population));
	}
	
	
}
