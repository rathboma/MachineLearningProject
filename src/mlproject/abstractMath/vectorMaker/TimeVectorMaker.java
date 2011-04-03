package mlproject.abstractMath.vectorMaker;

import java.sql.Time;

import mlproject.abstractMath.VectorMaker;
import mlproject.models.Issue;

public class TimeVectorMaker implements VectorMaker<Issue> {

	@Override
	public String name() {
		return "Time Vector maker";
	}

	@Override
	public Double[] toVector(Issue t) {
		Double[] v = new Double[1];
		v[0] = (double) t.getTime();
		return v;
	}

	@Override
	public int vectorSize() {
		return 1;
	}

}
