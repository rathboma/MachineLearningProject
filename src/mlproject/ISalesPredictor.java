package mlproject;




public interface ISalesPredictor{
	
	public void Train(Set<Issue> issues);
	public double Predict(Issue issue);
	
}