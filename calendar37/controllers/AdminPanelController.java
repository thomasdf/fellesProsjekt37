package controllers;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import utils.DatabaseInterface;
import models.Account;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

public class AdminPanelController implements Initializable {

	//fxml
	@FXML TextField txf_au_user_name;
	@FXML TextField txf_au_password;
	@FXML TextField txf_au_first_name;
	@FXML TextField txf_au_last_name;
	@FXML TextField txf_au_phone;
	@FXML Button btn_validate_all;
	@FXML Button btn_add_user;
	@FXML TextField txf_remove_user;
	@FXML Button btn_remove_user;
	@FXML Button btn_close;
	
	Map<TextField, Boolean> validate_all;
	
	DatabaseInterface dbi = new DatabaseInterface();
	
	public void txfUser_nameChangeAdd(ObservableValue<? extends String> property, String oldValue, String newValue){
		txf_au_user_name.setText(txf_au_user_name.getText().toLowerCase().trim());
		validateUser_nameAdd(newValue, txf_au_user_name);
	}
	
	public void txfUser_nameChangeRemove(ObservableValue<? extends String> property, String oldValue, String newValue){
		txf_remove_user.setText(txf_remove_user.getText().toLowerCase().trim());
		validateUser_nameRemove(newValue, txf_remove_user);
	}
	
	public void txfPasswordChange(ObservableValue<? extends String> property, String oldValue, String newValue){
		validateString(newValue, "^[a-z0-9_-]{6,18}$", txf_au_password);
	}
	
	public void txfFirst_nameChange(ObservableValue<? extends String> property, String oldValue, String newValue){
		validateString(newValue, "^[A-ZÆØÅ]{1}[-a-zA-ZæøåÆØÅ ]{0,19}$", txf_au_first_name);
	}
	
	public void txfLast_nameChange(ObservableValue<? extends String> property, String oldValue, String newValue){
		validateString(newValue, "^[A-ZÆØÅ]{1}[-a-zA-ZæøåÆØÅ ]{0,19}$", txf_au_last_name);
	}
	
	public void txfPhoneChange(ObservableValue<? extends String> property, String oldValue, String newValue){
		validateString(newValue, "^[0-9]{8}$", txf_au_phone);
	}
	
	private void validateUser_nameAdd(String user_name, TextField textField) {
		boolean is_valid = !dbi.isUsername(user_name) && !textField.getText().isEmpty() && user_name.matches("^[a-z0-9_-]{1,10}$");
		if (is_valid) {
			textField.setTooltip(null);
			textField.setStyle("-fx-border-color: transparent;");
			validate_all.put(textField, true);
		} else {
			textField.setTooltip(new Tooltip("Brukernavnet finnes fra før av!"));
			textField.setStyle("-fx-border-color: red;");
			validate_all.put(textField, false);
		}
	}
	
	private void validateUser_nameRemove(String user_name, TextField textField) {
		boolean is_valid = dbi.isUsername(user_name) && !textField.getText().isEmpty() && user_name.matches("^[a-z0-9_-]{1,10}$");
		if (is_valid) {
			textField.setTooltip(null);
			textField.setStyle("-fx-border-color: transparent;");
			btn_remove_user.setDisable(false);
		} else {
			textField.setTooltip(new Tooltip("Brukernavnet finnes ikke!"));
			textField.setStyle("-fx-border-color: red;");
			btn_remove_user.setDisable(true);
		}
	}
	
	private void validateString(String value, String regex, TextField textField){
		boolean isValid = value.matches(regex);
		if (isValid){
			textField.setStyle("-fx-border-color: transparent;");
			validate_all.put(textField, true);
		} else {
			textField.setStyle("-fx-border-color: red;");
			validate_all.put(textField, false);
		}
	}
	
	@FXML public void validateAll() {
		boolean is_valid = true;
		for (Entry<TextField, Boolean> entry : validate_all.entrySet()) {
			if (entry.getValue() == false) {
				is_valid = false;
			}
		}
		if (is_valid) {
			btn_add_user.setDisable(false);
		} else {
			btn_add_user.setDisable(true);
		}
	}
	
	@FXML public void addUser() {
		Account new_user = new Account(txf_au_user_name.getText(), txf_au_password.getText(),
				txf_au_first_name.getText(), txf_au_last_name.getText(), txf_au_phone.getText());
		dbi.setAccount(new_user);
		
		for (Entry<TextField, Boolean> entry : validate_all.entrySet()) {
			entry.getKey().clear();
			entry.getKey().setStyle("-fx-border-color: transparent;");
		}
		btn_add_user.setDisable(true);
	}
	
	@FXML public void removeUser() {
		dbi.removeAccount(txf_remove_user.getText());
		txf_remove_user.clear();
		btn_remove_user.setDisable(true);
	}
	
	@FXML public void close() {
		Stage stage = (Stage) btn_close.getScene().getWindow();
		stage.close();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		validate_all = new HashMap<TextField, Boolean>();
		
		txf_au_user_name.setTooltip(new Tooltip("Må være små bokstaver, tall eller -_ og mellom 1-10 tegn lang"));
		validate_all.put(txf_au_user_name, false);
		txf_au_password.setTooltip(new Tooltip("Må være små bokstaver, tall eller -_ og mellom 6-18 tegn lang"));
		validate_all.put(txf_au_password, false);
		txf_au_first_name.setTooltip(new Tooltip("Kan maks være 20 tegn langt og kun bestå av bokstaver"));
		validate_all.put(txf_au_first_name, false);
		txf_au_last_name.setTooltip(new Tooltip("Kan maks være 20 tegn langt og kun bestå av bokstaver"));
		validate_all.put(txf_au_last_name, false);
		txf_au_phone.setTooltip(new Tooltip("Kan kun bestå av tall og må være 8 tegn lang"));
		validate_all.put(txf_au_phone, false);
		txf_remove_user.setTooltip(new Tooltip("Må være små bokstaver, tall eller -_ og mellom 3-15 tegn lang"));
	}
}
