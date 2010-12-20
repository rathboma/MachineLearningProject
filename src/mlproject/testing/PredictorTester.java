package mlproject.testing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import mlproject.ISalesPredictor;
import mlproject.models.Issue;

public class PredictorTester{
	
	/**
	 * @param predictor
	 * @param issues
	 * @param trainingSize
	 * @param testSize
	 * @return the average loss for this test run.
	 */
	public Map<DataSetType, BatchPredictionResults> testPredictor(ISalesPredictor predictor, DataLoader loader) {
		
		System.out.println("Testing Predictor " + predictor.name());
		
		Collection<Issue> all = loader.getAll();
		
		
		//for each issue in all, train the predictor on everything else, then test against that one, then add to loss
		
		Map<DataSetType, BatchPredictionResults> results =
			new HashMap<DataSetType, BatchPredictionResults>();
		
		results.put(DataSetType.TEST, tryOnSample(predictor, all));
		
		return results;
	}
	
	private BatchPredictionResults tryOnSample(ISalesPredictor predictor, Collection<Issue> everything) {
		
		double totalLoss = 0;
		int correctDirection = 0;
		
		ArrayList<Issue> everythingClone = new ArrayList<Issue>();
		for(Issue issue : everything){
			everythingClone.add(issue);
		}
		//for each issue
		//	remove it from the training set
		//	train
		//	test on that one issue
		//  add to total loss & correct direction
		
		for(Issue testSample : everything){
			System.out.print('.');
			everythingClone.remove(testSample);
			predictor.Train(everythingClone);
			double logPercentPrediction = predictor.Predict(testSample);
			double actualLogPercent = testSample.getLogPercent();
			if ((logPercentPrediction >= 0) == (actualLogPercent >= 0)) correctDirection++;
			double loss = Math.pow(logPercentPrediction - actualLogPercent, 2);
			totalLoss += loss;
			//now it has everything in it again
			everythingClone.add(testSample);
		}	
		System.out.println();
		
		BatchPredictionResults testResults = new BatchPredictionResults();
		testResults.averageLoss = totalLoss/everything.size();
		testResults.numCorrectDirection = correctDirection;
		testResults.totalChecked = everything.size();
		return testResults;
	}
}