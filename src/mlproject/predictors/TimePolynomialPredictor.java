package mlproject.predictors;

import java.util.Collection;

import mlproject.ISalesPredictor;
import mlproject.abstractMath.PolynomialFitter;
import mlproject.models.Issue;

public class TimePolynomialPredictor implements ISalesPredictor {

	public final int degree;
	public final Integer previousDaysWindow;
	Collection<Issue> issues;
	
	public TimePolynomialPredictor(int degree, Integer previousDaysWindow) {
		this.degree = degree;
		this.previousDaysWindow = previousDaysWindow;
	}
	
	@Override
	public double Predict(Issue issue) {
		PolynomialFitter polyFitter = new PolynomialFitter(degree);
		long thisTime = issue.getTime();
		
		for(Issue anIssue: issues) {
			long thatTime = anIssue.getTime();
			if ((previousDaysWindow == null) || ((thatTime < thisTime) && (thatTime + previousDaysWindow > thisTime))) {
				polyFitter.addPoint(thatTime, anIssue.getLogSales());
			}
		}

		double logSalesPrediction = polyFitter.getBestFit().getY(issue.getTime());
		
		return logSalesPrediction - issue.getExpectedLogSales();
	}

	@Override
	public void Train(Collection<Issue> issues) {
		this.issues = issues;
	}

	@Override
	public String name() {
		if (previousDaysWindow == null)
			return "Time Poly degree " + degree;
		return "Time Poly degree " + degree + " window days " + previousDaysWindow;
	}

}
