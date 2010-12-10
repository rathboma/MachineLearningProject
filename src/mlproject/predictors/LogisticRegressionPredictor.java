package mlproject.predictors;

import java.util.Collection;

import weka.classifiers.functions.Logistic;
import weka.core.Instance;

import mlproject.models.Issue;

public class LogisticRegressionPredictor extends BasePredictor {

	Logistic logistic;
	
	@Override
	public double Predict(Issue issue) {
		
		
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void Train(Collection<Issue> issues) {
		// TODO Auto-generated method stub
		
	}
	
	//public static Instance getWekaInstance(Issue issue) {
		
	//}

}
