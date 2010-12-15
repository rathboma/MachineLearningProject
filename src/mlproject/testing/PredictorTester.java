package mlproject.testing;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
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
	public Map<DataSetType, BatchPredictionResults> testPredictor(ISalesPredictor predictor, DataLoader loader) {
		
		Collection<Issue> trainingSample = loader.getTrainingData();
		Collection<Issue> testSample = loader.getTestData();
		predictor.Train(trainingSample);
		
		Map<DataSetType, BatchPredictionResults> results =
			new HashMap<DataSetType, BatchPredictionResults>();
		
		results.put(DataSetType.TRAINING, tryOnSample(predictor, trainingSample));
		results.put(DataSetType.TEST, tryOnSample(predictor, testSample));
		
		return results;
	}
	
	private BatchPredictionResults tryOnSample(ISalesPredictor trainedPredictor, Collection<Issue> samples) {
		
		double totalLoss = 0;
		int correctDirection = 0;
		for(Issue sample: samples) {
			double logPercentPrediction = trainedPredictor.Predict(sample);
			double actualLogPercent = sample.getLogPercent();
			
			if ((logPercentPrediction >= 0) == (actualLogPercent >= 0)) correctDirection++;
			double loss = Math.pow(logPercentPrediction - actualLogPercent, 2);
			
			//System.out.println("actual percent: " + sample.getLogPercent() + " predicted: " + logPercentPrediction);
			
			totalLoss += loss;
		}	
		
		BatchPredictionResults testResults = new BatchPredictionResults();
		testResults.averageLoss = totalLoss/samples.size();
		testResults.numCorrectDirection = correctDirection;
		testResults.totalChecked = samples.size();
		return testResults;
	}
}