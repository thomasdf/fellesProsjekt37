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
		String databasepassword = databaseinterface.getPassword(user_name);
		
		if(databasepassword.equals(null)){
			label_error.setText("Brukernavnet eksisterer ikke. Sjekk at det er skrevet riktig. Kontakt systemadministrator om problemet vedvarer.");
		} else{
			if(databasepassword.equals(txt_password.getText())){//user_name and password are correct. Login success
				//login success-action!
				label_error.setText("login success! horray!");
			} else{
				label_error.setText("Passordet er ikke riktig. Sjekk om det er skrevet riktig. Kontakt systemadministrator om problemet vedvarer.");
			}
		}
	}
}
