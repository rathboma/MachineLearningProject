package mlproject.predictors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mlproject.ISalesPredictor;
import mlproject.abstractMath.Metric;
import mlproject.abstractMath.NaturalNearestNeighbor;
import mlproject.abstractMath.NearestNeighborFunction;
import mlproject.models.Issue;



public class KNearestNeighbour extends BasePredictor{

	final public NearestNeighborFunction<Issue> nnf;
	final public int k;
	final public String id;
	
	private Collection<Issue> issues = new ArrayList<Issue>();
	
	public KNearestNeighbour(NearestNeighborFunction<Issue> nnf, int k, String id, ISalesPredictor timeEstimator) {
		super(timeEstimator);
		this.nnf = nnf;
		this.k = k;
		this.id = id;
	}
	
	public KNearestNeighbour(Metric<Issue> measure, int k, String id, ISalesPredictor timeEstimator) {
		super(timeEstimator);
		this.nnf = new NaturalNearestNeighbor<Issue>(measure);
		this.k = k;
		this.id = id;
	}
	
	@Override
	public double Predict(Issue issue) {
		List<Issue> neighbors = nnf.nearestNeighbors(k, issue, issues);
		
		double totalPercent = 0;
		for(Issue neighbor: neighbors) {
			totalPercent += Math.log(neighbor.sales) - timeEstimator.Predict(neighbor);
		}
		
		return totalPercent / neighbors.size() ;
	}

	@Override
	public void trainPredictor(Collection<Issue> issues) {
		this.issues = issues;
	}

	@Override
	public String name() {
		return "K-Nearest-Neighbor, K = " + k + ", " + id;
	}
	
	
}