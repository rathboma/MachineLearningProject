package mlproject.predictors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mlproject.abstractMath.NaturalNearestNeighbor;
import mlproject.abstractMath.NearestNeighborFunction;
import mlproject.abstractMath.VectorMaker;
import mlproject.abstractMath.impl.MinkowskiMetric;
import mlproject.models.Issue;

/**
 * In this case, we are going to call K-nearest neighbor with
 * a Minkowski metric, and try to learn the hyperparameter p
 * of the metric.
 * @author mes592
 *
 */
public class KNearestNeighborHyper extends BasePredictor {

	final private VectorMaker<Issue> vectorMaker;
	private Collection<Issue> issues;
	private double minkowskiParam;
	final private int k;
	
	public KNearestNeighborHyper(VectorMaker<Issue> vectorMaker, int k) {
		this.vectorMaker = vectorMaker;
		this.k = k;
	}
	
	@Override
	public double Predict(Issue issue) {
		NearestNeighborFunction<Issue> nnf = 
			new NaturalNearestNeighbor<Issue>(new MinkowskiMetric(vectorMaker, minkowskiParam));
		
		List<Issue> neighbors = nnf.nearestNeighbors(k, issue, issues);
		
		double totalPercent = 0;
		for(Issue neighbor: neighbors) {
			totalPercent += neighbor.getLogPercent();
		}
		
		return totalPercent / neighbors.size();
	}

	@Override
	public void Train(Collection<Issue> issues) {
		this.issues = issues;		
		this.minkowskiParam = trainParam(issues, 2, .2); 		
	}
	
	/**
	 * @param issues
	 * @param param
	 * @param paramStep
	 * Sort of a gradient descent on the param
	 */
	private double trainParam(Collection<Issue> issues, double param, double paramStep) {
		System.out.println("Training with p = " + param + " step = " + paramStep);
		
		if (paramStep < 0.01) return param; //Converged
		
		double currentLoss = getLossForParam(param, issues);

		int bestLossIndex = 0; 
		double bestLoss = Double.MAX_VALUE;
		
		for(int i = 0; i <= 10; i++) {
			double lossForParam = param + (i - 5)*paramStep;
			
			if (lossForParam < bestLoss) {
				bestLoss = lossForParam;
				bestLossIndex = i;
			}
		}
		
		double newParam = param + (bestLossIndex - 5)*paramStep;
		
		return trainParam(issues, newParam, paramStep / 10);
	}
	
	private double getLossForParam(double param, Collection<Issue> issues) {

		KNearestNeighbour knn = new KNearestNeighbour(new MinkowskiMetric(vectorMaker, param), k, "minkowski " +param);
		double totalLoss = 0.0;
		for(Issue leftOut: issues) {
			Collection<Issue> cloneIssues = new ArrayList<Issue>();
			cloneIssues.addAll(issues);
			cloneIssues.remove(leftOut);
			
			knn.Train(cloneIssues);
			double predictedSales = knn.Predict(leftOut);
			double actualSales = leftOut.getLogPercent();
			double loss = Math.pow(predictedSales - actualSales, 2);
			totalLoss += loss;
		}
		
		return totalLoss / issues.size();
	}

	@Override
	public String name() {
		return "KNN learn Minkowski: Vector maker " + vectorMaker.name();
	}

}
