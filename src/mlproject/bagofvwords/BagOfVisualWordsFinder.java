package mlproject.bagofvwords;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import mlproject.predictors.clustering.kMeansClustering;

public class BagOfVisualWordsFinder {

	public static void main(String[] args) {
		String dataFolder = "./data/images/";
		File dataFolderFile = new File(dataFolder);
		String[] images = dataFolderFile.list();
		
		List<Double[]> allPatches = new LinkedList<Double[]>();
		
		for(String image: images) {
			System.out.println(image);
			try {
				BufferedImage bimage = ImageIO.read(new File(dataFolder + image));
				
				if (bimage == null) {
					System.out.println("Error: null image " + image);
					continue;
				}

				List<Double[]> patches = getPatches(5, 5, bimage);
				allPatches.addAll(patches);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		kMeansClustering kMeans = getKMeans(allPatches, 100);

	}
	
	private static kMeansClustering getKMeans(List<Double[]> patches, int k) {
		System.out.println("Starting k-means, k="+k);
		kMeansClustering kMeans = new kMeansClustering(k);
		Double[][] dataSet = (Double[][]) patches.toArray();
		System.out.println("!! " + dataSet.length + " " + dataSet[0].length);
		kMeans.computePrototypes(dataSet);
		return kMeans;
	}

	/**
	 * @param patchx
	 * @param patchy
	 * @param imageFile
	 * @return get an array of size (patchx x patchy x 3) from the image
	 * @throws IOException 
	 */
	public static List<Double[]> getPatches(int patchx, int patchy, BufferedImage bimage) throws IOException {
		List<Double[]> patches = new ArrayList<Double[]>();
		
		if (bimage == null) {
			System.out.println("Error: null image ");
			return patches;
		}
		
	    int maxX = bimage.getWidth();
	    int maxY = bimage.getHeight();
	    
	    for(int x = 0; x < maxX - patchx; x+= patchx) {
		    for(int y = 0; y < maxY - patchy; y+= patchy) {
		    	Double[] patch = getPatch(bimage, x, y, patchx, patchy);
		    	patches.add(patch);
		    }	
	    }
	    
	    return patches;
	}

	private static Double[] getPatch(BufferedImage bimage, int x, int y, int patchx, int patchy) {
		Double[] retVal = new Double[patchx*patchy*3];
		int patchIndex = 0;
		for(int i = 0; i < patchx; i++) {
			for(int j = 0; j < patchy; j++) {
				Color color = new Color(bimage.getRGB(i + x, j + y));
				retVal[patchIndex] = (double) color.getRed();
				retVal[patchIndex+1] = (double) color.getGreen();
				retVal[patchIndex+2] = (double) color.getBlue();
				patchIndex += 3;
			}
		}
		return retVal;
	}

}
