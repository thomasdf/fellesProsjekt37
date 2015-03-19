package controllers;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import utils.DatabaseInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Account;
import models.Activity;

public class CreateActivityController {

	// FXML fields
	@FXML private TextField name;
	@FXML private TextArea description;
	@FXML private DatePicker start_date;
	@FXML private TextField start_hours;
	@FXML private TextField start_minutes;
	@FXML private DatePicker end_date;
	@FXML private TextField end_hours;
	@FXML private TextField end_minutes;
	
	private String user_name;
	
	/**
	 * Checks if date fields are correctly filled out
	 * @return true if start_date is before or equal to end_date
	 */
	private boolean dateIsOkay()	{
		if(this.start_date.getValue() == null || this.end_date.getValue() == null)	{
			return false;
		}
		if(this.start_date.getValue().isBefore(this.end_date.getValue())
				|| this.start_date.getValue().isEqual(this.end_date.getValue()))	{
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
		if(this.start_date.getValue().isEqual(this.end_date.getValue()))	{
			LocalTime start = this.parseTime(this.start_hours.getText() + ":" + this.start_minutes.getText());
			LocalTime end = this.parseTime(this.end_hours.getText() + ":" + this.end_minutes.getText());;
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
		if(time.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]"))	{
			return true;
		}
		return false;
	}
	
	/**
	 * Function that invites all the selected accounts to the activity
	 * @param invited list of all accounts that should be invited
	 * @param activity_id the id of the activity that has been created
	 */
	private void addInvited(ObservableList<Account> invited, int activity_id)	{
		DatabaseInterface db = new DatabaseInterface();
		for(Account invited_account : invited)	{
			db.inviteAccount(activity_id, invited_account.getUsername());
		}
	}
	
	private void closeScene(Stage stage)	{
		stage.close();
	}
	
	private void makeDialog(String message)	{
		Stage dialog = new Stage();
		dialog.initModality(Modality.WINDOW_MODAL);
		VBox box = new VBox();
		Button ok = new Button("OK");
		ok.alignmentProperty();
		box.getChildren().addAll(new Text(message), ok);
		ok.setOnAction(new EventHandler<ActionEvent>()	{
			@Override public void handle(ActionEvent e)	{
				closeScene(dialog);
			}
		});
		dialog.setScene(new Scene(box, 200, 100));
		dialog.show();
	}
	
	//Init
	@FXML void initialize(){
	}
	
	private String anyIsEmpty()	{
		if(this.name.getText().equals(""))	{
			return "You have to fill out a name.";
		}
		if(this.description.getText().equals(""))	{
			return "You have to fill out a description.";
		}
		if(this.start_date.getValue() == null)	{
			return "You have to fill out a start date.";
		}
		if(this.end_date.getValue() == null)	{
			return "You have to fill out an end date.";
		}
		if(this.start_hours.getText().equals("") || this.start_minutes.getText().equals("")
				|| this.end_hours.getText().equals("") || this.end_minutes.getText().equals(""))	{
			return "You have to fill out a time space for your activity to be in.";
		}
		return "";
	}
	
	/**
	 * Function that creates an activity in the dbInterface if and only if the data is validated correctly
	 * TODO: Let user know if data is not valid
	 * TODO: Burde lukke viewet om en avtale opprettes
	 */
	@FXML private void createActivity()	{
		String owner_user_name = user_name;
		String room_name = "batcave"; // TODO: not yet defined in view.
		if(dateIsOkay() && timeIsLogical() && this.anyIsEmpty().equals(""))	{
			DatabaseInterface db = new DatabaseInterface(); //TODO: DENN KNEKKER HELE PROGRAMMET MED AT act = null UANSETT
			Activity act = db.setActivity(owner_user_name , this.description.getText(), this.start_date.getValue(), this.end_date.getValue(),
					this.parseTime(this.start_hours.getText() + ":" + this.start_minutes.getText()),
					this.parseTime(this.end_hours.getText() + ":" + this.end_minutes.getText()), room_name);
			//this.findInvitedAccounts();
			this.addInvited(fetchedAccounts, act.getActivity_id());
		}	else	{
			this.makeDialog(this.anyIsEmpty().equals("") ? "Your time space is not logical." : this.anyIsEmpty());
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
	
	private void makeTimeColor(String color)	{
		String style = "-fx-border-color: " + color;
		start_hours.setStyle(style);
		start_minutes.setStyle(style);
		end_hours.setStyle(style);
		end_minutes.setStyle(style);
	}
	
	/**
	 * Function that changes border of time fields to red and back again according to validation
	 */
	@FXML private void timeChange()	{
		if(start_hours.getText().equals("") || start_minutes.getText().equals("") ||
				end_hours.getText().equals("") || end_minutes.getText().equals(""))	{
			makeTimeColor("none");
			return;
		}
		if(start_date.getValue() == null || end_date.getValue() == null)	{
			makeTimeColor("none");
			return;
		}
		if(start_date.getValue().isBefore(end_date.getValue()))	{
			makeTimeColor("none");
			return;
		}
		if(start_date.getValue().isEqual(end_date.getValue()))	{
			if(!timeIsLogical())	{
				makeTimeColor("red");
			}	else	{
				makeTimeColor("none");
			}
			return;
		}
		
	}
	
		//Litt hacky løsning, men fetchedAccounts fylles med brukerne fra InviteController (Med checked true/false status)
	static ObservableList<Account> fetchedAccounts = FXCollections.observableList(new ArrayList<Account>());
	//Denne funksjonen åpner InviteView.FXML som et "modal" vindu.
	@FXML
	private void showInviteWindow(ActionEvent event) throws IOException{
		Stage Invstage = new Stage();
		Parent root = FXMLLoader.load(InviteController.class.getResource("/views/InviteView.fxml"));
		//Parent root = FXMLLoader.load(getClass().getResource("/views/InviteView.fxml"));
		Invstage.setScene(new Scene(root));
		Invstage.setTitle("Invite");
		Invstage.initModality(Modality.WINDOW_MODAL);
		Invstage.initOwner(
		((Node)event.getSource()).getScene().getWindow() );
		Invstage.show();
	}
	
	@FXML
	private void findInvitedAccounts(){
		// ObservableList<Account> fetchedAccounts = FXCollections.observableList(new ArrayList<Account>());
		System.out.println(fetchedAccounts.get(0).getChecked());
	}
	
	//Denne kalles i InviteController for å sende lista med inviterte brukere inn i denne kontrolleren
	public static void fetchList(ObservableList<Account> a){
		fetchedAccounts = a;
	}
	
	public void setUserInfo(String user_name) {
		this.user_name = user_name;
	}
}
