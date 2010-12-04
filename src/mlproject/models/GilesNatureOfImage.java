package mlproject.models;

/**
 * @author mes592
 * Classification System from Jim Giles
 */
public enum GilesNatureOfImage {
	Ab("abstract illustration. Contains few or no elements that are literal representations of the story."),
	Ph("photograph or realistic illustration."),
	Ty("typographic cover (i.e. mainly or all text).");
	
	String explanation;
	
	GilesNatureOfImage(String explanation) {
		this.explanation = explanation;
	}
}
