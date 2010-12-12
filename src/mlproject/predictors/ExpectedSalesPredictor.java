package mlproject.predictors;

import java.util.Collection;

import mlproject.models.Issue;

public class ExpectedSalesPredictor extends BasePredictor {

	@Override
	public double Predict(Issue issue) {
		return 1;
<<<<<<< HEAD
=======
		//return (issue.expectedSales == null)? 36041: issue.expectedSales;
>>>>>>> f253d043385b024aac942b04e2c49232f91c87d2
	}

	@Override
	public void Train(Collection<Issue> issues) {}

	@Override
	public String name() {
		return "Expected Sales Predictor";
	}

}
