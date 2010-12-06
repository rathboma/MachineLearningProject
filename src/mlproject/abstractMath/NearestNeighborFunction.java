package mlproject.abstractMath;

import java.util.Collection;
import java.util.List;

public interface NearestNeighborFunction<T> {
	/**
	 * @param t
	 * @return the k-nearest neighbors of t from closest to furthest in the dataset
	 */
	List<T> nearestNeighbors(int k, T t, Collection<T> dataSet);
}
