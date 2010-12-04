package mlproject.testing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.Set;

import mlproject.ISalesPredictor;
import mlproject.models.Issue;

public class TestResults{
	
	private Random generator = new Random();
	
	/**
	 * @param predictor
	 * @param issues
	 * @param trainingSize
	 * @param testSize
	 * @return the average loss for this test run.
	 */
	public double testPredictor(ISalesPredictor predictor, Collection<Issue> issues, int trainingSize, int testSize) {
		Collection<Issue> trainingSample = generateRandomSample(trainingSize, issues);
		Collection<Issue> testSample = generateRandomSample(testSize, issues);
		
		predictor.Train(trainingSample);
		
		double totalLoss = 0;
		for(Issue issue: testSample) {
			double salesPrediction = predictor.Predict(issue);
			double actualSales = issue.sales;
			
			double loss = Math.abs(Math.log(salesPrediction) - Math.log(actualSales));
			totalLoss += loss;
		}
		
		double averageLoss = totalLoss/testSample.size();
		return averageLoss;
	}
	
	public Collection<Issue> generateRandomSample(int n, Collection<Issue> issues) {
		Issue[] issueArray = issues.toArray(new Issue[issues.size()]);
		
		Collection<Issue> randomIssues = new ArrayList<Issue>(); 
		
		for(int i = 0; i < n; i++) {
			randomIssues.add(getRandomIssue(issueArray));
		}
		
		return randomIssues;
	}
	
	public Issue getRandomIssue(Issue[] issues) {
		return issues[generator.nextInt(issues.length)];
	}
	
}