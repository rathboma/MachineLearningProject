package mlproject;

import java.util.ArrayList;
import java.util.List;

import mlproject.abstractMath.VectorMaker;
import mlproject.abstractMath.vectorMaker.AverageColorVectorMaker;
import mlproject.abstractMath.vectorMaker.ColorHistogramVectorMaker;
import mlproject.abstractMath.vectorMaker.CombinedVectorMaker;
import mlproject.abstractMath.vectorMaker.PolynomialVectorMaker;
import mlproject.abstractMath.vectorMaker.WeightedVectorMaker;
import mlproject.models.Issue;

public class VectorMakerLists {
	public static List<VectorMaker<Issue>> getVMs() {
		List<VectorMaker<Issue>> vectorMakers = new ArrayList<VectorMaker<Issue>>();
		List<VectorMaker<Issue>> baseVMs = getBaseVMs();
//		return getBaseVMs();
		for(VectorMaker<Issue> baseVM: baseVMs) {
			int maxDegree = baseVM.vectorSize() > 10? 1: 5;
			for(int degree = 1; degree <= maxDegree; degree++) {
				vectorMakers.add(new PolynomialVectorMaker<Issue>(degree, baseVM));
			}
		}
		
		return vectorMakers;
	}
	
	public static List<VectorMaker<Issue>> getBaseVMs() {
		List<VectorMaker<Issue>> baseVMs = new ArrayList<VectorMaker<Issue>>();
		
		VectorMaker<Issue> averageColor = new AverageColorVectorMaker();
		VectorMaker<Issue> weighted = new WeightedVectorMaker();
		VectorMaker<Issue> colorHistogram = new ColorHistogramVectorMaker();
		
		baseVMs.add(averageColor);
		baseVMs.add(weighted);
		baseVMs.add(colorHistogram);
//		baseVMs.add(new CombinedVectorMaker<Issue>(averageColor, weighted));
//		baseVMs.add(new CombinedVectorMaker<Issue>(averageColor, colorHistogram));
//		baseVMs.add(new CombinedVectorMaker<Issue>(weighted, colorHistogram));
		baseVMs.add(new CombinedVectorMaker<Issue>(averageColor, weighted, colorHistogram));
		return baseVMs;
	}
}
