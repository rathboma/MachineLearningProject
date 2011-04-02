package mlproject;

import java.util.Collection;
import java.util.Date;

import mlproject.abstractMath.VectorMaker;
import mlproject.abstractMath.vectorMaker.AverageColorVectorMaker;
import mlproject.models.Issue;
import mlproject.predictors.SumOfGaussianPredictor;

public class ProjectFindBestColor {
	
	final static int COLOR_HOPPER = 3;
	
	public static void main(String[] args){
		Collection<Issue> issues = Project.loadIssues();
		
		VectorMaker<Issue> vm = new AverageColorVectorMaker();
		ISalesPredictor predictor = new SumOfGaussianPredictor(vm, 0.5);
		
		predictor.Train(issues);
    
		int colorGap = (int) Math.pow(2, COLOR_HOPPER);
		
		int bestRed = 0;
		int bestGreen = 0;
		int bestBlue = 0;
		double bestResult = Double.NEGATIVE_INFINITY;
		for(int red = 0; red < 256; red+=colorGap) {
			for(int green = 0; green < 256; green+=colorGap) {
				for(int blue = 0; blue < 256; blue+=colorGap) {
					Issue predictMe = new Issue();
					predictMe.date = new Date(System.currentTimeMillis());
					predictMe.setColors(((double)red) / 256, ((double)green) / 256, ((double)blue) / 256);
					
			        double result = predictor.Predict(predictMe);
			        System.out.println("("+ red + "," + green + "," + blue + ") result " + result);
			        
			        if (result > bestResult) {
			        	bestRed = red;
			        	bestGreen = green;
			        	bestBlue = blue;
			        	bestResult = result;
			        }
				}
			}
		}
		
		System.out.println("*************************");
		System.out.println("Best Color");
		System.out.println("Red: " + bestRed);
		System.out.println("Green: " + bestGreen);
		System.out.println("Blue: " + bestBlue);
		System.out.println("Result: " + bestResult);
	}
	
}