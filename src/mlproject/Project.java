package mlproject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import mlproject.abstractMath.impl.EuclideanMetric;
import mlproject.abstractMath.impl.NaiveVectorMaker;
import mlproject.abstractMath.impl.WeightedVectorMaker;
import mlproject.dataimport.Importer;
import mlproject.models.Issue;
import mlproject.predictors.ExpectedSalesPredictor;
import mlproject.predictors.KNearestNeighbour;
import mlproject.testing.DataLoader;
import mlproject.testing.TestResults;



public class Project {
	
	public static void main(String[] args){	
		//for(Field f: Issue.class.getDeclaredFields())
		//System.out.println(Issue.class.getDeclaredFields().length);
		
		Collection<Issue> issues = null;
		try {
			System.out.println("Loading issues from csv....");
			
			issues = Importer.getIssues("/Users/matthew/Downloads/Consolidated.csv");
			File[] images = Importer.getImages("/Users/matthew/Pictures/cover_images/");

			//issues = Importer.getIssues("/home/mes592/Desktop/Consolidated.csv");
			//File[] images = Importer.getImages("/home/mes592/images/cover_images/");

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
		KNearestNeighbour knn = new KNearestNeighbour(new EuclideanMetric(new NaiveVectorMaker()), 10); 
		KNearestNeighbour knnW = new KNearestNeighbour(new EuclideanMetric(new WeightedVectorMaker()), 10);
		ExpectedSalesPredictor expSales = new ExpectedSalesPredictor();
		
		
		
		TestResults tester = new TestResults();
		System.out.println("10 nearest neighbor loss: " + tester.testPredictor(knn, loader));
		System.out.println("10 nearest neighbor weighted loss: " + tester.testPredictor(knnW, loader));
		System.out.println("current loss: " + tester.testPredictor(expSales, loader));
	}
	
}