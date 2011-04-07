package mlproject.predictors.estimators;

import java.util.Collection;

import mlproject.ISalesPredictor;
import mlproject.abstractMath.PolynomialFitter;
import mlproject.abstractMath.PolynomialFitter.Polynomial;
import mlproject.models.Issue;

public class TimePolynomialEstimator implements ISalesPredictor {

	public final int degree;
	Collection<Issue> issues;
	public Polynomial bestFit;
	
	public TimePolynomialEstimator(int degree) {
		this.degree = degree;
	}
	
	@Override
	public double Predict(Issue issue) {

		double logSalesPrediction = bestFit.getY(issue.getTime());
		
		return logSalesPrediction;
	}

	@Override
	public void Train(Collection<Issue> issues) {
		this.issues = issues;
		
		PolynomialFitter polyFitter = new PolynomialFitter(degree);
		
		for(Issue anIssue: issues) {
			long thatTime = anIssue.getTime();
			polyFitter.addPoint(thatTime, anIssue.getLogSales());
		}
		
		bestFit = polyFitter.getBestFit();
	}

	@Override
	public String name() {
		return "Time Poly degree " + degree;
	}

}
