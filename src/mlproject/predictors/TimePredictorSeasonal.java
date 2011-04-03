package mlproject.predictors;

import java.util.Collection;

import mlproject.ISalesPredictor;
import mlproject.abstractMath.PolynomialFitter;
import mlproject.models.Issue;

public class TimePredictorSeasonal implements ISalesPredictor {

	private final TimePolynomialPredictor tpp = new TimePolynomialPredictor(2, null);
	final int sVarWindow;
	Collection<Issue> issues;
	
	public TimePredictorSeasonal(int degree, int sVarWindow) {
		this.sVarWindow = sVarWindow;
	}
	
	@Override
	public double Predict(Issue issue) {
		tpp.Predict(issue);
		
		long thisTime = issue.getTime();
		
		double totalVar = 0;
		int numIssues = 0;
		
		for(Issue anIssue: issues) {
			if ((anIssue.getDayOfYear() - issue.getDayOfYear()) % 365 > sVarWindow) continue;
			numIssues++;
			totalVar += anIssue.getLogSales() - tpp.Predict(anIssue);
		}
		
		double aveDeviation = totalVar / numIssues;
		System.out.println("AD: " + aveDeviation);

		double logSalesPrediction = tpp.Predict(issue) + aveDeviation;
		
		return logSalesPrediction - issue.getExpectedLogSales();
	}

	@Override
	public void Train(Collection<Issue> issues) {
		this.issues = issues;
		tpp.Train(issues);
	}

	@Override
	public String name() {
		return "Seasonal Predictor " + sVarWindow;
	}

}
