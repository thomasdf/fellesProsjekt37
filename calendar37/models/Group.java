package models;

import java.util.ArrayList;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;

public class Group {

	//Final attributes
	private final int group_id;
	private final String group_name;
	
	//Property-attributes
	private Property<ArrayList<String>> membersProperty = new ObjectPropertyBase<ArrayList<String>>() {
		@Override
		public Object getBean() {
			return this;
		}
		@Override
		public String getName() {
			return "members";
		}
	};
	
	//Constructor
	public Group(int group_id, String group_name) {
		this.group_id = group_id;
		this.group_name = group_name;
	}
	
	//Getters, Setters & Properties
	public int getGroup_id() {
		return group_id;
	}
	
	public String getGroup_name() {
		return group_name;
	}
	
	public ArrayList<String> getMembers() {
		return membersProperty.getValue();
	}
	public void setMembers(ArrayList<String> members) {
		membersProperty.setValue(members);
	}
	public Property<ArrayList<String>> membersProperty() {
		return membersProperty;
	}
}
