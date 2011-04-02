package mlproject;

import java.util.Collection;
import mlproject.models.Issue;


public interface ISalesPredictor{
	
	public void Train(Collection<Issue> issues);
	
	//Predicts Log-Percent off the expected sales curve.
	public double Predict(Issue issue);
	
	public String name();
	
}