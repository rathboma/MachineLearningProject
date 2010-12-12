package mlproject.abstractMath;

public interface VectorMaker<T> {
	
	int vectorSize();
	
	Double[] toVector(T t);
}
