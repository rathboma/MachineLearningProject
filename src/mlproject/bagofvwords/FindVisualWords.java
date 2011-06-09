package mlproject.bagofvwords;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import mlproject.abstractMath.DoubleVectorUtils;
import mlproject.predictors.clustering.kMeansClustering;

public class FindVisualWords {
	final public static String vWordFile = "imageProtos.txt";
	
	public static void main(String[] args) throws IOException {
		String dataFolder = "./data/images/";
		File dataFolderFile = new File(dataFolder);
		String[] images = dataFolderFile.list();
		
		List<BufferedImage> bImages = new ArrayList<BufferedImage>();
		
		int i = -1;
		for(String image: images) {
			i++;
			if ((i%5) != 0) continue; //Sample the data.
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
		
		int j = 0;
		for(BufferedImage bimage: bImages) {
			System.out.println("Working on image.. j = " + j);
			List<Double[]> patches = getPatches(5, 5, bimage);
			for(Double[] patch: patches) {
				allPatches[j] = patch;
				j++;
			}
		}
		
		System.out.println("** " + j + " :: " + allPatches.length);
		if (j < allPatches.length) {
			Double[][] allPatchesNew = new Double[j][];
			for(int index = 0; index<j; index++) {
				allPatchesNew[index] = allPatches[index];
			}
			
			allPatches = allPatchesNew;
		}
		System.out.println("** " + j + " :: " + allPatches.length);
		
		int k = 64;
		int maxTurns = 150;
		
		long currTime = System.currentTimeMillis();
		kMeansClustering kMeans = getKMeans(allPatches, k, maxTurns);
		System.out.println("time: " + (System.currentTimeMillis() - currTime));
		
		FileWriter fr = new FileWriter(vWordFile);
		for(int j2 = 0; j2 < k; j2++) {
			Double[] proto = kMeans.getPrototype(j2);
			System.out.println("Prototype " + j2 + ": " + DoubleVectorUtils.vectorToString(proto));
			fr.append(DoubleVectorUtils.vectorToString(proto));
			fr.append("\n");
		}
		
		fr.close();
	}
	
	private static kMeansClustering getKMeans(Double[][] patches, int k, int maxTurns) {
		System.out.println("Starting k-means, k="+k + " : numTurns = " + maxTurns);
		kMeansClustering kMeans = new kMeansClustering(k, maxTurns);
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
	
	public static Double[][] loadVisualWords(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		ArrayList<Double[]> array = new ArrayList<Double[]>();
		while(true) {
			String line = br.readLine();
			if (line == null) break;
			String[] vals = line.split(", ");
			Double[] dvals = new Double[vals.length];
			for(int i = 0; i < vals.length; i++) {
				dvals[i] = Double.parseDouble(vals[i]);
			}
			array.add(dvals);
		}
		
		Double[][] retVal = new Double[array.size()][];
		for(int i = 0; i < array.size(); i++) retVal[i] = array.get(i);
		
		return retVal;
	}

}
