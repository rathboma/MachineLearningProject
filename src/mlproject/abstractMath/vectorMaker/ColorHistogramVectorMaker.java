package mlproject.abstractMath.vectorMaker;

import mlproject.abstractMath.VectorMaker;
import mlproject.models.Issue;

public class ColorHistogramVectorMaker implements VectorMaker<Issue> {

	@Override
	public String name() {
		return "Color histogram";
	}

	@Override
	public Double[] toVector(Issue t) {
		Double[] v = new Double[vectorSize()];
		
	    int atPlace = 0;
		int cLimit = Issue.colorLimit;
		for(int r = 0; r < cLimit; r++) {
		    for(int g = 0; g < cLimit; g++) {
			    for(int b = 0; b < cLimit; b++) {
			    	v[atPlace] = t.colorHistogram[r][g][b];
			    	atPlace++;
			    }
		    }
	    }

		return v;
	}

	@Override
	public int vectorSize() {
		return Issue.colorLimit*Issue.colorLimit*Issue.colorLimit;
	}

}
