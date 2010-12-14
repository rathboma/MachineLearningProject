package mlproject.predictors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import mlproject.abstractMath.DoubleVectorUtils;
import mlproject.abstractMath.VectorMaker;
import mlproject.abstractMath.impl.WeightedVectorMaker;
import mlproject.models.Issue;

public class KMeansPredictor extends BasePredictor {

	public int k;
	
	Double[][] prototypes;
	Double[] prototypePredictions;
	VectorMaker<Issue> vectorMaker;
	
	public KMeansPredictor(int k, VectorMaker maker){
		this.k = k;
		this.vectorMaker = maker;
	}
	
	private boolean converged(Double[][] r, Double[][] r2){
		double sum = 0;
		for(int i = 0; i < r.length; i++){
			sum += DoubleVectorUtils.dist(r[i], r2[i]);
		}
		System.out.println("checking convergence, difference : " + sum);
		return sum == 0;
	}
	
	// Initializes the prototype array to the first k elements of the issues array
	private Double[][] initializePrototypes(Double[][] issues, int num){
		if(issues.length < num ) return null;
		
		Double[][] results = new Double[num][issues[0].length];
		for(int i = 0; i < num; i++){
			
			for(int j = 0; j < issues[i].length; j++ ){
				results[i][j] = new Double(issues[i][j].doubleValue());
			}
		}
		return results;
	}
	
	private void recomputePrototypes(Double[][] issues, Double[][] responsibilities, Issue[] issueObjects){
		for(int i = 0; i < responsibilities.length; i++){
			prototypes[i] = new Double[prototypes[i].length];
			prototypePredictions[i] = 0.0;
			double number = DoubleVectorUtils.sum(responsibilities[i]);
			for(int j = 0; j < responsibilities[i].length; j++){
				double respon = responsibilities[i][j].doubleValue();
				if(respon == 1.0) {
					Double[] issue = issues[j];
					prototypes[i] = DoubleVectorUtils.add(prototypes[i], issue);
					prototypePredictions[i] += issueObjects[j].getPercent(); 
				}	
			} // end j
			DoubleVectorUtils.divideAll(prototypes[i], number);
			prototypePredictions[i] /= number;
		}
	}
	
	@Override
	public void Train(Collection<Issue> issues) {
		VectorMaker<Issue> maker = vectorMaker;
		Issue[] allIssues = new Issue[issues.size()];
		
		allIssues = issues.toArray(allIssues);
		Double[][] issueVectors = new Double[issues.size()][maker.vectorSize()];
		for(int i = 0; i < issues.size(); i++){
			issueVectors[i] = maker.toVector(allIssues[i]);
		}
		
		prototypes = initializePrototypes(issueVectors, k); //copy some issue
		prototypePredictions = new Double[k];
		Double[][] responsibilities = new Double[k][issues.size()];
		Double[][] lastR = new Double[k][issues.size()];
		
		
		boolean firstLoop = true;
		while( firstLoop || !converged(responsibilities, lastR)){
			lastR = responsibilities;
			responsibilities = new Double[k][issues.size()];
			for(int i = 0; i < issueVectors.length; i++){
				int closest = 0;
				double closestDistance = -1;
				for(int j = 0; j < prototypes.length; j++){
					responsibilities[j][i] = 0.0;
					double distance = DoubleVectorUtils.dist(issueVectors[i], prototypes[j]); 
					if(closestDistance == -1 || distance < closestDistance){
						closest = j;
						closestDistance = distance;
					}
				}
				responsibilities[closest][i] = 1.0;
			}
			recomputePrototypes(issueVectors, responsibilities, allIssues);
			
			
			//for all issues i
				// for all prototypes j 
					// responsibility(i, j) = 1 if distance(i, j) = min, else 0
				//
			//
			//for each prototype recompute so that it is the average of all the assigned samples
			
			
			firstLoop = false;
		}
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public double Predict(Issue issue) {
		VectorMaker<Issue> maker = vectorMaker;
		Double[] issueVector = maker.toVector(issue);
		int closest = -1;
		double closestValue = 0;
		for(int i = 0; i < prototypes.length; i++){
			double distance = DoubleVectorUtils.dist(prototypes[i], issueVector);
			if(closest == -1 || distance < closestValue){
				closest = i;
				closestValue = distance;
			}
		}
		System.out.println("prediction using prototype " + closest);
		// TODO Auto-generated method stub
		return prototypePredictions[closest];
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return null;
	}

}
