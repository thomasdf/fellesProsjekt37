package controllers;

import utils.DatabaseInterface;
import models.Account;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AccountController {

	//fxml
	@FXML TextField txf_full_name;
	@FXML TextField txf_phone;
	@FXML Button btn_close;
	
	public void setAccountView(String user_name) {
		DatabaseInterface dbi = new DatabaseInterface();
		Account model = dbi.getAccount(user_name);
		
		txf_full_name.setText(model.getFirst_name() + " " + model.getLast_name());
		txf_phone.setText(model.getMobile_nr());
	}
	
	@FXML public void close() {
		Stage stage = (Stage) btn_close.getScene().getWindow();
		stage.close();
	}
}
