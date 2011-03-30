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
	/**
	 * @return These are vectors of high length that take a long time to train,
	 * polynomials of higher degree.
	 */
	public static List<VectorMaker<Issue>> getSlowVMs() {
		List<VectorMaker<Issue>> vectorMakers = new ArrayList<VectorMaker<Issue>>();

//		VectorMaker<Issue> averageColor = new AverageColorVectorMaker();
//		VectorMaker<Issue> weighted = new WeightedVectorMaker();
//
//		vectorMakers.add(new PolynomialVectorMaker<Issue>(2, averageColor));
//		vectorMakers.add(new PolynomialVectorMaker<Issue>(3, averageColor));
//		vectorMakers.add(new PolynomialVectorMaker<Issue>(2, weighted));
//		vectorMakers.add(new PolynomialVectorMaker<Issue>(2, new CombinedVectorMaker<Issue>(averageColor, weighted)));

		System.out.println("number of vector makers : " + vectorMakers.size());
		return vectorMakers;
	}
	
	public static List<VectorMaker<Issue>> getBaseVMs() {
		List<VectorMaker<Issue>> baseVMs = new ArrayList<VectorMaker<Issue>>();
		
		VectorMaker<Issue> averageColor = new AverageColorVectorMaker();
		VectorMaker<Issue> weighted = new WeightedVectorMaker();
		VectorMaker<Issue> colorHistogram = new ColorHistogramVectorMaker();
		
//		baseVMs.add(averageColor);
//		baseVMs.add(weighted);
//		baseVMs.add(colorHistogram);
//		baseVMs.add(new CombinedVectorMaker<Issue>(averageColor, weighted));
		baseVMs.add(new CombinedVectorMaker<Issue>(averageColor, colorHistogram));
//		baseVMs.add(new CombinedVectorMaker<Issue>(weighted, colorHistogram));
//		baseVMs.add(new CombinedVectorMaker<Issue>(averageColor, weighted, colorHistogram));
		return baseVMs;
	}
}
