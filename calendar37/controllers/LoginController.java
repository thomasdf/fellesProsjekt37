package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import utils.DatabaseInterface;

public class LoginController implements Initializable{

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	@FXML private Button btn_login;
	@FXML private TextField txt_user_name;
	@FXML private PasswordField txt_password;
	@FXML private Label label_error;
	
	@FXML
	public void login(ActionEvent login_event){
		DatabaseInterface databaseinterface = new DatabaseInterface();
		String user_name = txt_user_name.getText();
		String db_password = databaseinterface.getPassword(user_name);
		
		if(!databaseinterface.isUsername(user_name)){//user_name does not exist.
			label_error.setText("Brukernavnet eksisterer ikke. Sjekk at det er skrevet riktig.");
		} else if(db_password.equals(txt_password.getText())){
				//login success-action!
				label_error.setText("login success! horray!");
			} else{
				label_error.setText("Passordet er ikke riktig. Sjekk om det er skrevet riktig.");
			}
		}
}
