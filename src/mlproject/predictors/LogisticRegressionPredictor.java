package mlproject.predictors;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Enumeration;

import weka.classifiers.functions.Logistic;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import mlproject.Utils;
import mlproject.abstractMath.VectorMaker;
import mlproject.models.IgnoreField;
import mlproject.models.Issue;
//import mlproject.models.TargetField;
import mlproject.models.TargetField;

public class LogisticRegressionPredictor extends BasePredictor {

	Logistic logistic;
	final FastVector attributes;
	VectorMaker vectorMaker;
	Instances globalInstances;
	double ridge;
	public LogisticRegressionPredictor(double ridge, VectorMaker maker) {
		vectorMaker = maker;
		this.ridge = ridge;
		Field[] fs = Issue.class.getFields();
		attributes = new FastVector(fs.length);
		for(int i = 0; i < fs.length; i++) attributes.addElement(new Attribute(Integer.toString(i)));		
	}
	
	@Override
	public double Predict(Issue issue) {
		try {
			double actual = issue.getLogPercent();
			double predicted = logistic.classifyInstance(getWekaInstance(issue)) == 0 ? 1 : -1; 
			System.out.println("actual percent " + actual + " predicted: " + predicted);
			return predicted; 
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		Field[] fs = Issue.class.getFields();
		for(int i = 0; i < fs.length; i++) {
			System.out.println(fs[i].getName());
		}
		
		LogisticRegressionPredictor p = new LogisticRegressionPredictor(1, null);
		System.out.println("size: " + p.attributes.size());
		Instances instances = new Instances("Issue", p.attributes, 300);

		Enumeration<?> enumer = instances.enumerateAttributes();
		System.out.println("Enums");
		while(enumer.hasMoreElements()) {
			System.out.println(enumer.nextElement().toString());
		}
	}
	
	@Override
	public void Train(Collection<Issue> issues) {
		FastVector fv = new FastVector(vectorMaker.vectorSize() + 1);

		for(int i = 0; i < vectorMaker.vectorSize(); i++) {
			Attribute attribute = new Attribute(String.valueOf(i));
			fv.addElement(attribute);
		}
		FastVector possibleValues = new FastVector(2);
		possibleValues.addElement("1");
		possibleValues.addElement("-1");
		fv.addElement(new Attribute("direction", possibleValues));
		
		Instances instances = new Instances("Issue", fv, issues.size());
		
		instances.setClassIndex(vectorMaker.vectorSize());
		globalInstances = instances;
		for(Issue issue: issues) {
			Instance instance = getWekaInstance(issue);
			instances.add(instance);
			//instance.setDataset(instances);
		}
		
		logistic = new Logistic();
		logistic.setRidge(this.ridge);
		try {
			logistic.buildClassifier(instances);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public String name() {
		return "Logistic Regression Predictor w/ ridge " + this.ridge;
	}
	
	public Instance getWekaInstance(Issue issue) {
Double[] attributeData = vectorMaker.toVector(issue);
		
		Double[] v = new Double[attributeData.length];
		
		for(int i = 0; i < attributeData.length; i++) {
		//	System.out.println("" + i + ": " + attributeData[i]);
			v[i] = attributeData[i];
		}
		
		String topValue = issue.getLogPercent() >= 0 ? "1" : "-1";
		
		Instance instance = new Instance(v.length + 1);
		instance.setDataset(globalInstances);
		for(int i = 0; i < v.length; i++) {
			instance.setValue(i , v[i]);				
		}
		instance.setValue(attributeData.length, topValue);
		
		return instance;
	}

}
