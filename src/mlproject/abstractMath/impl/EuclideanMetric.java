package mlproject.abstractMath.impl;

import mlproject.abstractMath.DoubleVectorUtils;
import mlproject.abstractMath.Metric;
import mlproject.abstractMath.VectorMaker;
import mlproject.abstractMath.vectorMaker.NaiveVectorMaker;
import mlproject.models.Issue;

public class EuclideanMetric implements Metric<Issue> {
	
	final VectorMaker<Issue> vectorMaker;
	
	public EuclideanMetric(VectorMaker<Issue> vectorMaker) {
		this.vectorMaker = vectorMaker;
	}
	
	@Override
	public double distance(Issue t1, Issue t2) {
		VectorMaker<Issue> maker = new NaiveVectorMaker();
		return DoubleVectorUtils.dist(maker.toVector(t1), maker.toVector(t2));
	}
}
