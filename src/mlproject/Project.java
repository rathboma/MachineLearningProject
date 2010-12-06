package mlproject;

import java.io.IOException;
import java.util.Collection;

import mlproject.abstractMath.impl.EuclideanMeasure;
import mlproject.abstractMath.impl.EuclideanMeasureWeighted;
import mlproject.dataimport.Importer;
import mlproject.models.*;
import mlproject.predictors.*;
import mlproject.testing.*;



public class Project {
	
	public static void main(String[] args){		
		Collection<Issue> issues = null;
		try {
			issues = Importer.getIssues("/home/mes592/Desktop/Consolidated.csv");
			for(Issue issue: issues) {
				System.out.println(issue.Issue);
				System.out.println(issue.date);
				System.out.println(issue.heading);
				System.out.println(issue.astronomyAndCosmology? "*****************": "Not astonomy");
				System.out.println("");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		KNearestNeighbour knn = new KNearestNeighbour(new EuclideanMeasure(), 10); 
		KNearestNeighbour knnW = new KNearestNeighbour(new EuclideanMeasureWeighted(), 10);
		ExpectedSalesPredictor expSales = new ExpectedSalesPredictor();
		
		TestResults tester = new TestResults();
		System.out.println("10 nearest neighbor loss: " + tester.testPredictor(knn, issues, 200, 50));
		System.out.println("10 nearest neighbor weighted loss: " + tester.testPredictor(knnW, issues, 200, 50));
		System.out.println("current loss: " + tester.testPredictor(expSales, issues, 200, 50));
	}
	
}