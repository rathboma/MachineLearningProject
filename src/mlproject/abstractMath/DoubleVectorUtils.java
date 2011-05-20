package mlproject.abstractMath;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Jama.Matrix;

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
	
	public static double dot(Double[] v1, Double[] v2) {
		int sum = 0;
		for(int i = 0; i < v1.length; i++) sum += v1[i]*v2[i];
		return sum;
	}
	
	public static Double[] matrixMultiplication(Double[][] m, Double[] v) {
		if (m[0].length != v.length) throw new RuntimeException("No can do!");
		Double[] retVal = new Double[m.length];
		for(int i = 0; i < m.length; i++) retVal[i] = dot(m[i], v);
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
	
	public static String vectorToString(Double[] v) {
		StringBuffer sb = new StringBuffer();
		
		for(int i=0; i < v.length; i++) {
			sb.append(v[i]);
			if (i != v.length-1) sb.append(", ");
		}
		
		return sb.toString();
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
	
	public static Double[] minusVectors(Double[] v1, Double[] v2) {
		Double[] sum = new Double[v1.length];
		for(int i = 0; i < v1.length; i++) sum[i] = v1[i] - v2[i];
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
	
	public static List<Double[]> subtractOutMean(Collection<Double[]> vectors) {
		ArrayList<Double[]> retVal = new ArrayList<Double[]>();
		Double[] mean = findMean(vectors);
		for(Double[] vector: vectors) retVal.add(minusVectors(vector, mean));
		return retVal;
	}
	
	/**
	 * @param vectors: Assume that the mean has already been subtracted out
	 * @return The covariance matrix
	 */
	public static Double[][] findCovarienceAssumingZeroMean(Collection<Double[]> vectors) {
		int d = getDim(vectors);
		
		Double[][] covar = new Double[d][d];
		
		for(int i = 0; i < d; i++) {
			for(int j = 0; j < d; j++) {
				Double sum = 0d;
				for(Double[] vector: vectors) sum += vector[i]*vector[j];
				covar[i][j] = sum / vectors.size();
			}
		}
		
		return covar;
	}
	
	private static int getDim(Collection<Double[]> vectors) {
		for(Double[] vector: vectors) return vector.length;
		return 0;
	}
	
	static public Double[][] inverseMatrix(Double[][] matrix) {
		//Need to do some dumb boxing and un boxing
		double[][] unboxedMatrix = new double[matrix.length][matrix.length];
		for(int i = 0; i < matrix.length; i++)
			for(int j = 0; j < matrix.length; j++)
				unboxedMatrix[i][j] = matrix[i][j];
		
		double[][] unboxedInverse = new Matrix(unboxedMatrix).inverse().getArray();
		Double[][] inverse = new Double[matrix.length][matrix.length];
		for(int i = 0; i < matrix.length; i++)
			for(int j = 0; j < matrix.length; j++)
				inverse[i][j] = unboxedInverse[i][j];
		return inverse;
	}
	
	static public double determinant(Double[][] matrix) {
		//Need to do some dumb boxing and un boxing
		double[][] unboxedMatrix = new double[matrix.length][matrix.length];
		for(int i = 0; i < matrix.length; i++)
			for(int j = 0; j < matrix.length; j++)
				unboxedMatrix[i][j] = matrix[i][j];
		
		return new Matrix(unboxedMatrix).det();
	}
	
	static public double computeGaussian(Double[] v, Double[] mean, Double[][] covar) {
		int k = v.length;
		if (mean.length != k) throw new RuntimeException("This is a problem!");
		if (covar.length != k) throw new RuntimeException("This is a problem!");
				
		double corvarDet = determinant(covar);
		Double[][] inverseCovar = inverseMatrix(covar);
		
		double scale = Math.pow(2*Math.PI, ((double)k)/2) * Math.sqrt(corvarDet);
		
		Double[] offMean = minusVectors(v, mean);
		double dot = dot(offMean, matrixMultiplication(inverseCovar, offMean));
		double val = Math.exp(-.5*dot);
		
		if (val == 0) {
			System.out.println("Bad Bad Bad " + val + " " + dot);
			//System.out.println(-.5 * dot);
			//System.out.println(Math.exp(-.5 * dot));
			//System.out.println(Math.exp(-100));
		}
		
		return val/scale;
	}
}
