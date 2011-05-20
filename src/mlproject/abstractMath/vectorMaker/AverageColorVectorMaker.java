package mlproject.abstractMath.vectorMaker;

import java.awt.Color;

import mlproject.abstractMath.VectorMaker;
import mlproject.models.Issue;

public class AverageColorVectorMaker implements VectorMaker<Issue> {
	
	public final Type type;
	
	public AverageColorVectorMaker(Type type) {
		this.type = type;
	}
	
	@Override
	public Double[] toVector(Issue issue) {
		Double[] v = new Double[vectorSize()];
  
		switch(type) {
		case RGB:
			v[0] = issue.avgRed;
			v[1] = issue.avgBlue;
			v[2] = issue.avgGreen;
			break;
		case RGB_LOG_ODDS:
			v[0] = issue.logOddsAvgRed;
			v[1] = issue.logOddsAvgBlue;
			v[2] = issue.logOddsAvgGreen;
			break;
		case RGB_MODE:
			double[] modeColor = getMostPopularColor(issue);
			v[0] = modeColor[0];
			v[1] = modeColor[1];
			v[2] = modeColor[2];
			break;
		}

		return v;
	}

	@Override
	public int vectorSize() {
		return 3;
		//return 4;
	}

	@Override
	public String name() {
		return "Average Color " + type;
	}
	
	static public double[] getMostPopularColor(Issue issue) {
		double[] currentColor = {0, 0, 0};
		double currentMostPopularAmount = 0;
		
		for(int r = 0; r < Issue.colorLimit; r++) {
			for(int g = 0; g < Issue.colorLimit; g++) {
				for(int b = 0; b < Issue.colorLimit; b++) {
					double count = issue.colorHistogram[r][g][b];
					if (count > currentMostPopularAmount) {
						currentColor[0] = r;
						currentColor[1] = g;
						currentColor[2] = b;
						currentMostPopularAmount = count;
					}
				}
			}
		}
		
		return currentColor;
	}
	
	public static enum Type {
		RGB, RGB_LOG_ODDS, RGB_MODE;
	}

}
