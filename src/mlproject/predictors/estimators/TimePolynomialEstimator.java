package mlproject.predictors.estimators;

import java.util.Collection;

import mlproject.ISalesPredictor;
import mlproject.abstractMath.PolynomialFitter;
import mlproject.models.Issue;

public class TimePolynomialEstimator implements ISalesPredictor {

	public final int degree;
	Collection<Issue> issues;
	
	public TimePolynomialEstimator(int degree) {
		this.degree = degree;
	}
	
	@Override
	public double Predict(Issue issue) {
		PolynomialFitter polyFitter = new PolynomialFitter(degree);
		
		for(Issue anIssue: issues) {
			long thatTime = anIssue.getTime();
			polyFitter.addPoint(thatTime, anIssue.getLogSales());
		}

		double logSalesPrediction = polyFitter.getBestFit().getY(issue.getTime());
		
		return logSalesPrediction;
	}

	@Override
	public void Train(Collection<Issue> issues) {
		this.issues = issues;
	}

	@Override
	public String name() {
		return "Time Poly degree " + degree;
	}

}
