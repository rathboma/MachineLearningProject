package mlproject.predictors;

import java.util.Collection;

import mlproject.models.Issue;

public class ExpectedSalesPredictor extends BasePredictor {

	@Override
	public double Predict(Issue issue) {
		return 0;
	}

	@Override
	public void Train(Collection<Issue> issues) {}

	@Override
	public String name() {
		return "Expected Sales Predictor";
	}

}
