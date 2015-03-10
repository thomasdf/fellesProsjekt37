package models;

//TESTVALUES!!!
import java.util.ArrayList;
import java.util.Arrays;
//TESTVALUES!!!

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
	private List<Integer> activities = new ArrayList<Integer>();
	private ObservableList<Integer> activitiesList = FXCollections.observableList(activities);
	
	//Constructor
	public Calendar(int calendar_id, String calendar_owner) {
		this.calendar_id = calendar_id;
		this.calendar_owner = calendar_owner;
		
		//TESTVALUES!!!
		activitiesList.addAll(Arrays.asList(10, 10, 5, 8, 15, 28));
		//TESTVALUES!!!
	}
	
	//Getters, Setters & Properties
	public int getCalendar_id() {
		return calendar_id;
	}
	
	public String getCalendar_owner() {
		return calendar_owner;
	}
	
	public ObservableList<Integer> getActivities() {
		return activitiesList;
	}
	public void setActivities(ObservableList<Integer> activities) {
		activitiesList = activities;
	}
}
