package mlproject.abstractMath.vectorMaker;

import java.util.ArrayList;
import java.util.List;

import mlproject.abstractMath.VectorMaker;

public class CombinedVectorMaker<T> implements VectorMaker<T> {

	final List<VectorMaker<T>> comboList;
	
	public CombinedVectorMaker(List<VectorMaker<T>> comboList) {
		this.comboList = comboList;
	}
	
	public CombinedVectorMaker(VectorMaker<T>... makers) {
		comboList = new ArrayList<VectorMaker<T>>();
		for(VectorMaker<T> maker: makers) comboList.add(maker);
	}
	
	public CombinedVectorMaker() {
		comboList = new ArrayList<VectorMaker<T>>();
	}

	@Override
	public String name() {
		StringBuffer name = new StringBuffer();
		name.append("Combo: ");
		for(VectorMaker<T> maker: comboList) {
			name.append(maker.name());
			name.append("; ");
		}

		return name.toString();
	}

	@Override
	public Double[] toVector(T t) {
		Double[] vector = new Double[vectorSize()];
		
		int atPlace = 0;
		for(VectorMaker<T> maker: comboList) {
			for(Double d: maker.toVector(t)) {
				vector[atPlace] = d;
				atPlace++;
			}
		}

		return vector;
	}

	@Override
	public int vectorSize() {
		int vectorSize = 0;
		for(VectorMaker<T> maker: comboList) {
			vectorSize += maker.vectorSize();
		}

		return vectorSize;
	}

}
