package mlproject.predictors;

import java.util.Collection;

import mlproject.models.Issue;

public class ExpectedSalesPredictor extends BasePredictor {

	@Override
	public double Predict(Issue issue) {
		return 1;
		//return (issue.expectedSales == null)? 36041: issue.expectedSales;
	}

	@Override
	public void Train(Collection<Issue> issues) {}

}
