package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import utils.DatabaseInterface;
import models.Account;
import models.Group;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class InviteController implements Initializable{

	//misc variables
	private String currentTab = "Person";
 
    // The table and columns
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

    //Store all the accounts from the database in this list:
    ObservableList<Account> PersonData;
    ObservableList<Group> GroupData;
     
	@SuppressWarnings("unchecked")
	@Override
    public void initialize(URL url, ResourceBundle rb) {
    	//TODO: Implementer henting av ALLE accounts fra databasen (Og sette ALLE brukerne sine CHECKED variabler til false)
		//TODO: KRÆSJER OM MAN UNCHECKER ALLE, OG INVITERER FOLK FLERE GANGER
    	tabPane.getSelectionModel().selectedItemProperty().addListener(
    		    new ChangeListener<Tab>() {
    		        @Override
    		        public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
    		            System.out.println("Tab Selection changed to: "+t1.getText());
    		            currentTab = t1.getText();
    		        }
    		    }
    		);
    	
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
        
        //Create a connection to the databse interface
        DatabaseInterface db = new DatabaseInterface();
        
        //get all accounts and store them in PersonData
        PersonData = FXCollections.observableArrayList();
        PersonData= FXCollections.observableList(db.getAllAccounts());
        
        //Get all groups and store them in GroupData
        GroupData = FXCollections.observableArrayList();
        GroupData= FXCollections.observableList(db.getAllGroups());
        
        //populate the table with groups / Persons
        PersonTable.setItems(PersonData);
        GroupTable.setItems(GroupData);
    }
	
    @FXML
    private void selectAll(ActionEvent event) {
    	if(currentTab.equals("Person")){
    		for(int i = 0; i < PersonData.size(); i++) {
    			PersonData.get(i).setChecked(true);
    		} 
    	}else {
    			for(int i = 0; i < GroupData.size(); i++) {
        			GroupData.get(i).setChecked(true);
        			System.out.println(GroupData.get(i).getGroup_name());
    		}
    	}
    }
    
    @FXML
    private void unselectAll(ActionEvent event) {
    	if(currentTab=="Person"){
    		for(int i = 0; i < PersonData.size(); i++) {
    			PersonData.get(i).setChecked(false);
    		}} else {
    			for(int i = 0; i < PersonData.size(); i++) {
        			GroupData.get(i).setChecked(false);
    		}
    			
    	}
    }
    
    //findInvited vil finne alle de inviterte account objektene (ogs� via inviterte grupper) og returnere en ObservableList
    @FXML
    private ObservableList<Account> findInvited(){
    	ObservableList<Account> InvitedAccounts = FXCollections.observableArrayList();
    	ObservableList<Account> InvitedGroupMembers = FXCollections.observableArrayList();
    	for(int i = 0; i < PersonData.size(); i++) {
			if(PersonData.get(i).getChecked()==true){
				System.out.println(PersonData.get(i).getUsername() + " is marked as invited.");
				InvitedAccounts.add(PersonData.get(i));
			} 
			
    	}
    	for(int i = 0; i < GroupData.size(); i++) {
    		if(GroupData.get(i).getChecked()==true){
    			DatabaseInterface dbi = new DatabaseInterface();
    			ObservableList<Account> accountList = FXCollections.observableArrayList();
    			for (int j = 0; j < GroupData.get(i).getMembers().size(); j++) {
    				accountList.add(dbi.getAccount(GroupData.get(i).getMembers().get(i)));
    			}
    			InvitedGroupMembers.addAll(accountList);
    		}
    	}
    	for (int i = 0; i < InvitedGroupMembers.size(); i++) {
    		if(InvitedAccounts.contains(InvitedGroupMembers.get(i))){
    			System.out.println(InvitedGroupMembers.get(i).getFirst_name() + " is already invited");
    		} else {
    			InvitedAccounts.add(InvitedGroupMembers.get(i));
    			System.out.println(InvitedGroupMembers.get(i).getFirst_name() + " is marked as Invited");
    		}
    	}
    	return InvitedAccounts;
    }
    
    //CloseWindow vil lukke invite vinduet og sende en liste med inviterte brukere tilbake til "CreateActivity" kontrolleren
    @FXML
    private void closeWindow(ActionEvent event){
    	CreateActivityController.fetchList(findInvited());
    	
    	// get a handle to the stage
        Stage stage = (Stage) invite_btn.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
}
