package mlproject.dataimport;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import au.com.bytecode.opencsv.CSVParser;

import mlproject.models.Issue;

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
				issues.add(makeIssue(csvFields, csvValues, imagePath));
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
			try {
				return (T) (new SimpleDateFormat("M/d/yyyy")).parse(str);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
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
	
}
