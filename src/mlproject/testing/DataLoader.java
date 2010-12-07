package mlproject.testing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import mlproject.models.Issue;

public class DataLoader {

	Collection<Issue> mAllIssues;;
	Collection<Issue> mTestData;
	Collection<Issue> mTrainingData;
	double mTestPercent;
	
	/**
	 * Takes a collection of issues and splits them into non-overlapping test and training sets.
	 * @param allIssues
	 * @param testPercent: The % of issues to use for testing, both 5 and 0.05 indicate 5%
	 */
	public DataLoader(Collection<Issue> allIssues, double testPercent){
		mAllIssues = allIssues;
		mTestPercent = testPercent;
		regenerate();
	}
	
	public Collection<Issue> getTestData(){
		
		return mTestData;
	}
	
	public Collection<Issue> getTrainingData(){
		
		return mTrainingData;
		
	}
	public Collection<Issue> getAll(){
		return mAllIssues;
	}
	
	public void regenerate(double newPercent){
		mTestPercent = newPercent;
		regenerate();
	}
	
	public void regenerate(){
		mTestData = new ArrayList<Issue>();
		mTrainingData = new ArrayList<Issue>();
		double testPercent = mTestPercent / 100.0;
		int total = mAllIssues.size();
		int testTot =  (int)(total * testPercent);
		int[] testNums = new int[testTot];
		Random r = new Random();
		for(int i = 0; i < testTot; i++){
			boolean already = true;
			int num;
			do{
				num = Math.abs(r.nextInt()) %  total;
				already = false;
				for(int j = 0; j < i; j++){
					if(testNums[j] == num) already = true;
				}
			}while(already);
			testNums[i] = num;

		}
		
		//nice little hack.
		Arrays.sort(testNums);
		
		Iterator<Issue> issues = mAllIssues.iterator();
		int j = 0;
		for(int i = 0; i < total; i++){
			Issue issue = issues.next();
			if(j < testNums.length && testNums[j] == i){
				mTestData.add(issue);
				j++;
			}
			else{
				mTrainingData.add(issue);
			}
			
		}

	}
	
	
}
