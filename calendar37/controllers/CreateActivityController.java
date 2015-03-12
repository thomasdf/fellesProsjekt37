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
	
	//Deklarerer modellen
	private Activity activity;
	
	//Variabler for ting og tang i FXML-en
	@SuppressWarnings("rawtypes")
	@FXML private TextField name;
	@FXML private TextArea description;
	@FXML private DatePicker start_date;
	@FXML private TextField start_hours;
	@FXML private TextField start_minutes;
	@FXML private DatePicker end_date;
	@FXML private TextField end_hours;
	@FXML private TextField end_minutes;
		
	//Listenerss
		
	//Bruksmetoder
	private boolean timeOKay()	{
		if(start_date.getValue().isEqual(end_date.getValue()))	{
			if(checkTimeString((start_hours.getText() + start_minutes.getText())))	{
				if(parseTime(start_hours.getText() + start_minutes.getText()).isAfter(parseTime(end_hours.getText() + ":" + end_minutes.getText())))	{
					return false;
				}
			}
		}	else if(start_date.getValue().isAfter(end_date.getValue()))	{
			return false;
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
	//Init
	@SuppressWarnings("unchecked")
	@FXML void initialize(){
		
	}
		
	//Vis om teksten er feil
			
	//Funksjoner for FXML-en
	private void createActivity()	{
		if(timeOKay())	{
			DatabaseInterface db = new DatabaseInterface();
			db.setActivity(/*owner_user_name goes here when I know how to get it */, description.getText(), start_date.getValue(), end_date.getValue(), parseTime((start_hours.getText() + ":" + start_minutes.getText())), parseTime((end_hours.getText() + ":" + end_minutes.getText())), /*room_name goes here when we add this to the view*/);
		}
	}
	
	private void addInvited(ObservableList<String> invited, int activity_id)	{
		DatabaseInterface db = new DatabaseInterface();
		for(String invited_account_username : invited)	{
			db.inviteAccount(activity_id, invited_account_username);
		}
	}
}
