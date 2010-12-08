package mlproject.abstractMath.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import mlproject.abstractMath.DoubleVectorUtils;
import mlproject.abstractMath.Metric;
import mlproject.models.Issue;

/**
 * @author mes592
 * This is a copy of Euclidean Measure but weighted so that
 *  all values are in [0, 1]
 */
public class EuclideanMetricWeighted implements Metric<Issue>  {
	
	@Override
	public double distance(Issue t1, Issue t2) {
		return DoubleVectorUtils.dist(toVector(t1), toVector(t2));
	}
	
	public static double toD(boolean b) {
		return b? 1: 0;
	}
	
	public static Double[] toVector(Issue issue) {
		Double[] v = new Double[15];

		SimpleDateFormat formatter = new SimpleDateFormat("M/d/yyyy");
		Long earliest = 0L, latest = 1L;
		try {
			earliest = formatter.parse("1/1/2005").getTime();
			latest = formatter.parse("5/1/2010").getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		v[0] = (((issue.expectedSales == null)? 36041: issue.expectedSales) - 26538) / 60297;
		v[1] = (double) (issue.date.getTime() - earliest) / latest;
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
