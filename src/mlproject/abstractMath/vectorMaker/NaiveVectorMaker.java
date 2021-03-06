package mlproject.abstractMath.vectorMaker;

import mlproject.Utils;
import mlproject.abstractMath.VectorMaker;
import mlproject.models.Issue;

@Deprecated public class NaiveVectorMaker implements VectorMaker<Issue> {
	
	@Override
	public Double[] toVector(Issue issue) {
		Double[] v = new Double[18];
        
		//v[0] = (issue.getExpectedSales() == null)? 36041: issue.getExpectedSales();
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
		v[15] = issue.avgRed;
		v[16] = issue.avgBlue;
		v[17] = issue.avgGreen;
		
		return v;
	}

	@Override
	public int vectorSize() {
		return 18;
	}

	@Override
	public String name() {
		return "NaiveVectorMaker";
	}

}
