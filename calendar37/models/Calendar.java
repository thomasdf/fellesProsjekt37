package models;

import java.util.ArrayList;

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
	private ObservableList<Integer> activitiesList = FXCollections.observableList(new ArrayList<Integer>());
	
	//Constructor
	public Calendar(int calendar_id, String calendar_owner) {
		this.calendar_id = calendar_id;
		this.calendar_owner = calendar_owner;
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
	public void addActivity(int activity_id) {
		activitiesList.add(activity_id);
	}
	public void setActivities(ArrayList<Integer> activities) {
		activitiesList.addAll(activities);
	}
}
