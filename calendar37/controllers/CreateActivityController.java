package controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import utils.DatabaseInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import models.Activity;

public class CreateActivityController {
	
	// Model initialization
	private Activity activity;
	
	@SuppressWarnings("rawtypes")
	
	// FXML fields
	@FXML private TextField name;
	@FXML private TextArea description;
	@FXML private DatePicker start_date;
	@FXML private TextField start_hours;
	@FXML private TextField start_minutes;
	@FXML private DatePicker end_date;
	@FXML private TextField end_hours;
	@FXML private TextField end_minutes;
	
	/**
	 * Checks if date fields are correctly filled out
	 * @return true if start_date is before or equal to end_date
	 */
	private boolean dateIsOkay()	{
		if(this.start_date.getValue().isBefore(this.end_date.getValue())
				|| this.start_date.getValue().isEqual(this.end_date.getValue()))	{
			System.out.println("We run this");
			return true;
		}
		if(this.start_date.getValue().isEqual(this.end_date.getValue()))	{
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if timefields are filled out correctly
	 * @return true if time fields are correct, false otherwise
	 */
	private boolean timeIsLogical()	{
		if(!checkTimeString(start_hours.getText() + ":" + start_minutes.getText()) || !checkTimeString(end_hours.getText() + ":" + end_minutes.getText()))	{
			return false;
		}
		if(!this.dateIsOkay())	{
			System.out.println("Hello");
			return false;
		}	else if(this.start_date.getValue().isEqual(this.end_date.getValue()))	{
			LocalTime start = this.parseTime(this.start_hours.getText() + ":" + this.start_minutes.getText());
			LocalTime end = this.parseTime(this.end_hours.getText() + ":" + this.end_minutes.getText());
			System.out.println(start.isAfter(end));
			if(start.isAfter(end))	{
				return false;
			}
		}
		return true;
	}
	
	private LocalTime parseTime(String time)	{
		DateTimeFormatter timeParser = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime returner = LocalTime.parse(time, timeParser);
		return returner;
	}
	
	private boolean checkTimeString(String time)	{
		if(time.matches("([0-2][0-9][2][0-3]):([0-5][0-9])"))	{
			return true;
		}
		return false;
	}
	
	/**
	 * Function that invites all the selected accounts to the activity
	 * @param invited list of all accounts that should be invited
	 * @param activity_id the id of the activity that has been created
	 */
	private void addInvited(ObservableList<String> invited, int activity_id)	{
		DatabaseInterface db = new DatabaseInterface();
		for(String invited_account_username : invited)	{
			db.inviteAccount(activity_id, invited_account_username);
		}
	}
	
	//Init
	@SuppressWarnings("unchecked")
	@FXML void initialize(){
		
	}
	
	/**
	 * Function that creates an activity in the dbInterface if and only if the data is validated correctly
	 * TODO: Let user know if data is not valid
	 */
	@FXML private void createActivity()	{
		String owner_user_name = "Get username from the program"; // TODO: get the username from somewhere in the nameSpace.
		String room_name = "Get the room name from the view"; // TODO: not yet defined in view.
		ObservableList<String> list = FXCollections.observableList(new ArrayList<String>()); // TODO: this is where we get the list of accounts, must be checked with the database
		if(dateIsOkay() && timeIsLogical())	{
			DatabaseInterface db = new DatabaseInterface();
			Activity act = db.setActivity(owner_user_name , this.description.getText(), this.start_date.getValue(), this.end_date.getValue(),
					this.parseTime(this.start_hours.getText() + ":" + this.start_minutes.getText()),
					this.parseTime(this.end_hours.getText() + ":" + this.end_minutes.getText()), room_name);
			this.addInvited(list, act.getActivity_id());
		}
	}
	
	/**
	 * Function that changes border to date and time fields red and back again according to validation
	 */
	@FXML private void dateChange()	{
		if(this.start_date.getValue() == null || this.end_date.getValue() == null)	{
			return;
		}
		if(!dateIsOkay())	{
			this.start_date.setStyle("-fx-border-color: red");
			this.end_date.setStyle("-fx-border-color: red");
		}	else	{
			this.start_date.setStyle("-fx-border-color: none");
			this.end_date.setStyle("-fx-border-color: none");
		}
		this.timeChange();
	}
	
	/**
	 * Function that changes border of time fields to red and back again according to validation
	 */
	@FXML private void timeChange()	{
		if(start_hours.getText().equals("") && start_minutes.getText().equals("") && end_hours.getText().equals("") && end_minutes.getText().equals(""))	{
			this.start_hours.setStyle("-fx-border-color: none");
			this.start_minutes.setStyle("-fx-border-color: none");
			this.end_hours.setStyle("-fx-border-color: none");
			this.end_minutes.setStyle("-fx-border-color: none");
		}
		if(start_hours.getText().equals("") || start_minutes.getText().equals("") ||
				end_hours.getText().equals("") || end_minutes.getText().equals(""))	{
			return;
		}
		if(this.start_date.getValue() == null || this.end_date.getValue() == null)	{
			return;
		}
		if(this.start_date.getValue().isEqual(this.end_date.getValue()))	{
			if(!timeIsLogical())	{
				System.out.println(this.start_date.getValue().isEqual(this.end_date.getValue()));
				this.start_hours.setStyle("-fx-border-color: red");
				this.start_minutes.setStyle("-fx-border-color: red");
				this.end_hours.setStyle("-fx-border-color: red");
				this.end_minutes.setStyle("-fx-border-color: red");
			}	else	{
				this.start_hours.setStyle("-fx-border-color: none");
				this.start_minutes.setStyle("-fx-border-color: none");
				this.end_hours.setStyle("-fx-border-color: none");
				this.end_minutes.setStyle("-fx-border-color: none");
			}
		}
	}
	
	
}
