package mlproject.models;

import java.util.HashMap;



public class Issue{
	
	HashMap<String, String> issueData;
	String mImagePath;
	
	public Issue(String csvFields, String csv, String imagePath) throws Exception{
		this.mImagePath = imagePath;
		String[] titles = csvFields.split(",\\s*");
		String[] data = csv.split(",\\s*");
		if(titles.length != data.length) throw new Exception("# titles doesn't match # of fields");
		
		for(int i = 0; i < titles.length; i++){
			String title = titles[i];
			String d = data[i];
			issueData.put(title, d);
		}
	}
	
	public String get(String t){
		return issueData.get(t);
	}
	
	private void extractImageFeatures(String image){
		
		//TODO: Matthew
	}
	
	
	
	
	
	
}