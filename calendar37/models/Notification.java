package models;

/**
 * 
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
