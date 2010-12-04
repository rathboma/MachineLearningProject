package mlproject.dataimport;

import java.lang.reflect.Field;

import mlproject.models.Issue;

public class Importer {

	public void importField(String fieldName, Object obj, String data) throws SecurityException, NoSuchFieldException {
		Class<?> inClass = obj.getClass();
		Field f = inClass.getField(fieldName);
		
		Class<?> type = f.getType();
		
		String typeString = f.getType().toString();
		
		//switch(typeString) {
		//
		//}
	}
	
	//enum ImportedTypes {
	//	
	//}
	
	public static void main(String [] args) {
		Class<Issue> cl = Issue.class;
		for(Field f: cl.getDeclaredFields()) {
			System.out.println(f.getName() + "::" + f.getType().toString());
			if (f.getType().isEnum()) System.out.println("Is enum");
		}
	}
	
}
