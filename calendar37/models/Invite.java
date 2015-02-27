package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Invite {

	//Final attributes
	private final String invited_by;
	private final String invited;
	private final int invited_to;
	
	//Property-attributes
	private StringProperty statusProperty = new SimpleStringProperty();
	
	//Constructor
	public Invite(String invited_by, String invited, int invited_to) {
		this.invited_by = invited_by;
		this.invited = invited;
		this.invited_to = invited_to;
	}
	
	//Getters, Setters & Properties
	public String getInvited_by() {
		return invited_by;
	}
	
	public String getInvited() {
		return invited;
	}
	
	public int getInvited_to() {
		return invited_to;
	}
	
	public String getStatus() {
		return statusProperty.getValue();
	}
	public void setStatus(String status) {
		statusProperty.setValue(status);
	}
	public StringProperty statusProperty() {
		return statusProperty;
	}
}
