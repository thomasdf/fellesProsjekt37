package models;

/**
 * This class is being used to notify an {@link Account} or a {@link Group} of changes, invitations, or any other
 * information that is needed for a {@link Person}, and is provided by an {@link Activity}.
 * 
 * @author gruppe37
 * @version %I%, %G%
 */
public class Notification {

	//Final attributes
	private final String message;
	
	//Property-attributes
	
	//Constructor
	public Notification(String message) {
		this.message = message;
	}
	
	//Getters, Setters & Properties
	public String getMessage() {
		return message;
	}
}
