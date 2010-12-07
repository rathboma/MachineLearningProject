package mlproject.predictors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mlproject.abstractMath.Metric;
import mlproject.abstractMath.NaturalNearestNeighbor;
import mlproject.abstractMath.NearestNeighborFunction;
import mlproject.models.Issue;



public class KNearestNeighbour extends BasePredictor{

	final public NearestNeighborFunction<Issue> nnf;
	final public int k;
	
	private Collection<Issue> issues = new ArrayList<Issue>();
	
	public KNearestNeighbour(NearestNeighborFunction<Issue> nnf, int k) {
		this.nnf = nnf;
		this.k = k;
	}
	
	public KNearestNeighbour(Metric<Issue> measure, int k) {
		this.nnf = new NaturalNearestNeighbor<Issue>(measure);
		this.k = k;
	}
	
	@Override
	public double Predict(Issue issue) {
		List<Issue> neighbors = nnf.nearestNeighbors(k, issue, issues);
		
		double totalSales = 0;
		for(Issue neighbor: neighbors) {
			totalSales += neighbor.sales;
		}
		
		return totalSales / neighbors.size() ;
	}

	@Override
	public void Train(Collection<Issue> issues) {
		this.issues = issues;
	}
	
	
}