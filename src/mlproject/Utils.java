package mlproject;

import java.util.Date;

public class Utils {
	public static double toDouble(Object o) {
		if (o instanceof Double) return (Double) o; 
		if (o instanceof Boolean) return (Boolean)o? 1: 0;
		if (o instanceof Date) return (double) ((Date)o).getTime();
			
		return 0.0d;
	}
}