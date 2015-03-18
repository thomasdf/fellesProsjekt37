package models;

import java.util.ArrayList;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Account is the class that keeps a record of everything relating to the account of a person,
 * and is used to identify and authenticate a user.
 * The password is encrypted.
 * 
 * @author gruppe37
 * @version %I%, %G%
 */
public class Account {

	//Final attributes
	private final String username;
	private final String password;
	private final String first_name;
	private final String last_name;
	private final String mobile_nr;
	
	//Oppsett for checkbox i tableview
		//Checked variablen tilsier om denne brukeren er huket av i invite viewet eller ikke
		//Pass på å alltid sette den til "false" for ALLE brukere når du entrer invite viewet.
		//Hvis ikke vil systemet tro at brukeren alltid er huket av, og dermed bli invitert til alt.
	//-------------------------------------------------------
		private SimpleBooleanProperty checked = new SimpleBooleanProperty(false);
		   // other columns here

		    public SimpleBooleanProperty checkedProperty() {
		        return this.checked;
		    }

		    public java.lang.Boolean getChecked() {
		        return this.checkedProperty().get();
		    }

		    public void setChecked(final java.lang.Boolean checked) {
		        this.checkedProperty().set(checked);
		    }
	//---------------------------------------------------
	
	//Property-attributes
	private ObservableList<Integer> in_groupsList = FXCollections.observableList(new ArrayList<Integer>());
	private ObservableList<Integer> calendarsList = FXCollections.observableList(new ArrayList<Integer>());
		//^ muligvis slette denne?
	
	//Constructor
	public Account(String username, String password, String first_name, String last_name, String mobile_nr) {
		this.username = username;
		this.password = password;
		this.first_name = first_name;
		this.last_name = last_name;
		this.mobile_nr = mobile_nr;
	}
	
	//Getters, Setters & Properties
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getFirst_name() {
		return first_name;
	}
	
	public String getLast_name() {
		return last_name;
	}
	
	public String getMobile_nr() {
		return mobile_nr;
	}
	
	public ObservableList<Integer> getIn_groups() {
		return in_groupsList;
	}
	public void addIn_group(int group_id) {
		in_groupsList.add(group_id);
	}
	public void setIn_groups(ArrayList<Integer> in_groups) {
		in_groupsList.addAll(in_groups);
	}
	
	public ObservableList<Integer> getCalendars() {
		return calendarsList;
	}
	public void addCalendar(int calendar_id) {
		calendarsList.add(calendar_id);
	}
	public void setCalendars(ArrayList<Integer> calendars) {
		calendarsList.addAll(calendars);
	}
}
