package mlproject.bagofvwords;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import mlproject.predictors.clustering.kMeansClustering;

public class BagOfVisualWordsFinder {

	public static void main(String[] args) throws IOException {
		String dataFolder = "./data/images/";
		File dataFolderFile = new File(dataFolder);
		String[] images = dataFolderFile.list();
		
		List<BufferedImage> bImages = new ArrayList<BufferedImage>();
		
		for(String image: images) {
			System.out.println(image);
			try {
				BufferedImage bimage = ImageIO.read(new File(dataFolder + image));
				
				if (bimage == null) {
					System.out.println("Error: null image " + image);
					continue;
				}
				
				bImages.add(bimage);
			} catch (IOException e) {
			 	e.printStackTrace();
			}
		}
		
		System.out.println(bImages.size());
		//Double[][] allPatches = new Double[bImages.size()*1131][];
		Double[][] allPatches = new Double[344926][]; //This is what worked.. not sure about how to get to that #
		System.out.println("Allocated Space");
		
		int i = 0;
		for(BufferedImage bimage: bImages) {
			System.out.println("Working on image.. i = " + i);
			List<Double[]> patches = getPatches(5, 5, bimage);
			for(Double[] patch: patches) {
				allPatches[i] = patch;
				i++;
			}
		}
		
		System.out.println("** " + i + " :: " + allPatches.length);
		
		int k = 100;
		kMeansClustering kMeans = getKMeans(allPatches, k);
		
		for(int j = 0; j < k; j++) {
			System.out.print("Prototype " + j + ": ");
			for(double d: kMeans.getPrototype(j)) System.out.print(d + ", ");
			System.out.println("");
		}
	}
	
	private static kMeansClustering getKMeans(Double[][] patches, int k) {
		System.out.println("Starting k-means, k="+k);
		kMeansClustering kMeans = new kMeansClustering(k);
		System.out.println("!! " + patches.length + " " + patches[0].length);
		kMeans.computePrototypes(patches);
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
