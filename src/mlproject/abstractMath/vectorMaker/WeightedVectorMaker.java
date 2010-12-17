package mlproject.abstractMath.vectorMaker;

import mlproject.Utils;
import mlproject.abstractMath.VectorMaker;
import mlproject.models.Issue;

public class WeightedVectorMaker implements VectorMaker<Issue> {
	
	@Override
	public Double[] toVector(Issue issue) {
		Double[] v = new Double[vectorSize()];
        
		v[0] = (issue.expectedSales == null)? 36041: issue.expectedSales;
		v[1] = (double) issue.date.getTime();
		v[2] = Utils.toDouble(issue.astronomyAndCosmology);
		v[3] = Utils.toDouble(issue.earthScience);
		v[4] = Utils.toDouble(issue.physics);
		v[5] = Utils.toDouble(issue.technology);
		v[6] = Utils.toDouble(issue.neuroSciencePsychology);
		v[7] = Utils.toDouble(issue.otherBiology);
		v[8] = Utils.toDouble(issue.otherTopic);
		v[9] = Utils.toDouble(issue.abstractImage);
		v[10] = Utils.toDouble(issue.photoImage);
		v[11] = Utils.toDouble(issue.typographicImage);
		v[12] = Utils.toDouble(issue.hasSpecial);
		v[13] = Utils.toDouble(issue.isSpecialEdition);
		v[14] = Utils.toDouble(issue.isAnniverseryEdition);
		return v;
	}

	@Override
	public int vectorSize() {
		return 15;
	}

	@Override
	public String name() {
		return "Weighted";
	}

}
