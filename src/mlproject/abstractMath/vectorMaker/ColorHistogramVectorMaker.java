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
		
		for(int r = 0; r < 4; r++) {
		    for(int g = 0; g < 4; g++) {
			    for(int b = 0; b < 4; b++) {
			    	v[atPlace] = t.colorHistogram[r][g][b];
			    	atPlace++;;
			    }
		    }
	    }

		return v;
	}

	@Override
	public int vectorSize() {
		return 4*4*4;
	}

}
