package mlproject.predictors.estimators;

import java.util.Collection;

import mlproject.ISalesPredictor;
import mlproject.models.Issue;

public class TimePredictorSeasonal implements ISalesPredictor {

	private final TimePolynomialEstimator tpp;
	final double wrappedOneOverVariance;
	Collection<Issue> issues;
	
	public TimePredictorSeasonal(int degree, double wrappedOneOverVariance) {
		tpp = new TimePolynomialEstimator(degree);
		this.wrappedOneOverVariance = wrappedOneOverVariance;
	}
	
	@Override
	public double Predict(Issue issue) {
		tpp.Predict(issue);
		
		long thisTime = issue.getTime();
		
		double totalSeasonalEffect = 0;
		double totalWeight = 0;
		
		for(Issue anotherIssue: issues) {
			long anotherTime = anotherIssue.getTime();
			double radianDifference = 2 * Math.PI * ((double)(thisTime - anotherTime)) / 365;
			
			double weight = Math.exp(wrappedOneOverVariance * Math.cos(radianDifference));

			totalWeight += weight;
			totalSeasonalEffect += (anotherIssue.getLogSales() - tpp.Predict(anotherIssue)) * weight;
		}
		
		double predictedSeasonalEffect = totalSeasonalEffect / totalWeight;

		double logSalesPrediction = tpp.Predict(issue) + predictedSeasonalEffect;
		
		return logSalesPrediction;
	}

	@Override
	public void Train(Collection<Issue> issues) {
		this.issues = issues;
		tpp.Train(issues);
	}

	@Override
	public String name() {
		return "Seasonal Predictor " + wrappedOneOverVariance + " deg " + tpp.degree;
	}

}
