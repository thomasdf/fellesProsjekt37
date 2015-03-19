package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import utils.DatabaseInterface;
import models.Account;
import models.Group;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class CreateGroupController implements Initializable{

	@FXML TableView<Account> PersonTable;
    @FXML TableView<Group> GroupTable;
    @SuppressWarnings("rawtypes")
	@FXML TableColumn FirstName;
    @SuppressWarnings("rawtypes")
    @FXML TableColumn LastName;
    @SuppressWarnings("rawtypes")
    @FXML TableColumn checkBoxTableColumn;
    @SuppressWarnings("rawtypes")
    @FXML TableColumn GroupName;
    @SuppressWarnings("rawtypes")
    @FXML TableColumn checkBoxGroup;
    @FXML Button CheckEverything;
    @FXML Button invite_btn;
    @FXML Tab groupTab;
    @FXML TabPane tabPane;
    @FXML Button cancel_btn;
    @SuppressWarnings("rawtypes")
	@FXML ComboBox subgroup_menu;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	// TODO Auto-generated method stub
		
		DatabaseInterface db = new DatabaseInterface();
		
		ObservableList<Account> Accounts = FXCollections.observableArrayList(db.getAllAccounts()); //Inneholder ALLE accountene i databasen
		ObservableList<Group> Groups = FXCollections.observableArrayList(db.getAllGroups()); // Inneholder ALLE gruppene i databasen
		ObservableList<String> list = FXCollections.observableArrayList();
		for(int x = 0; x < Groups.size(); x++){
			list.add(Groups.get(x).getGroup_name());
		}
		
		// Set up the table data
        FirstName.setCellValueFactory(
            new PropertyValueFactory<Account,String>("first_name")
        );
        LastName.setCellValueFactory(
            new PropertyValueFactory<Account,String>("last_name")
        );
        GroupName.setCellValueFactory(
        	new PropertyValueFactory<Group,String>("group_name")
        );
        
		
        //Sett verdiene inn i tabellen
		PersonTable.setItems(Accounts);
		GroupTable.setItems(Groups);
		
		
		subgroup_menu.getItems().addAll(list);

	}
	
	@FXML
	private void createGroup(ActionEvent event){
		
	}
	
}