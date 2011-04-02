package mlproject.models;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import javax.imageio.ImageIO;

public class Issue{
	@IgnoreField public String mImagePath;
	public boolean imageAttached;
	
	public Double sales;
	public Long Issue;
	public Date date;
	public Integer time;
	public String heading;
	public Boolean aboveExpected;
	public Boolean earthScience, astronomyAndCosmology, physics, technology, neuroSciencePsychology;
	public Boolean otherBiology, otherTopic, abstractImage, photoImage, typographicImage;
	public Boolean hasSpecial, isSpecialEdition, isAnniverseryEdition;
	
	public Double avgRed = 0.0, avgGreen = 0.0, avgBlue = 0.0;
	
	public Double logOddsAvgRed = 0.0, logOddsAvgGreen = 0.0, logOddsAvgBlue = 0.0;
	
	public static int colorLimit = 4;
	public Double[][][] colorHistogram = new Double[colorLimit][colorLimit][colorLimit];
	public String imageFile;
	
	public Issue() {
	    for(int r = 0; r < colorLimit; r++) {
		    for(int g = 0; g < colorLimit; g++) {
			    for(int b = 0; b < colorLimit; b++) {
			    	colorHistogram[r][g][b] = 0.0;
			    }
		    }
	    }
	}
	
	public static double EXP_LOG_SALES_QUAD_A = -1.242E-7;
	public static double EXP_LOG_SALES_QUAD_B = 1.161126E-4;
	public static double EXP_LOG_SALES_QUAD_C = 10.5357962769;
	
	public Double getExpectedLogSales() {
		if (time == null) return null;
		return EXP_LOG_SALES_QUAD_A*time*time + EXP_LOG_SALES_QUAD_B*time + EXP_LOG_SALES_QUAD_C;
	}
	
	public Double getLogSales() {
		return Math.log(sales);
	}
	
	public Double getExpectedSales() {
		return Math.exp(getExpectedLogSales());
	}
		
	public double getLogPercent() {
		return Math.log(getPercent());
	}
	
	public double getDirection(){
		return getLogPercent() >= 0 ? 1 : -1;
	}
	
	private double getPercent(){
		if(sales != null && getExpectedSales() != null) return sales / getExpectedSales();
		return 1;
	}
	
	
	public void extractImageFeatures(String img) throws IOException{
		imageAttached = true;
		File file= new File(img);
		this.imageFile = file.getName();
	    BufferedImage image = ImageIO.read(file);
	    int maxX = image.getWidth();
	    int maxY = image.getHeight();
	    int numPixels = maxX * maxY;
	    
	    int redSum = 0, greenSum = 0, blueSum = 0;
	    
	    for(int x = 0; x < maxX; x++){
	    	for(int y = 0; y < maxY; y++){
	    	    Color color = new Color(image.getRGB(x, y));
	    		int red = color.getRed();
	    		int green = color.getGreen();
	    		int blue = color.getBlue();
	    		int divisor = 256 / colorLimit;
	    		colorHistogram[red / divisor][green / divisor][blue / divisor] += 1;
	    		
	    		redSum += red;
	    		greenSum += green;
	    		blueSum += blue;
	    	}
	    }
	    
	    double avgRedCalc = redSum / (double)numPixels;
	    double avgGreenCalc = greenSum / (double)numPixels;
	    double avgBlueCalc = blueSum / (double)numPixels;
	    
	    setColors(avgRedCalc, avgGreenCalc, avgBlueCalc);
	    
	    for(int r = 0; r < colorLimit; r++) {
		    for(int g = 0; g < colorLimit; g++) {
			    for(int b = 0; b < colorLimit; b++) {
			    	colorHistogram[r][g][b] /= (double)numPixels;
			    }
		    }
	    }
	    
		//TODO: Matthew to do more.
	}
	
	public void setColors(double avgRed, double avgGreen, double avgBlue) {
		this.avgRed = avgRed;
		this.avgGreen = avgGreen;
		this.avgBlue = avgBlue;
		
	    //Map onto the real line
	    logOddsAvgRed = logOdds((avgRed + 0.5) / 256);
	    logOddsAvgGreen = logOdds((avgGreen + 0.5) / 256);
	    logOddsAvgBlue = logOdds((avgBlue + 0.5) / 256);
	}

	public static double logOdds(double x) {
		return Math.log(x/(1-x));
	}
	

	public boolean shouldOwn(Date d) {
		
		if (d == null) System.err.println("Input date is null");
		if (d == null) System.err.println("Issue date is null");
		
		return d.equals(date);
	}	
}