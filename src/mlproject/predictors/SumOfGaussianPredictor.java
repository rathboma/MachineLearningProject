package mlproject.predictors;

import java.util.Collection;

import mlproject.abstractMath.DoubleVectorUtils;
import mlproject.abstractMath.VectorMaker;
import mlproject.models.Issue;



public class SumOfGaussianPredictor extends BasePredictor{

	final public VectorMaker<Issue> vectorMaker;
	final public double standardDev;
	
	private Collection<Issue> issues;
	
	public SumOfGaussianPredictor(VectorMaker<Issue> vectorMaker, double standardDev) {
		this.vectorMaker = vectorMaker;
		this.standardDev = standardDev;
	}
	
	@Override
	public double Predict(Issue issue) {
		Double[][] covar = new Double[vectorMaker.vectorSize()][vectorMaker.vectorSize()];
		for(int i = 0; i < vectorMaker.vectorSize(); i++)
			for(int j = 0; j < vectorMaker.vectorSize(); j++)
				covar[i][j] = (i==j)? standardDev: 0;
		
		Double[] v = vectorMaker.toVector(issue);
		
		//Get the appropriate mixture
		Double[] mixture = new Double[issues.size()];
		double mixtureSum = 0;
		double prediction = 0;
		int i = 0;
		for(Issue knownIssue: issues) {
			mixture[i] = DoubleVectorUtils.computeGaussian(v, vectorMaker.toVector(knownIssue), covar);
			mixtureSum += mixture[i];
			prediction += mixture[i] * knownIssue.getLogPercent();
			i++;
		}
		
		prediction /= mixtureSum;
		return prediction;
	}

	@Override
	public void Train(Collection<Issue> issues) {
		this.issues = issues;
	}

	@Override
	public String name() {
		return "Sum Of Gaussian Predictor, sd = " + standardDev;
	}
	
	
}