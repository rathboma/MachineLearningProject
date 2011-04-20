package mlproject.predictors;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mlproject.ISalesPredictor;
import mlproject.models.Issue;

public class WeightedMajorityPredictor extends BasePredictor {
	public final double mover = 0.01;
	
	public final double beta;
	public final List<ISalesPredictor> experts;
	public final Map<ISalesPredictor, Integer> numWrong = new HashMap<ISalesPredictor, Integer>();
	
	public WeightedMajorityPredictor(ISalesPredictor timeEstimator, double beta, List<ISalesPredictor> experts) {
		super(timeEstimator);
		this.beta = beta;
		this.experts = experts;
	}

	@Override
	public void trainPredictor(Collection<Issue> issues) {
		for(ISalesPredictor expert: experts) {
			expert.Train(issues);
			int wrong = 0;
			for(Issue issue: issues) {
				double estimated = timeEstimator.Predict(issue);
				double expertPrediction = expert.Predict(issue);
				double actual = issue.getLogSales();
				
				//Is the estimated and expert prediction on the same side as actual?
				boolean isCorrect = (estimated - actual) * (expertPrediction - actual) > 0;
				if (!isCorrect) wrong++;
			}
			numWrong.put(expert, wrong);
		}
	}

	@Override
	public double Predict(Issue issue) {
		double totalVote = 0;
		for(ISalesPredictor expert: experts) {
			double expertPrediction = Math.signum(expert.Predict(issue) - timeEstimator.Predict(issue));
			double weight = Math.pow(beta, numWrong.get(expert));
			totalVote += expertPrediction*weight;
		}
		double sidePrediction = Math.signum(totalVote);
		return timeEstimator.Predict(issue) - sidePrediction*mover;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Weighted Majority, Beta = " + beta;
	}

}
