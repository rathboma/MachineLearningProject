package mlproject.abstractMath;

public interface VectorMaker<T> {
	String name();
	
	int vectorSize();
	
	Double[] toVector(T t);
}
