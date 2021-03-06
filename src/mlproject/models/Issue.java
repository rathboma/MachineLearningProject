package mlproject.models;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.imageio.ImageIO;

import mlproject.bagofvwords.VisualWordUtils;

public class Issue{
    private static final DateFormat firstIssueDateFormat = new SimpleDateFormat("MM/dd/yyyy");
    public static Date firstIssue;
    
    static {
	 try {
		 firstIssue = firstIssueDateFormat.parse("01/08/2005");
	 } catch(Exception e) {e.printStackTrace();}
    }
	
	@IgnoreField public String mImagePath;
	public boolean imageAttached;
	
	public Double expectedSales;
	public Double sales;
	public Long Issue;
	public Date date;
	public String dateString;
	public String heading;
	public Boolean aboveExpected;
	public Boolean earthScience, astronomyAndCosmology, physics, technology, neuroSciencePsychology;
	public Boolean otherBiology, otherTopic, abstractImage, photoImage, typographicImage;
	public Boolean hasSpecial, isSpecialEdition, isAnniverseryEdition;
	
	public Double avgRed = 0.0, avgGreen = 0.0, avgBlue = 0.0;
	public Double logOddsAvgRed = 0.0, logOddsAvgGreen = 0.0, logOddsAvgBlue = 0.0;
	
	public static int colorLimit = 4;
	public Double[][][] colorHistogram = new Double[colorLimit][colorLimit][colorLimit];
	
	public int[] vWordHistogram;
	
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
	
	public static double EXP_LOG_SALES_QUAD_A = -1.091306773154e-07;
	public static double EXP_LOG_SALES_QUAD_B = 8.675900629499e-05;
	public static double EXP_LOG_SALES_QUAD_C = 1.053759992675e+01;
	
	public Double getExpectedLogSales() {
		Long time = getTime();
		if (time == null) return null;
		return EXP_LOG_SALES_QUAD_A*time*time + EXP_LOG_SALES_QUAD_B*time + EXP_LOG_SALES_QUAD_C;
	}
	
	public Long getTime() {
		if (date == null) return null;
		return (((date.getTime() - firstIssue.getTime())) / (1000*60*60*24));
		
	}
	
	public int getDayOfYear() {
        Calendar ca1 = Calendar.getInstance();
        ca1.set(date.getYear(),date.getMonth(),date.getDay());
        return ca1.get(Calendar.DAY_OF_YEAR);
	}
	
	//public Double getOldExpectedSales() {
	//	Long time = getTime();
	//	if (time == null) return null;
	//	return 37676.1 + 3.37277*time - 0.00400981*time*time;
	//}
	
	public Double getLogSales() {
		return Math.log(sales);
	}
	
	//public Double getExpectedSales() {
	//	Long time = getTime();
	//	if (time == null) return null;
	//	return Math.exp(getExpectedLogSales());
	//}
		
	//public double getLogPercent() {
	//	return Math.log(getPercent());
	//}
	
	//public double getDirection(){
	//	return getLogPercent() >= 0 ? 1 : -1;
	//}
	
	//private double getPercent(){
	//	if(sales != null && getExpectedSales() != null) return sales / getExpectedSales();
	//	return 1;
	//}
	
	
	public void extractImageFeatures(String folder, Double[][] vwords) throws IOException{
		String img = folder + this.dateString + ".jpg";
		imageAttached = true;
		File file= new File(img);
		this.imageFile = file.getName();
		System.out.println("extracting image features for " + this.dateString);
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
	    
		if (vwords != null)
			vWordHistogram = VisualWordUtils.getVWordDistribution(image, vwords);
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