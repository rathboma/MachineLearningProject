package mlproject.testing;

import java.util.Collection;
import java.util.Random;

import mlproject.ISalesPredictor;
import mlproject.models.Issue;

public class PredictorTester{
	
	private Random generator = new Random();
	
	/**
	 * @param predictor
	 * @param issues
	 * @param trainingSize
	 * @param testSize
	 * @return the average loss for this test run.
	 */
	public TestResults testPredictor(ISalesPredictor predictor, DataLoader loader) {
		
		Collection<Issue> trainingSample = loader.getTrainingData();
		Collection<Issue> testSample = loader.getTestData();
		predictor.Train(trainingSample);
		
		double totalLoss = 0;
		int correctDirection = 0;
		for(Issue issue: testSample) {
			double percentPrediction = predictor.Predict(issue);
			double actualPercent = issue.getPercent();
			//let it treat 1.0 as a positive prediction I guess.
			percentPrediction = percentPrediction == 1.00 ? 1.0001 : percentPrediction;
			if(Math.signum(1- percentPrediction) == Math.signum(1 - actualPercent)) correctDirection++;
			double loss = Math.abs(percentPrediction - actualPercent);
			System.out.println("actual percent " + issue.getPercent() + " predicted: " + percentPrediction);
			totalLoss += loss;
		}	
		
		TestResults testResults = new TestResults();
		testResults.averageLoss = totalLoss/testSample.size();
		testResults.numCorrectDirection = correctDirection;
		testResults.totalChecked = testSample.size();
		
		return testResults;
	}	
}