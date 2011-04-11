package mlproject.predictors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import mlproject.ISalesPredictor;
import mlproject.abstractMath.DoubleVectorUtils;
import mlproject.abstractMath.VectorMaker;
import mlproject.models.Issue;

public class KMeansPredictor extends BasePredictor {

	public int k;
	
	Double[][] prototypes;
	Double[] prototypePredictions;
	VectorMaker<Issue> vectorMaker;
	
	final String id;
	
	public KMeansPredictor(int k, VectorMaker<Issue> maker, String id, ISalesPredictor timeEstimator){
		super(timeEstimator);
		this.k = k;
		this.vectorMaker = maker;
		this.id = id;
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
		if(issues.length < num ) return null;
		ArrayList<Integer> alreadyFound = new ArrayList<Integer>();
		Double[][] results = new Double[num][issues[0].length];
		for(int i = 0; i < num; i++){
			Random r = new Random();
			int issueNum = -1;
			while(issueNum == -1 || alreadyFound.contains(issueNum)) 
				issueNum = Math.abs(r.nextInt()) % issues.length;
			alreadyFound.add(issueNum);
			for(int j = 0; j < issues[i].length; j++ ){
				results[i][j] = new Double(issues[issueNum][j].doubleValue());
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
					
					Issue issueObject = issueObjects[j];
					prototypePredictions[i] += Math.log(issueObject.sales) - timeEstimator.Predict(issueObject); 
				}	
			} // end j
			DoubleVectorUtils.divideAll(prototypes[i], number);
			prototypePredictions[i] /= number;
		}
	}
	
	@Override
	public void trainPredictor(Collection<Issue> issues) {		
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
		
		//Output
		if ((k == 3) && (vectorMaker.vectorSize() == 3)) {
			//System.out.println("Trained k=" +k + " means predictor");
			for(int i = 0; i < k; i++) {
				for(int j = 0; j < prototypes[i].length; j++) {
					double colorLogOdds = prototypes[i][j];
					double eToThat = Math.exp(colorLogOdds);
					double color = 256 * eToThat / (eToThat + 1);
					//System.out.print(color + " ");
				}
				//System.out.print("Predicts: " + prototypePredictions[i]);
				//System.out.println("\n");
			}
		}
		
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
		// TODO Auto-generated method stub
		return prototypePredictions[closest];
	}

	@Override
	public String name() {
		return "k-means, k = " + k + ", " +id;
	}

}
