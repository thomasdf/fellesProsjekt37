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
	private final String calendar_owner_user;
	private final int calendar_owner_group;
	private final boolean is_group_cal;
	
	//Property-attributes
	private ObservableList<Integer> activitiesList = FXCollections.observableList(new ArrayList<Integer>());
	
	//Constructor
		//For user-owner
	public Calendar(int calendar_id, String user_name) {
		this.calendar_id = calendar_id;
		this.calendar_owner_user = user_name;
		this.calendar_owner_group = 0;
		this.is_group_cal = false;
	}
		//For group-owner
	public Calendar(int calendar_id, int group_id) {
		this.calendar_id = calendar_id;
		this.calendar_owner_group = group_id;
		this.calendar_owner_user = null;
		this.is_group_cal = false;
	}
	
	//Getters, Setters & Properties
	public int getCalendar_id() {
		return calendar_id;
	}
	
	public String getCalendar_owner_user() {
		return calendar_owner_user;
	}
	public int getCalendar_owner_group() {
		return calendar_owner_group;
	}
	
	public boolean getIs_group_cal() {
		return is_group_cal;
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
