package mlproject.abstractMath.impl;

import mlproject.abstractMath.VectorMaker;

public class PolynomialVectorMaker<T> implements VectorMaker<T> {
	final VectorMaker<T> basis;
	final int degree;
	
	public PolynomialVectorMaker(int degree, VectorMaker<T> basis) {
		this.basis = basis;
		this.degree = 2;
		// TODO Other options besides degree 2.
	}
	
	@Override
	public Double[] toVector(T t) {
		Double[] orig = basis.toVector(t);
		Double[] v = new Double[vectorSize()];
		
		int currentIdx = 0;
		v[currentIdx] = 1.0;
		currentIdx++;
		
		for(int i = 0; i < orig.length; i++) {
			v[currentIdx] = orig[i];
			currentIdx++;
		}
		
		for(int i = 0; i < orig.length; i++) {
			for(int j = i; j < orig.length; j++) {
				v[currentIdx] = orig[i]*orig[j];
				currentIdx++;
			}
		}
		
		return v;
	}
	
	/*@Override
	public Double[] toVector(T t) {
		Double[] orig = basis.toVector(t);
		Double[] v = new Double[vectorSize()];
		
		int currentIdx = 0;
		
		for(int deg = 0; deg <= degree; deg++) {
			
			
			
		}
		
		v[currentIdx] = 1.0;
		currentIdx++;
		
		for(int i = 0; i < orig.length; i++) {
			v[currentIdx] = orig[i];
			currentIdx++;
		}
		
		for(int i = 0; i < orig.length; i++) {
			for(int j = i; j < orig.length; j++) {
				v[currentIdx] = orig[i]*orig[j];
				currentIdx++;
			}
		}
		
		return v;
	}*/

	@Override
	public int vectorSize() {
		int s = basis.vectorSize();
		return 1 + 2*s + s*(s -1)/2;
	}

	@Override
	public String name() {
		return "Polynomial Degree 2 of " + basis.name();
	}

}
