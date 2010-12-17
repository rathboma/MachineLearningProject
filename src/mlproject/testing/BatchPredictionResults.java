package mlproject.testing;

public class BatchPredictionResults {
	public double averageLoss;
	public int numCorrectDirection, totalChecked;
	
	public double directionalSuccessRate() {
		return ((double) numCorrectDirection) / totalChecked;
	}
}
