package mlproject.predictors;

import java.util.Collection;

import mlproject.models.Issue;

public class OldExpectedSalesPredictor extends BasePredictor {

	@Override
	public double Predict(Issue issue) {
		double oldExpectedSales = issue.getOldExpectedSales();
		return Math.log(oldExpectedSales / issue.getExpectedSales());
	}

	@Override
	public void Train(Collection<Issue> issues) {}

	@Override
	public String name() {
		return "Old Expected Sales Predictor";
	}

}
