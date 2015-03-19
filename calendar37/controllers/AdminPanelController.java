package controllers;

import utils.DatabaseInterface;
import models.Account;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

public class AdminPanelController {

	//fxml
	@FXML TextField txf_au_user_name;
	@FXML TextField txf_au_password;
	@FXML TextField txf_au_first_name;
	@FXML TextField txf_au_last_name;
	@FXML TextField txf_au_phone;
	@FXML Button btn_add_user;
	@FXML TextField txf_remove_user;
	@FXML Button btn_remove_user;
	@FXML Button btn_close;
	
	DatabaseInterface dbi = new DatabaseInterface();
	
	public void txfUser_nameChangeAdd(ObservableValue<? extends String> property, String oldValue, String newValue){
		txf_au_user_name.setText(txf_au_user_name.getText().toLowerCase().trim());
		validateUser_nameAdd(newValue, txf_au_user_name);
	}
	
	public void txfUser_nameChangeRemove(ObservableValue<? extends String> property, String oldValue, String newValue){
		txf_remove_user.setText(txf_remove_user.getText().toLowerCase().trim());
		validateUser_nameRemove(newValue, txf_remove_user);
	}
	
	private void validateUser_nameAdd(String user_name, TextField field) {
		boolean is_valid = !dbi.isUsername(user_name) && !field.getText().isEmpty();
		if (is_valid) {
			field.setTooltip(null);
			field.setStyle("-fx-border-color: transparent;");
			btn_add_user.disableProperty().set(false);
		} else {
			field.setTooltip(new Tooltip("Brukernavnet finnes fra før av!"));
			field.setStyle("-fx-border-color: red;");
			btn_add_user.disableProperty().set(true);
		}
	}
	
	private void validateUser_nameRemove(String user_name, TextField field) {
		boolean is_valid = dbi.isUsername(user_name) && !field.getText().isEmpty();
		if (is_valid) {
			field.setTooltip(null);
			field.setStyle("-fx-border-color: transparent;");
			btn_remove_user.disableProperty().set(false);
		} else {
			field.setTooltip(new Tooltip("Brukernavnet finnes ikke!"));
			field.setStyle("-fx-border-color: red;");
			btn_remove_user.disableProperty().set(true);
		}
	}
	
	@FXML public void addUser() {
		Account new_user = new Account(txf_au_user_name.getText(), txf_au_password.getText(),
				txf_au_first_name.getText(), txf_au_last_name.getText(), txf_au_phone.getText());
		dbi.setAccount(new_user);
	}
	
	@FXML public void removeUser() {
		dbi.removeAccount(txf_remove_user.getText());
	}
	
	@FXML public void close() {
		Stage stage = (Stage) btn_close.getScene().getWindow();
		stage.close();
	}
}
