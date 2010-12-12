package mlproject.predictors;

import java.util.Collection;

import mlproject.abstractMath.DoubleVectorUtils;
import mlproject.models.Issue;
import Jama.Matrix;

public class KMeansPredictor extends BasePredictor {

	public int k;
	
	public KMeansPredictor(int k){
		this.k = k;
	}
	
	private boolean converged(Double[][] r, Double[][] r2){
		double sum = 0;
		for(int i = 0; i < r.length; i++){
			sum += DoubleVectorUtils.dist(r[i], r2[i]);
		}
		return sum == 0;
	}
	
	private Issue[] initializePrototypes(Collection<Issue> samples, int num){
		Issue[] baseSet = (Issue[])samples.toArray();
		Issue[] results = new Issue[num];
		for(int i = 0; i < results.length; i++){
			
			results[i] = new Issue();
		}
	}
	
	
	@Override
	public void Train(Collection<Issue> issues) {
		
		Issue[] prototypes = initializePrototypes(issues, k); //copy some issue
		Double[][] responsibilities = new Double[k][issues.size()];
		Double[][] lastR = new Double[k][issues.size()];
		
		
		boolean firstLoop = true;
		while( firstLoop || !converged(responsibilities, lastR)){
			for(Issue issue : issues){
				
			}
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return null;
	}

}
