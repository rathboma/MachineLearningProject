package mlproject.abstractMath.impl;

import mlproject.abstractMath.VectorMaker;
import mlproject.models.Issue;

public class NaiveVectorMaker implements VectorMaker<Issue> {

	public static double toD(boolean b) {
		return b? 1: 0;
	}
	
	@Override
	public Double[] toVector(Issue issue) {
		Double[] v = new Double[18];
        
		v[0] = (issue.expectedSales == null)? 36041: issue.expectedSales;
		v[1] = (double) issue.date.getTime();
		v[2] = toD(issue.astronomyAndCosmology);
		v[3] = toD(issue.earthScience);
		v[4] = toD(issue.physics);
		v[5] = toD(issue.technology);
		v[6] = toD(issue.neuroSciencePsychology);
		v[7] = toD(issue.otherBiology);
		v[8] = toD(issue.otherTopic);
		v[9] = toD(issue.abstractImage);
		v[10] = toD(issue.photoImage);
		v[11] = toD(issue.typographicImage);
		v[12] = toD(issue.hasSpecial);
		v[13] = toD(issue.isSpecialEdition);
		v[14] = toD(issue.isAnniverseryEdition);
		v[15] = issue.avgRed;
		v[16] = issue.avgBlue;
		v[17] = issue.avgGreen;
		
		return v;
	}

}
