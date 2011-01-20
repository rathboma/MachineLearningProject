package mlproject.predictors;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;

import mlproject.abstractMath.VectorMaker;
import mlproject.abstractMath.vectorMaker.WeightedVectorMaker;
import mlproject.models.Issue;
import weka.classifiers.functions.LinearRegression;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class LinearRegressionPredictor extends BasePredictor {

	LinearRegression linearRegression;
	public final VectorMaker<Issue> vectorMaker;
	final FastVector attributes;
	double mRidge = 0;
	
	public LinearRegressionPredictor(double ridge, VectorMaker<Issue> vectorMaker) {
		System.out.println();
		mRidge = ridge;
		this.vectorMaker = vectorMaker;
		attributes = new FastVector(vectorMaker.vectorSize() + 1);
		for(int i = 0; i < vectorMaker.vectorSize() + 1; i++) {
			attributes.addElement(new Attribute(Integer.toString(i)));
		}
	}
	
	@Override
	public double Predict(Issue issue) {
		try {
			double predicted = linearRegression.classifyInstance(getWekaInstance(issue));
			double actual = issue.getLogPercent();
			//System.out.println("actual percent " + actual + " predicted: " + predicted);
			return linearRegression.classifyInstance(getWekaInstance(issue));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public double getCoefficient(int i) {
		Double[] v = new Double[vectorMaker.vectorSize() + 1];
		for(int k = 0; k < v.length; k++) v[k] = (i == k)? 1.0: 0.0;
		try {
			double predicted = linearRegression.classifyInstance(getWekaInstance(v));
			return predicted;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public Double[] getCoefficients() {
		Double[] v = new Double[vectorMaker.vectorSize()];
		for(int i = 0; i < v.length; i++) v[i] = getCoefficient(i);
		return v;
	}

	public static void main(String[] args) {
		Field[] fs = Issue.class.getFields();
		for(int i = 0; i < fs.length; i++) {
			System.out.println(fs[i].getName());
		}
		

		LinearRegressionPredictor p = new LinearRegressionPredictor(0, new WeightedVectorMaker());
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

		for(int i = 0; i < vectorMaker.vectorSize() + 1; i++) {
			Attribute attribute = new Attribute(String.valueOf(i));
			fv.addElement(attribute);
		}
		Instances instances = new Instances("Issue", fv, issues.size());
		
		//System.out.println(fv.size());
		
		//The last element is the target variable - classIndex means 'variable you want to predict'
		instances.setClassIndex(vectorMaker.vectorSize());
		
		for(Issue issue: issues) {
			Instance instance = getWekaInstance(issue);
			instances.add(instance);
			instance.setDataset(instances);
		}
		
		linearRegression = new LinearRegression();
		
		linearRegression.setRidge(mRidge);
		try {
			//linearRegression.setRidge()
			linearRegression.buildClassifier(instances);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public String name() {
		return "Linear Regression Predictor, ridge: " + mRidge +  ", Vector Maker: " + vectorMaker.name();
	}
	
	public Instance getWekaInstance(Issue issue) {
		Double[] attributeData = vectorMaker.toVector(issue);
		Double[] v = new Double[attributeData.length + 1];
		
		for(int i = 0; i < attributeData.length; i++) {
			v[i] = attributeData[i];
		}
		
		v[attributeData.length] = issue.getLogPercent();

		return getWekaInstance(v);
	}

	public Instance getWekaInstance(Double[] v) {		
		
		Instance instance = new Instance(v.length);
		
		for(int i = 0; i < v.length; i++) {
			instance.setValue(i , v[i]);				
		}
		
		return instance;
	}

}
