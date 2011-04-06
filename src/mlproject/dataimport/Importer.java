package mlproject.dataimport;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import mlproject.models.Issue;
import au.com.bytecode.opencsv.CSVParser;

public class Importer {

	public static Collection<Issue> getIssues(String dataFile) throws IOException {
		FileReader fr = null;
		try {
			fr = new FileReader(dataFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);
		
		Collection<Issue> issues = new ArrayList<Issue>(); 
		
		String csvFields = br.readLine();
		String csvValues = null;
		while((csvValues = br.readLine()) != null) {
			
			//TODO: Add the image path
			String imagePath = null;
			
			try {
				Issue newIssue = makeIssue(csvFields, csvValues, imagePath);
				if (newIssue.date != null) {
					issues.add(newIssue);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return issues;
	}
	
	public static Issue makeIssue(String csvFields, String csvValues, String imagePath) throws Exception{
		Issue issue = new Issue();
		issue.mImagePath = imagePath;
		
		CSVParser parser = new CSVParser();
		
		
		String[] titles = parser.parseLine(csvFields);
		String[] data = parser.parseLine(csvValues);
		if(titles.length != data.length) {
			//throw new Exception("# titles doesn't match # of fields.\n" + csvFields + "\n" + csvValues);
			System.out.println("# titles doesn't match # of fields.\n" + csvFields + "\n" + csvValues);
			for(String d: data) System.out.println(d);
			
			System.exit(0);
		}
		
		for(int i = 0; i < titles.length; i++){
			Importer.importField(titles[i], issue, data[i]);
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		issue.date = dateFormat.parse(issue.dateString);
		
		return issue;
	}
	
	public static boolean importField(String fieldName, Object obj, String data) throws SecurityException, NoSuchFieldException {
		Class<?> inClass = obj.getClass();
		if (!hasField(inClass, fieldName)) return false;
		Field f = inClass.getField(fieldName);
		
		try {
			f.set(obj, parse(data, f.getType()));
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
	public static boolean hasField(Class<?> c, String fName) {
		for(Field f: c.getFields()) {
			if (f.getName().equals(fName)) return true;
		}
		return false;
	}
	
	public static <T> T parse(String str, Class<T> type) {
		if (type == Boolean.class)
			return (T) (Boolean) Boolean.parseBoolean(str);
		if (type == Long.class)
			return (T) (Long) Long.parseLong(str);
		if (type == Double.class)
			return (T) (Double) Double.parseDouble(str);
		if (type == String.class) 
			return (T) str;
		if (type == Date.class) {
			//simple date parser doesn't work well when year is 2 numbers, which it was. Regular date parser works better.
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			try {
				return (T) dateFormat.parse(str);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		throw new RuntimeException("Unknown type: " + type);
	}
	
	public static void main(String [] args) {
		Class<Issue> cl = Issue.class;
		for(Field f: cl.getDeclaredFields()) {
			System.out.println(f.getName() + "::" + f.getType().getCanonicalName());
			if (f.getType().isEnum()) System.out.println("Is enum");
		}
	}

	public static File[] getImages(String string) {
		File folder = new File(string);
		return folder.listFiles();
	}
	
	public static HashMap<File, Date> extractIssueDates(File[] files){
		
		File testEnv = new File("/Users/matthew/");
		File f = testEnv.exists()? new File("/Users/matthew/mappings.txt"): new File("/home/mes592/mappings.txt");
		
		HashMap<File, Date> map = new HashMap<File, Date>();
		try{
			BufferedWriter o = new BufferedWriter(new FileWriter(f));
		
		for(File file : files){
			Date d = getDate(file.getName());
			//o.write(file.getName() + " , " + (d.getYear() + 1900) + "-" + d.getMonth() + "-" + d.getDay() + "\n");
			map.put(file, getDate(file.getName()));
		}
		o.close();
		}catch(Exception e){}
		return map;
	}
	
	private static Date getDate(String filename){
		String[] split1 = filename.split("-");
		Date d;
		if(split1.length > 1){
			d = new Date(Date.parse(split1[1] + "/" + split1[2] + "/" + split1[3]));
			
		} else{
			split1 = filename.split("_");
			String[] secondLevel = split1[1].split("(?<=\\G..)");
			d = new Date(Date.parse(secondLevel[1] + "/" + secondLevel[0] + "/" + secondLevel[2]));
		}
		//System.out.println(d.toGMTString() + " from " + filename);
		return d;
		
	}
	
	
	
}
