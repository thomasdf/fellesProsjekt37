package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.DatabaseInterface;

public class LoginController implements Initializable {

	@FXML
	private Button btn_login;
	@FXML
	private TextField txt_user_name;
	@FXML
	private PasswordField txt_password;
	@FXML
	private Label label_error;
	
	private Stage stage;
	private boolean is_ok = false;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//Listener for textChange in the user_name-textfield.
		txt_user_name.textProperty().addListener(new ChangeListener<String>() {
		@Override
	    public void changed(ObservableValue<? extends String> observable,
	            String oldValue, String newValue) {
			txt_user_name.setText(newValue.toLowerCase());
		}
	});
		
	}
	
	@FXML
	public void login(ActionEvent login_event) {
		DatabaseInterface databaseinterface = new DatabaseInterface();
		String user_name = txt_user_name.getText();
		String password = txt_password.getText();
		String db_password = databaseinterface.getPassword(user_name);

		if ((user_name.equals("") || user_name.isEmpty())
				&& (password.equals("") || password.isEmpty())) {
			label_error.setText("Vennligst fyll inn brukernavn og passord");
		} else {
			if (!databaseinterface.isUsername(user_name)) {// user_name does not
															// exist.
				label_error
						.setText("Brukernavnet eksisterer ikke. Sjekk at det er skrevet riktig.");
			} else if (db_password.equals(txt_password.getText())) {
				// login success-action!
				label_error.setText("login success! horray!");
				is_ok = true;
				stage.close();
			} else {
				label_error
						.setText("Passordet er ikke riktig. Sjekk om det er skrevet riktig.");
			}
		}
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public boolean isOk() {
		return is_ok;
	}
	
	public String getUser_name() {
		return txt_user_name.getText();
	}
}
