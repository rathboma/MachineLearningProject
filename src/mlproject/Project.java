package mlproject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mlproject.abstractMath.VectorMaker;
import mlproject.abstractMath.vectorMaker.AverageColorVectorMaker;
import mlproject.abstractMath.vectorMaker.PolynomialVectorMaker;
import mlproject.dataimport.Importer;
import mlproject.models.Issue;
import mlproject.predictors.ExpectedSalesPredictor;
import mlproject.predictors.LinearRegressionPredictor;
import mlproject.testing.BatchPredictionResults;
import mlproject.testing.DataLoader;
import mlproject.testing.DataSetType;
import mlproject.testing.PredictorTester;

public class Project {
	
	public static void main(String[] args){
		Collection<Issue> issues = loadIssues();
		
//		try{
//			File f = new File("/Users/matthew/mapping.txt");
//			BufferedWriter o = new BufferedWriter(new FileWriter(f));
//			for(Issue issue: issues){
//				o.write(issue.imageFile + " " + issue.getDirection() + "\n");
//			}
//			o.close();
//		}catch(Exception e){}
//
//		if(true) return;
//		
		
		DataLoader loader = new DataLoader(issues, 10); //% test samples.

		//Just testing
		PolynomialVectorMaker<Issue> pvm = new PolynomialVectorMaker<Issue>(3, new AverageColorVectorMaker());
		LinearRegressionPredictor lpredictor = new LinearRegressionPredictor(0.2, pvm);
		lpredictor.Train(issues);
		pvm.printOrder();
		System.out.println(Arrays.toString(lpredictor.getCoefficients()));
		
		

		//Remove this to actually run things
		//if (true) return;
		
		List<ISalesPredictor> fastPredictors = getFastPredictors();
		System.out.println("fetched " + fastPredictors.size() + " fast predictor combinations");
		
		List<ISalesPredictor> slowPredictors = getSlowPredictors();
		System.out.println("fetched " + slowPredictors.size() + " slow predictor combinations");
		
		PredictorTester tester = new PredictorTester();
		
		final Map<ISalesPredictor, Map<DataSetType, BatchPredictionResults>> results = 
			new HashMap<ISalesPredictor, Map<DataSetType, BatchPredictionResults>>();
		
		System.out.println("Testing Fast Predictors");
		for(ISalesPredictor predictor: fastPredictors) {
			results.put(predictor, tester.testPredictor(predictor, loader));
		}

		System.out.println("Testing Slow Predictors");
		for(ISalesPredictor predictor: slowPredictors) {
			results.put(predictor, tester.testPredictorShort(predictor, loader, 50));
		}
		
		List<ISalesPredictor> allPredictors = new ArrayList<ISalesPredictor>();
		allPredictors.addAll(fastPredictors);
		allPredictors.addAll(slowPredictors);		

		//Print out the results.
		for(ISalesPredictor predictor: allPredictors) {
			Map<DataSetType, BatchPredictionResults> thisResult = results.get(predictor);
			System.out.println(predictor.name());
			for(DataSetType dst: thisResult.keySet()) {
				System.out.println(dst.toString() + " data: ");
				System.out.println(" - Average Loss = " + thisResult.get(dst).averageLoss);
				System.out.println(" - Direction Success: " + thisResult.get(dst).numCorrectDirection + " / " + thisResult.get(dst).totalChecked);
			}
			System.out.println("");
		}

		Collections.sort(allPredictors, new Comparator<ISalesPredictor>() {
			@Override public int compare(ISalesPredictor p1, ISalesPredictor p2) {
				double l1 = results.get(p1).get(DataSetType.TEST).averageLoss;
				double l2 = results.get(p2).get(DataSetType.TEST).averageLoss;
				return Utils.sign(l1 - l2);
			}
		});
	
		System.out.println("Predictors in Order of Average Loss");
		for(ISalesPredictor predictor: allPredictors) {
			System.out.println(predictor.name() + " (" + results.get(predictor).get(DataSetType.TEST).averageLoss + ")");
		}
		System.out.println("");
		
		Collections.sort(allPredictors, new Comparator<ISalesPredictor>() {
			@Override public int compare(ISalesPredictor p1, ISalesPredictor p2) {
				double l1 = results.get(p1).get(DataSetType.TEST).directionalSuccessRate();
				double l2 = results.get(p2).get(DataSetType.TEST).directionalSuccessRate();
				return Utils.sign(l2 - l1);
			}
		});
		
		System.out.println("Predictors in Order of Directional Success");
		for(ISalesPredictor predictor: allPredictors) {
			System.out.println(predictor.name() + " (" + results.get(predictor).get(DataSetType.TEST).directionalSuccessRate() + ")");
		}
		System.out.println("");
	}

	private static Collection<Issue> loadIssues() {
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
			}
			System.out.println();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return issues;
	}
	
	private static List<ISalesPredictor> getSlowPredictors() {
		List<VectorMaker<Issue>> vectorMakers = VectorMakerLists.getSlowVMs();
		List<ISalesPredictor> slowPredictors = new ArrayList<ISalesPredictor>();
		
		return slowPredictors; //Return nothing
		/*
		double[] ridges = {0.2, 0.1, .05, .02};
		
		for(VectorMaker<Issue> vectorMaker: vectorMakers) {
			for(int k = 2; k < 5; k++) {
				slowPredictors.add(new KMeansPredictor(k, vectorMaker, "VectorMaker: " + vectorMaker.name()));
				slowPredictors.add(new KNearestNeighbour(new EuclideanMetric(vectorMaker), k,""));
			}

			for(double ridge : ridges){
				//slowPredictors.add(new LogisticRegressionPredictor(ridge, vectorMaker));
				slowPredictors.add(new LinearRegressionPredictor(ridge, vectorMaker));
			}

		}
		
		return slowPredictors;*/
	}

	public static List<ISalesPredictor> getFastPredictors() {
		List<VectorMaker<Issue>> vectorMakers = VectorMakerLists.getBaseVMs();

		List<ISalesPredictor> fastPredictors = new ArrayList<ISalesPredictor>();
		
		//double[] ridges = {0.5, 0.2, 0.1, 0.01, 0.001};
		
		/*for(VectorMaker<Issue> vectorMaker: vectorMakers) {

			for(int k = 2; k < 5; k++) {
				fastPredictors.add(new KMeansPredictor(k, vectorMaker, "VectorMaker: " + vectorMaker.name()));
				fastPredictors.add(new KNearestNeighbour(new EuclideanMetric(vectorMaker), k, "Euclidean"));
				fastPredictors.add(new KNearestNeighbour(new MinkowskiMetric(vectorMaker, 1.9), k, "Minkowski 1.9"));
				fastPredictors.add(new KNearestNeighbour(new MinkowskiMetric(vectorMaker, 2.1), k, "Minkowski 2.1"));
			}
			
			for(double ridge : ridges){
				fastPredictors.add(new LogisticRegressionPredictor(ridge, vectorMaker));
				fastPredictors.add(new LinearRegressionPredictor(ridge, vectorMaker));
			}
		}*/
		
		fastPredictors.add(new LinearRegressionPredictor(0.2, 
			new PolynomialVectorMaker<Issue>(3, new AverageColorVectorMaker())));
	
		fastPredictors.add(new ExpectedSalesPredictor());
		return fastPredictors;

	}
	
}