package mlproject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Random;

public class Utils {
	public static double toDouble(Object o) {
		if (o instanceof Double) return (Double) o; 
		if (o instanceof Boolean) return (Boolean)o? 1: 0;
		if (o instanceof Date) return (double) ((Date)o).getTime();
			
		return 0.0d;
	}
	
	public static int sign(double d) {
		if (d > 0) return 1;
		if (d < 0) return -1;
		return 0;
	}
	
	public static <T> Collection<T> randomSubset(Collection<T> collection, int size) {
		ArrayList<T> clone = new ArrayList<T>();
		for(T t : collection) clone.add(t);
		
		Random generator = new Random();

		ArrayList<T> retVal = new ArrayList<T>();
		
		for(int i = 0; i < size; i++) {
			int index = generator.nextInt(clone.size());
			retVal.add(clone.remove(index));
		}
		
		return retVal;
	}
}
