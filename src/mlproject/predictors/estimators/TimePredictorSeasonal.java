package mlproject.predictors.estimators;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
		if (!predictionCache.containsKey(issue)) {
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
		
			predictionCache.put(issue, logSalesPrediction);
		}
		
		return predictionCache.get(issue);

	}
	
	Map<Issue, Double> predictionCache = new HashMap<Issue, Double>();

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
