package mlproject.abstractMath;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

//import mlproject.abstractMath.impl.CachedMetric;

/**
 * @author mes592
 * Implements the nearest neighbor class using a measure function on the space
 */
public class NaturalNearestNeighbor<T> implements NearestNeighborFunction<T> {
	
	final Metric<T> metric;
	
	public NaturalNearestNeighbor(Metric<T> metric) {
		//this.metric = new CachedMetric<T>(metric);
		this.metric = metric;
	}
	
	@Override
	public List<T> nearestNeighbors(int k, final T t, Collection<T> dataSet) {
		SortedSet<T> sortedDataSet = new TreeSet<T>(new Comparator<T>() {
			@Override
			public int compare(T t1, T t2) {
				// TODO Auto-generated method stub
				return (metric.distance(t, t1) - metric.distance(t, t2)) > 0? 1: -1;
			}
		});
		
		sortedDataSet.addAll(dataSet);
		
		List<T> neighbors = new ArrayList<T>();
		for(int i = 0; i < k; i++) {
			T firstElt = sortedDataSet.first();
			if (firstElt == null) break;
			neighbors.add(firstElt);
			sortedDataSet.remove(firstElt);
		}
		
		return neighbors;
	}


}
