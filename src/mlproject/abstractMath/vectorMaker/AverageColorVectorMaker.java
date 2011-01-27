package mlproject.abstractMath.vectorMaker;

import mlproject.abstractMath.VectorMaker;
import mlproject.models.Issue;

public class AverageColorVectorMaker implements VectorMaker<Issue> {
	
	@Override
	public Double[] toVector(Issue issue) {
		Double[] v = new Double[vectorSize()];
  
		v[0] = issue.avgRed;
		v[1] = issue.avgBlue;
		v[2] = issue.avgGreen;
		//v[3] = Utils.toDouble(issue.photoImage);
		return v;
	}

	@Override
	public int vectorSize() {
		return 3;
		//return 4;
	}

	@Override
	public String name() {
		return "Average Color";
	}

}
