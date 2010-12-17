package mlproject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mlproject.abstractMath.VectorMaker;
import mlproject.abstractMath.impl.EuclideanMetric;
import mlproject.abstractMath.impl.PolynomialVectorMaker;
import mlproject.abstractMath.impl.WeightedVectorMaker;
import mlproject.dataimport.Importer;
import mlproject.models.Issue;
import mlproject.predictors.ExpectedSalesPredictor;
import mlproject.predictors.KMeansPredictor;
import mlproject.predictors.KNearestNeighbour;
import mlproject.predictors.LinearRegressionPredictor;
import mlproject.testing.BatchPredictionResults;
import mlproject.testing.DataLoader;
import mlproject.testing.DataSetType;
import mlproject.testing.PredictorTester;

public class Project {
	
	public static void main(String[] args){		
		Collection<Issue> issues = null;
		try {
			System.out.println("Loading issues from csv....");
			
			File[] images = null;
			
			File testEnv = new File("/Users/matthew/");
			if (testEnv.exists()) {
				issues = Importer.getIssues("/Users/matthew/Downloads/Consolidated.csv");
				images = Importer.getImages("/Users/matthew/Pictures/cover_images/");
			} else {
				issues = Importer.getIssues("/home/mes592/Desktop/Consolidated.csv");
				images = Importer.getImages("/home/mes592/images/cover_images/");
			}

			HashMap<File, Date> dateMappings = Importer.extractIssueDates(images);
			System.out.println("Extracting image features...");
			for(Issue issue: issues) {
				System.out.print(".");
				for(File image : images){
					if(issue.shouldOwn(dateMappings.get(image))){
						try{
							issue.extractImageFeatures(image.getAbsolutePath());
							//System.out.println("RGB avg: " + issue.avgRed + " " + issue.avgGreen + " " + issue.avgBlue);
						}catch(IOException e){
							System.err.println("Unable to load data from " + image.getName() + " for issue " + issue.Issue );
						}
						break;
					}
					
				}
				
//				System.out.println(issue.Issue);
//				System.out.println(issue.date.toGMTString());
//				System.out.println(issue.heading);
//				System.out.println(issue.astronomyAndCosmology? "*****************": "Not astonomy");
//				System.out.println("");
			}
			System.out.println();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DataLoader loader = new DataLoader(issues, 30); //30% test samples.
// JUST FOR DEBUGGING
//		System.out.println("# of issues: " + issues.size());
//		System.out.println("# of test issues: " + loader.getTestData().size());
//		System.out.println("# of training issues: " + loader.getTrainingData().size());
//		
		List<VectorMaker<Issue>> vectorMakers = new ArrayList<VectorMaker<Issue>>();
		vectorMakers.add(new WeightedVectorMaker(false));
		//vectorMakers.add(new PolynomialVectorMaker<Issue>(0, new WeightedVectorMaker(false)));
		vectorMakers.add(new PolynomialVectorMaker<Issue>(1, new WeightedVectorMaker(false)));
		//vectorMakers.add(new PolynomialVectorMaker<Issue>(2, new WeightedVectorMaker(false)));
		//vectorMakers.add(new PolynomialVectorMaker<Issue>(3, new WeightedVectorMaker(false)));
		vectorMakers.add(new WeightedVectorMaker(true));
		//vectorMakers.add(new PolynomialVectorMaker<Issue>(0, new WeightedVectorMaker(true)));
		vectorMakers.add(new PolynomialVectorMaker<Issue>(1, new WeightedVectorMaker(true)));
		vectorMakers.add(new PolynomialVectorMaker<Issue>(2, new WeightedVectorMaker(true)));
		//vectorMakers.add(new PolynomialVectorMaker<Issue>(3, new WeightedVectorMaker(true)));

		List<ISalesPredictor> predictors = new ArrayList<ISalesPredictor>();
		
		for(VectorMaker<Issue> vectorMaker: vectorMakers) {
			for(int k = 2; k < 20; k+=2) {
				predictors.add(new KMeansPredictor(k, vectorMaker, "VectorMaker: " + vectorMaker.name()));
				//predictors.add(new KNearestNeighbour(new EuclideanMetric(vectorMaker), k));
			}
			
			predictors.add(new LinearRegressionPredictor(vectorMaker));
		}
		
		predictors.add(new ExpectedSalesPredictor());
		
		PredictorTester tester = new PredictorTester();
		
		final Map<ISalesPredictor, Map<DataSetType, BatchPredictionResults>> results = 
			new HashMap<ISalesPredictor, Map<DataSetType, BatchPredictionResults>>();
		
		for(ISalesPredictor predictor: predictors) {
			results.put(predictor, tester.testPredictor(predictor, loader));
		}

		//Print out the results.
		for(ISalesPredictor predictor: predictors) {
			Map<DataSetType, BatchPredictionResults> thisResult = results.get(predictor);
			System.out.println(predictor.name());
			for(DataSetType dst: thisResult.keySet()) {
				System.out.println(dst.toString() + " data: ");
				System.out.println(" - Average Loss = " + thisResult.get(dst).averageLoss);
				System.out.println(" - Direction Success: " + thisResult.get(dst).numCorrectDirection + " / " + thisResult.get(dst).totalChecked);
			}
			System.out.println("");
		}

		Collections.sort(predictors, new Comparator<ISalesPredictor>() {
			@Override public int compare(ISalesPredictor p1, ISalesPredictor p2) {
				double l1 = results.get(p1).get(DataSetType.TEST).averageLoss;
				double l2 = results.get(p2).get(DataSetType.TEST).averageLoss;
				return Utils.sign(l1 - l2);
			}
		});
	
		System.out.println("Predictors in Order of Average Loss");
		for(ISalesPredictor predictor: predictors) System.out.println(predictor.name());
		System.out.println("");
		
		Collections.sort(predictors, new Comparator<ISalesPredictor>() {
			@Override public int compare(ISalesPredictor p1, ISalesPredictor p2) {
				double l1 = results.get(p1).get(DataSetType.TEST).directionalSuccessRate();
				double l2 = results.get(p2).get(DataSetType.TEST).directionalSuccessRate();
				return Utils.sign(l2 - l1);
			}
		});
		
		System.out.println("Predictors in Order of Directional Success");
		for(ISalesPredictor predictor: predictors) System.out.println(predictor.name());
		System.out.println("");
	}
	
}