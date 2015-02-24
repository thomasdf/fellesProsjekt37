package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Room {

	//Final attributes
	
	//Property-attributes
	private StringProperty room_nameProperty = new SimpleStringProperty();
	private StringProperty buildingProperty = new SimpleStringProperty();
	private StringProperty floorProperty = new SimpleStringProperty();
	
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
}
