package mlproject.testing;

import java.util.ArrayList;
import java.util.Collection;

import mlproject.ISalesPredictor;
import mlproject.models.Issue;

public class SalesEstimateTester{
	
	/**
	 * @param predictor
	 * @param issues
	 * @param trainingSize
	 * @param testSize
	 * @return the average loss for this test run.
	 */
	public Double testPredictor(ISalesPredictor predictor, DataLoader loader) {
		
		System.out.println("Testing S " + predictor.name());
		
		Collection<Issue> all = loader.getAll();		
		
		return tryOnSample(predictor, all);
	}
	
	private Double tryOnSample(ISalesPredictor predictor, Collection<Issue> everything) {
		return tryOnSampleShort(predictor, everything, everything);
	}

	/**
	 * @param predictor
	 * @param loader
	 * @return Leave one analysis, returns average loss.
	 */
	private Double tryOnSampleShort(ISalesPredictor predictor, Collection<Issue> everything, Collection<Issue> testHoldouts) {
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
		
		for(Issue testSample : testHoldouts){
			System.out.print('.');
			everythingClone.remove(testSample);
			predictor.Train(everythingClone);
			double logSalesPrediction = predictor.Predict(testSample);
			double actualLogSales = testSample.getLogSales();
			double loss = Math.pow(logSalesPrediction - actualLogSales, 2);
			totalLoss += loss;
			//now it has everything in it again
			everythingClone.add(testSample);
		}
		System.out.println("");

		
		return totalLoss/testHoldouts.size();
	}


}