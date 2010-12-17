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
	public Double expectedSales;
	public Long Issue;
	public Date date;
	public String heading;
	public Boolean aboveExpected;
	public Boolean earthScience, astronomyAndCosmology, physics, technology, neuroSciencePsychology;
	public Boolean otherBiology, otherTopic, abstractImage, photoImage, typographicImage;
	public Boolean hasSpecial, isSpecialEdition, isAnniverseryEdition;
	public Double avgRed = 0.0, avgGreen = 0.0, avgBlue = 0.0;
	
	public Double[][][] colorHistogram = new Double[4][4][4];
	
	public Issue() {
	    for(int r = 0; r < 4; r++) {
		    for(int g = 0; g < 4; g++) {
			    for(int b = 0; b < 4; b++) {
			    	colorHistogram[r][g][b] = 0.0;
			    }
		    }
	    }
	}
	
	public double getLogPercent() {
		return Math.log(getPercent());
	}
	
	private double getPercent(){
		if(sales != null && expectedSales != null) return sales / expectedSales;
		return 1;
	}
	
	
	public void extractImageFeatures(String img) throws IOException{
		imageAttached = true;
		File file= new File(img);
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
	    		
	    		colorHistogram[red / 64][green / 64][blue / 64] += 1;
	    		
	    		redSum += red;
	    		greenSum += green;
	    		blueSum += blue;
	    	}
	    }
	    
	    avgRed = redSum / (double)numPixels;
	    avgGreen = greenSum / (double)numPixels;
	    avgBlue = blueSum / (double)numPixels;
	    
	    for(int r = 0; r < 4; r++) {
		    for(int g = 0; g < 4; g++) {
			    for(int b = 0; b < 4; b++) {
			    	colorHistogram[r][g][b] /= (double)numPixels;
			    }
		    }
	    }
	    
		
		//TODO: Matthew to do more.
	}


	public boolean shouldOwn(Date d) {
		//System.out.println("ME " + date.toGMTString() + " vs " + d.toGMTString());
		return d.getYear() == date.getYear() && d.getMonth() == date.getMonth() && d.getDay() == date.getDay();
		
	}	
}