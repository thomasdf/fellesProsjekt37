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
	private StringProperty buildingProperty = new SimpleStringProperty();
	private StringProperty floorProperty = new SimpleStringProperty();
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
	
	public String getBuilding() {
		return buildingProperty.getValue();
	}
	public void setBuilding(String building) {
		buildingProperty.setValue(building);
	}
	public StringProperty buildingProperty() {
		return buildingProperty;
	}
	
	public String getFloor() {
		return floorProperty.getValue();
	}
	public void setFloor(String floor) {
		floorProperty.setValue(floor);
	}
	public StringProperty floorProperty() {
		return floorProperty;
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
