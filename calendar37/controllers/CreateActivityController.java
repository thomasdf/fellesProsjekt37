package controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import utils.DatabaseInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Account;
import models.Activity;

public class CreateActivityController implements Initializable {

	// FXML fields
	@FXML private TextField name;
	@FXML private TextArea description;
	@FXML private DatePicker start_date;
	@FXML private TextField start_hours;
	@FXML private TextField start_minutes;
	@FXML private DatePicker end_date;
	@FXML private TextField end_hours;
	@FXML private TextField end_minutes;
	@FXML private ComboBox room_picker;
	
	private Stage dialogStage;	
	private Stage stage;
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
			LocalTime start = this.parseTime(this.start_hours.getText() + ":" + this.start_minutes.getText());
			LocalTime end = this.parseTime(this.end_hours.getText() + ":" + this.end_minutes.getText());;
			if(start.isAfter(end))	{
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
		if(this.dialogStage != null)	{
			this.dialogStage.close();
		}
		dialogStage = new Stage();
		Stage dialog = dialogStage;
		dialog.initModality(Modality.WINDOW_MODAL);
		VBox box = new VBox();
		Button ok = new Button("OK");
		Text text = new Text(message);
		box.getChildren().addAll(text, ok);
		box.setAlignment(Pos.CENTER);
		ok.setOnAction(new EventHandler<ActionEvent>()	{
			@Override public void handle(ActionEvent e)	{
				closeScene(dialog);
			}
		});
		dialog.setScene(new Scene(box, 300, 100));
		dialog.show();
	}
	
	private String anyIsEmpty()	{
		if(this.name.getText().equals(""))	{
			return "Du må fylle ut et navn til aktiviteten";
		}
		if(this.description.getText().equals(""))	{
			return "Du må fylle ut en beskrivelse for aktiviteten.";
		}
		if(this.start_date.getValue() == null)	{
			return "Du må fylle ut en start dato.";
		}
		if(this.end_date.getValue() == null)	{
			return "Du må fylle ut en slutt dato.";
		}
		if(this.start_hours.getText().equals("") || this.start_minutes.getText().equals("")
				|| this.end_hours.getText().equals("") || this.end_minutes.getText().equals(""))	{
			return "Du må fylle ut tidspunkt for aktiviteten.";
		}
		return "";
	}
	
	private boolean roomIsAvailable()	{
		
		String room_name = this.room_picker.getValue().toString();
		if(!anyIsEmpty().equals(""))	{	
			DatabaseInterface db = new DatabaseInterface();
			ObservableList list_of_available = FXCollections.observableList(db.getAvailableRooms(
					start_date.getValue(), end_date.getValue(),
					parseTime(start_hours.getText() + ":" + start_minutes.getText()),
					parseTime(end_hours.getText() + ":" + end_minutes.getText())));
			if(list_of_available.lastIndexOf(room_name) == -1)	{
				return false;
			}	else	{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Function that creates an activity in the dbInterface if and only if the data is validated correctly
	 * TODO: Let user know if data is not valid
	 * TODO: Burde lukke viewet om en avtale opprettes
	 */
	@FXML private void createActivity()	{
		String room_name = "batcave"; // TODO: not yet defined in view.
		if(dateIsOkay() && timeIsLogical() && this.anyIsEmpty().equals("") && roomIsAvailable())	{
			DatabaseInterface db = new DatabaseInterface(); //TODO: DENN KNEKKER HELE PROGRAMMET MED AT act = null UANSETT
			Activity act = db.setActivity(this.user_name , this.description.getText(), this.start_date.getValue(), this.end_date.getValue(),
					this.parseTime(this.start_hours.getText() + ":" + this.start_minutes.getText()),
					this.parseTime(this.end_hours.getText() + ":" + this.end_minutes.getText()), room_name);
			//this.findInvitedAccounts();
			this.addInvited(fetchedAccounts, act.getActivity_id());
			stage.close();
		}	else	{
			if(!roomIsAvailable())	{
				this.makeDialog("Beklager, rommet du har valgt er ikke tilgjengelig i det valgte tidsrommet.");
				DatabaseInterface db = new DatabaseInterface();
				this.room_picker = FXCollections.observableList(db.getAvailableRooms()); 
			}
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
		if(!timeIsLogical() && (start_date.getValue() == null || end_date.getValue() == null))	{
			makeTimeColor("red");
			return;
		}	else if(timeIsLogical() && (start_date.getValue() == null || end_date.getValue() == null))	{
			makeTimeColor("none");
			return;
		}	else	{
			if(start_date.getValue().isBefore(end_date.getValue()))	{
				makeTimeColor("none");
				return;
			}
			if(start_date.getValue().isEqual(end_date.getValue()))	{
				if(!timeIsLogical())	{
					makeTimeColor("red");
					return;
				}	else	{
					makeTimeColor("none");
					return;
				}
			}
		}
		
	}
	
	
	
		//Litt hacky løsning, men fetchedAccounts fylles med brukerne fra InviteController (Med checked true/false status)
	static ObservableList<Account> fetchedAccounts = FXCollections.observableList(new ArrayList<Account>());
	
	//Denne funksjonen åpner InviteView.FXML som et "modal" vindu.
		@FXML
		private void showInviteWindow(ActionEvent event) throws IOException{
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/InviteView.fxml"));
				Parent root = (Parent) loader.load();
				InviteController controller = (InviteController) loader.getController();
				
				//Lager scenen og stagen
				Scene scene = new Scene(root);
				Stage stage = new Stage();
				
				//test
				DatabaseInterface db = new DatabaseInterface();
				ObservableList<Account> tabledata = FXCollections.observableList(db.getAllAccounts());
				
				//Sender inn en liste med inviterte Accounter (hvis det er noen)
				controller.setTableData(fetchedAccounts, tabledata);
				
				
				//Initializes the stage and shows it
				stage.setTitle("Inviter til aktivitet");
				stage.setScene(scene);
				
				stage.initModality(Modality.WINDOW_MODAL);
				stage.initOwner(
				((Node)event.getSource()).getScene().getWindow() );
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
	
	public void setUserInfo(Stage stage, String user_name) {
		this.stage = stage;
		this.user_name = user_name;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		DatabaseInterface db = new DatabaseInterface();
		ObservableList room_list = FXCollections.observableList(db.getAllRooms());
		room_picker = new ComboBox(room_list);
	}
}
