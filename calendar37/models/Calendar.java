package models;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;

/**
 * This class is the main class, and the basis of our application. It contains every {@link Activity} for a certain Calendar,
 * and shows the correct {@link Activity} to the correct {@link Account}. A Calendar can either belong to a {@link Group} or
 * an {@link Account}, which leads to it showing for certain people in our user-database.
 * 
 * @author gruppe37
 * @version %I%, %G%
 */
public class Calendar {

	//Final attributes
	private final int calendar_id;
	private final String calendar_owner;
	
	//Property-attributes
	private Property<ArrayList<Integer>> activitiesProperty = new ObjectPropertyBase<ArrayList<Integer>>() {
		@Override
		public Object getBean() {
			return this;
		}
		@Override
		public String getName() {
			return "activities";
		}
	};
	
	//Constructor
	public Calendar(int calendar_id, String calendar_owner) {
		this.calendar_id = calendar_id;
		this.calendar_owner = calendar_owner;
		
		//TESTVALUES!!!
		activitiesProperty.getValue().addAll(Arrays.asList(1, 5, 8, 15, 28));
		//TESTVALUES!!!
	}
	
	//Getters, Setters & Properties
	public int getCalendar_id() {
		return calendar_id;
	}
	
	public String getCalendar_owner() {
		return calendar_owner;
	}
	
	public ArrayList<Integer> getActivities() {
		return activitiesProperty.getValue();
	}
	public void setActivities(ArrayList<Integer> activities) {
		activitiesProperty.setValue(activities);
	}
	public Property<ArrayList<Integer>> activitiesProperty() {
		return activitiesProperty;
	}
}
