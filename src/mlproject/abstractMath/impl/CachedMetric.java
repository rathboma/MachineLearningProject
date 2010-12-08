package mlproject.abstractMath.impl;

import java.util.HashMap;
import java.util.Map;

import mlproject.abstractMath.Metric;

public class CachedMetric<T> implements Metric<T> {

	final Metric<T> metric;
	
	final private Map<T, Map<T, Double>> cache = new HashMap<T, Map<T, Double>>();
	
	public CachedMetric(Metric<T> metric) {
		this.metric = metric;
	}
	
	@Override
	public double distance(T t1, T t2) {
		if (cache.containsKey(t1) && cache.get(t1).containsKey(t2)) {
			return cache.get(t1).get(t2);
		}
		
		Double distance = metric.distance(t1, t2);
		
		if (!cache.containsKey(t1)) cache.put(t1, new HashMap<T, Double>());
		if (!cache.containsKey(t2)) cache.put(t2, new HashMap<T, Double>());
		
		cache.get(t1).put(t2, distance);
		cache.get(t2).put(t1, distance);

		return distance;
	}

}
