package mlproject.predictors;

import java.util.Collection;

import mlproject.ISalesPredictor;
import mlproject.models.Issue;


public abstract class BasePredictor implements ISalesPredictor {
	
	final ISalesPredictor timeEstimator;
	
	public BasePredictor(ISalesPredictor timeEstimator) {
		this.timeEstimator = timeEstimator;
	}
	
	@Override
	public void Train(Collection<Issue> issues) {
		timeEstimator.Train(issues);
		trainPredictor(issues);
	}
	
	public abstract void trainPredictor(Collection<Issue> issues);
	
	
}



