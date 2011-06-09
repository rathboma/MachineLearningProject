package mlproject.bagofvwords;

import java.awt.Color;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;

import mlproject.Project;
import mlproject.models.Issue;
import mlproject.predictors.clustering.kMeansClustering;

public class VisualWordUtils {
	public static int[] getVWordDistribution(BufferedImage bimage, Double[][] vWords) throws IOException {
		return getVWordDistribution(bimage, vWords, 5, 5);
	}
	
	public static int[] getVWordDistribution(BufferedImage bimage, Double[][] vWords, int patchx, int patchy) throws IOException {
		
		List<Double[]> patches = FindVisualWords.getPatches(patchx, patchy, bimage);
		
		int[] histogram = new int[vWords.length];
		for(int i = 0; i < histogram.length; i++) histogram[i] = 0;
		
		for(Double[] patch: patches) {
			int proto = kMeansClustering.getClosestPrototype(patch, vWords);
			histogram[proto]++;
		}
		
		return histogram;
	}
	
	public static void main(String[] args) throws IOException {
		Collection<Issue> issues = Project.loadIssues();
		for(Issue issue: issues) {
			System.out.println(issue.dateString);
			for(int i = 0; i < issue.vWordHistogram.length; i++) {
				System.out.println(i + " :: " + issue.vWordHistogram[i]);
			}
		}
		
		
	}
	
	public static void seeVisualWords(Double[][] prototypes, String filename) throws IOException {
		BufferedImage im = new BufferedImage(40, 40, BufferedImage.TYPE_INT_RGB);
		
		for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				int startx = x*5;
				int starty = y*5;
				Double[] prototype = prototypes[x*8 + y];

				int pixelx = 0; int pixely = 0;
				
				for(int i = 0; i < 75; i+=3) {
					int[] rgb = new int[3];
					for(int c = 0; c < rgb.length; c++) {
						rgb[c] = (int) Math.floor(prototype[i+c]);	
						System.out.println("rgb" + c + ": " + rgb[c]);
					}
					
					Color color = new Color(rgb[0],rgb[1],rgb[2]);
					int rgbInt = color.getRGB();
					im.setRGB(startx+pixelx, starty+pixely, rgbInt);
					//im.setRGB(startx+pixelx, starty+pixely, 1, 1, rgb, 0, 0);
					
					if (pixelx < 4) {
						pixelx++;
					} else {
						pixelx = 0;
						pixely++;
					}
				}
			}
		}
		
		File f = new File(filename);
		ImageIO.write(im, "bmp", f);
	}
	
	public static int[] insersectionKernelDotProduct(int[] histogram1, int[] histogram2) {
		int[] retVal = new int[histogram1.length];
		for(int i = 0; i < histogram1.length; i++) {
			retVal[i] = Math.min(histogram1[i], histogram2[i]);
		}
		return retVal;
	}
}
