package mlproject.abstractMath.impl;

import mlproject.abstractMath.DoubleVectorUtils;
import mlproject.abstractMath.Metric;
import mlproject.abstractMath.VectorMaker;
import mlproject.abstractMath.vectorMaker.ColorHistogramVectorMaker;
import mlproject.abstractMath.vectorMaker.NaiveVectorMaker;
import mlproject.models.Issue;

public class MinkowskiMetric implements Metric<Issue> {
	final VectorMaker<Issue> vectorMaker;
	final Double p;
	
	public MinkowskiMetric(VectorMaker<Issue> vectorMaker, Double p) {
		this.vectorMaker = vectorMaker;
		this.p = p;
	}
	
	@Override
	public double distance(Issue t1, Issue t2) {
		VectorMaker<Issue> maker = new ColorHistogramVectorMaker();
		return DoubleVectorUtils.minkowskiDistance(maker.toVector(t1), maker.toVector(t2), p);
	}
}
