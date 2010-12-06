package mlproject.abstractMath.impl;

import mlproject.abstractMath.DoubleVectorUtils;
import mlproject.abstractMath.Measure;
import mlproject.models.Issue;

public class EuclideanMeasure implements Measure<Issue> {

	@Override
	public double distance(Issue t1, Issue t2) {
		return DoubleVectorUtils.dist(toVector(t1), toVector(t2));
	}
	
	public static double toD(boolean b) {
		return b? 1: 0;
	}
	
	public static Double[] toVector(Issue issue) {
		Double[] v = new Double[15];
		                        
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
		
		return v;
	}

}
