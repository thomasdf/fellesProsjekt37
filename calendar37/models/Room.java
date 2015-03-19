package models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class is used to keep track of a room, where it is and most importantly, how many persons it holds.
 * It is used by the {@link Activity}-class, which may link to a room, describing where the {@link Activity}
 * is to take place.
 * 
 * @author gruppe37
 * @version %I%, %G%
 */
public class Room {

	//Final attributes
	
	//Property-attributes
	private StringProperty room_nameProperty = new SimpleStringProperty();
	private IntegerProperty capacityProperty = new SimpleIntegerProperty();
	
	//Constructor
	
	//Getters, Setters & Properties
	public String getRoom_name() {
		return room_nameProperty.getValue();
	}
	public void setRoom_name(String room_name) {
		room_nameProperty.setValue(room_name);
	}
	public StringProperty room_nameProperty() {
		return room_nameProperty;
	}
	
	public Integer getCapacity() {
		return capacityProperty.getValue();
	}
	public void setCapacity(Integer capacity) {
		capacityProperty.setValue(capacity);
	}
	public IntegerProperty capacityProperty() {
		return capacityProperty;
	}
}
