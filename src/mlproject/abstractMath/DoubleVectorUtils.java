package mlproject.abstractMath;

import java.util.Collection;

public class DoubleVectorUtils {
	
	public static double minkowskiAbsoluteValue(Double[] v, double p) {
		double total = 0;
		for(int i = 0; i < v.length; i++) total += Math.pow(v[i], p);
		return Math.pow(total, 1/p);
	}
	
	public static double euclideanAbsoluteValue(Double[] v) {
		return minkowskiAbsoluteValue(v, 2);
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
		return euclideanAbsoluteValue(add(v1, mult(-1.0, v2)));
	}
	
	public static double minkowskiDistance(Double[] v1, Double[] v2, Double p) {
		return minkowskiAbsoluteValue(add(v1, mult(-1.0, v2)), p);
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
	
	public static Double[] addVectors(Double[] v1, Double[] v2) {
		Double[] sum = new Double[v1.length];
		for(int i = 0; i < v1.length; i++) sum[i] = v1[i] + v2[i];
		return sum;
	}
	
	public static Double[] addVectors(Collection<Double[]> vectors) {
		Double[] sum = null;
		for(Double[] vector: vectors) {
			if (sum == null) sum = vector;
			else sum = addVectors(sum, vector);
		}
		return sum;
	}
	
	public static Double[] findMean(Collection<Double[]> vectors) {
		Double[] mean = addVectors(vectors);
		divideAll(mean, vectors.size());
		return mean;
	}
	
	public static Double[][] findCovarience(Collection<Double[]> vectors) {
		int d = getDim(vectors);
		
		Double[] mean = findMean(vectors);
		
		Double[][] covar = new Double[d][d];
		
		for(int i = 0; i < d; i++) {
			for(int j = 0; j < d; j++) {
				Double sum = 0d;
				for(Double[] vector: vectors) {
					sum += (vector[i] - mean[i])*(vector[j] - mean[j]);
				}
				covar[i][j] = sum / (vectors.size() - 1);
			}
		}
		
		return covar;
	}
	
	private static int getDim(Collection<Double[]> vectors) {
		for(Double[] vector: vectors) return vector.length;
		return 0;
	}
}
