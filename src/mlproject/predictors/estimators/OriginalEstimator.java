package mlproject.predictors.estimators;

import java.util.Collection;

import mlproject.ISalesPredictor;
import mlproject.models.Issue;

public class OriginalEstimator implements ISalesPredictor {

	public OriginalEstimator() {
	}
	
	@Override
	public double Predict(Issue issue) {
		return issue.getExpectedLogSales();
	}

	@Override
	public void Train(Collection<Issue> issues) {
	}

	@Override
	public String name() {
		return "Original Expected ";
	}

}
