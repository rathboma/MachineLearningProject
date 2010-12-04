package mlproject;

import java.util.Set;
import mlproject.models.Issue;


public interface ISalesPredictor{
	
	public void Train(Set<Issue> issues);
	public double Predict(Issue issue);
	
}