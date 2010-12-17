package mlproject.abstractMath.vectorMaker;

import java.util.ArrayList;
import java.util.List;

import mlproject.abstractMath.VectorMaker;

public class PolynomialVectorMaker<T> implements VectorMaker<T> {
	final VectorMaker<T> basis;
	final int degree;
	
	public PolynomialVectorMaker(int degree, VectorMaker<T> basis) {
		this.basis = basis;
		this.degree = degree;
	}
	
	@Override
	public Double[] toVector(T t) {
		Double[] orig = basis.toVector(t);
		Double[] v = new Double[vectorSize()];
		
		int currentIdx = 0;
		
		List<int[]> partitions = getPartitions(basis.vectorSize(), degree);
		
		for(int[] partition: partitions) {
			double partitionValue = 1.0;
			
			for(int i = 0; i < partition.length; i++) {
				partitionValue *= Math.pow(orig[i], partition[i]);
			}
			
			v[currentIdx] = partitionValue;
			currentIdx++;
		}
		
		return v;
	}

	@Override
	public int vectorSize() {
		int s = basis.vectorSize();
		return getPartitions(s, degree).size();
	}

	@Override
	public String name() {
		return "Polynomial Degree 2 of " + basis.name();
	}
	
	/**
	 * @param length
	 * @param sum
	 * @return a list of all vectors with a given length whose
	 *  sum is less than or equal to the given sum.
	 */
	public static List<int[]> getPartitions(int length, int sum) {
		List<int[]> retVal = new ArrayList<int[]>();
		
		if (length == 0) {
			retVal.add(new int[0]);
			return retVal;
		}
		
		for(int first = 0; first <= sum; first++) {
			List<int[]> subParts = getPartitions(length-1, sum-first);
			for(int[] subPart: subParts) {
				int[] fullPartition = new int[length];
				fullPartition[0] = first;
				for(int i = 0; i < subPart.length; i++)fullPartition[i+1] = subPart[i];
				retVal.add(fullPartition);
			}
		}
		
		return retVal;
	}
	
	public static void main(String[] args) {
		List<int[]> parts = getPartitions(3, 2);
		
		for(int[] part: parts)  {
			for(int i = 0; i < part.length; i++) {
				System.out.print(part[i] + " ");
			}
			System.out.println("");
		}
		System.out.println("");
		
		PolynomialVectorMaker<Integer> pvm = 
			new PolynomialVectorMaker<Integer>(3, new VectorMaker<Integer>() {

				@Override
				public String name() {
					return "test";
				}

				@Override
				public Double[] toVector(Integer t) {
					Double[] retVal = {2.0, 5.0, 10.0};
					return retVal;
				}

				@Override
				public int vectorSize() {
					return 3;
				}
				
			});
		
		
		Double[] v = pvm.toVector(1);
		for(int i = 0; i < v.length; i++) {
			System.out.println(v[i]);
		}
	}
	
}