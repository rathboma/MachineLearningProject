package mlproject.predictors;

import java.util.Collection;

import mlproject.ISalesPredictor;
import mlproject.models.Issue;

public class ExpectedSalesPredictor extends BasePredictor {

	public ExpectedSalesPredictor(ISalesPredictor timeEstimator) {
		super(timeEstimator);
	}

	@Override
	public double Predict(Issue issue) {
		return 0;
	}

	@Override
	public void trainPredictor(Collection<Issue> issues) {}

	@Override
	public String name() {
		return "Expected Sales Predictor";
	}

}
