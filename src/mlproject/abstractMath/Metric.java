package mlproject.abstractMath;

public interface Metric<T> {
	double distance(T t1, T t2);
}
