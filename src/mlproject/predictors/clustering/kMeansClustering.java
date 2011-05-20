package mlproject.predictors.clustering;

import java.util.ArrayList;
import java.util.Random;

import mlproject.abstractMath.DoubleVectorUtils;

public class kMeansClustering {
	final public int k;
	final public long maxTurns;
	
	Double[][] prototypes;
	
	public kMeansClustering(int k, int maxTurns) {
		this.k = k;
		this.maxTurns = maxTurns;
	}
	
	public int getClosestPrototype(Double[] v) {
		return getClosestPrototype(v, prototypes);
	}
	
	public static int getClosestPrototype(Double[] v, Double[][] prototypes) {
		int closest = -1;
		double closestValue = 0;
		for(int i = 0; i < prototypes.length; i++){
			double distance = DoubleVectorUtils.dist(prototypes[i], v);
			if(closest == -1 || distance < closestValue){
				closest = i;
				closestValue = distance;
			}
		}
		return closest;		
	}
	
	public Double[] getPrototype(int i) {
		return prototypes[i];
	}
	
	public void computePrototypes(Double[][] dataSet) {
		prototypes = initializePrototypes(dataSet, k); //copy some issue
		//prototypePredictions = new Double[k];
		Double[][] responsibilities = new Double[k][dataSet.length];
		Double[][] lastR = new Double[k][dataSet.length];
		
		int turnNum = 0;
		while((turnNum==0) || !converged(responsibilities, lastR)){
			if (turnNum >= maxTurns) break;
			System.out.println("Making a k-means pass");
			lastR = responsibilities;
			responsibilities = new Double[k][dataSet.length];
			for(int i = 0; i < dataSet.length; i++){
				if (i%1000 == 0) System.out.println("Data point: " + i);
				int closest = 0;
				double closestDistance = -1;
				for(int j = 0; j < prototypes.length; j++){
					responsibilities[j][i] = 0.0;
					double distance = DoubleVectorUtils.dist(dataSet[i], prototypes[j]); 
					if(closestDistance == -1 || distance < closestDistance){
						closest = j;
						closestDistance = distance;
					}
				}
				responsibilities[closest][i] = 1.0;
			}
			//recomputePrototypes(dataSet, responsibilities, allIssues);
			recomputePrototypes(dataSet, responsibilities);
			
			
			//for all issues i
				// for all prototypes j 
					// responsibility(i, j) = 1 if distance(i, j) = min, else 0
				//
			//
			//for each prototype recompute so that it is the average of all the assigned samples
			
			
			turnNum++;
		}
	}
		
	private boolean converged(Double[][] r, Double[][] r2){
		double sum = 0;
		for(int i = 0; i < r.length; i++){
			sum += DoubleVectorUtils.dist(r[i], r2[i]);
		}
		//System.out.println("checking convergence, difference : " + sum);
		return sum == 0;
	}
		
	// Initializes the prototype array to the first k elements of the issues array
	private Double[][] initializePrototypes(Double[][] issues, int num){
		System.out.println("Init prototypes...");
		if(issues.length < num ) return null;
		ArrayList<Integer> alreadyFound = new ArrayList<Integer>();
		Double[][] results = new Double[num][issues[0].length];
		for(int i = 0; i < num; i++){
			System.out.println("Getting prototype " + i);
			Random r = new Random();
			int issueNum = -1;
			while(issueNum == -1 || alreadyFound.contains(issueNum)) 
				issueNum = Math.abs(r.nextInt()) % issues.length;
			alreadyFound.add(issueNum);
			for(int j = 0; j < issues[i].length; j++ ){
				Double[] issueVector = issues[issueNum];
				results[i][j] = issueVector[j];
			}
		}
		return results;
	}
		
	//private void recomputePrototypes(Double[][] issues, Double[][] responsibilities, Issue[] issueObjects){
	private void recomputePrototypes(Double[][] issues, Double[][] responsibilities){
		System.out.println("Recomputing...");
		for(int i = 0; i < responsibilities.length; i++){
			prototypes[i] = new Double[prototypes[i].length];
			//prototypePredictions[i] = 0.0;
			double number = DoubleVectorUtils.sum(responsibilities[i]);
			for(int j = 0; j < responsibilities[i].length; j++){
				double respon = responsibilities[i][j].doubleValue();
				if(respon == 1.0) {
					Double[] issue = issues[j];
					prototypes[i] = DoubleVectorUtils.add(prototypes[i], issue);
					
					//Issue issueObject = issueObjects[j];
					//prototypePredictions[i] += Math.log(issueObject.sales) - timeEstimator.Predict(issueObject); 
				}	
			} // end j
			DoubleVectorUtils.divideAll(prototypes[i], number);
			//prototypePredictions[i] /= number;
		}
	}

	
}
