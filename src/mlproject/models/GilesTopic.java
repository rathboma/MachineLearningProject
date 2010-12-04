package mlproject.models;

/**
 * @author mes592
 * Classification system comes from Jim Giles
 */
public enum GilesTopic {
	E("Earth scieces & environment"),
	A("astronomy & cosmology"),
	Q("quantum physics & other physics"),
	T("technology"),
	N("neuroscience & psychology"),
	B("other biology"),
	O("other");
	
	String longform;
	
	GilesTopic(String longform) {
		this.longform = longform;
	}
	
	
}
