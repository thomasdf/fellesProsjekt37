package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
	String currentTab = "";
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
    @FXML Button cancel_btn;

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
        
        
        
        //Get all groups and store them in GroupData
        GroupData = FXCollections.observableArrayList();
        GroupData= FXCollections.observableList(db.getAllGroups());
       
        
        
      
        //populate the table with groups / Persons
//        PersonTable.setItems(PersonData);
        GroupTable.setItems(GroupData);
    }

	//-----Mottar en liste med Accounter som er inviterte til aktiviteten (hvis et er noen, ellers er den tom)
	ObservableList<Account> InvitedAccounts = FXCollections.observableArrayList();
	
	
	public void setTableData(ObservableList<Account> list, ObservableList<Account> PersonData){
		this.InvitedAccounts.addAll(list);
		for(int i = 0; i < InvitedAccounts.size(); i++) {
			for(int y = 0; y < PersonData.size(); y++){
				if(InvitedAccounts.get(i).getUsername().equals(PersonData.get(y).getUsername())){
					PersonData.get(y).setChecked(true);
					
				}
			}
		}
		
		PersonTable.setItems(PersonData);
	}
	
	public ObservableList<Account> getTableData(){
		return PersonTable.getItems();
	}
	
    @FXML
    private void selectAll(ActionEvent event) {
    	if(currentTab.equals("Gruppe")){
    		for(int i = 0; i < GroupData.size(); i++) {
    			GroupData.get(i).setChecked(true);
    		} 
    	}else {
    			for(int i = 0; i < getTableData().size(); i++) {
        			getTableData().get(i).setChecked(true);        			
    		}
    	}
    }
    
    @FXML
    private void unselectAll(ActionEvent event) {
    	if(currentTab.equals("Gruppe")){
    		for(int i = 0; i < GroupData.size(); i++) {
    			GroupData.get(i).setChecked(false);
    		} 
    	}else {
    			for(int i = 0; i < getTableData().size(); i++) {
        			getTableData().get(i).setChecked(false);        			
    		}
    	}
    }
    
    //findInvited vil finne alle de inviterte account objektene (ogs� via inviterte grupper) og returnere en ObservableList
    @FXML
    private ObservableList<Account> findInvited(){
    	ObservableList<Account> InvitedAccounts = FXCollections.observableArrayList();
    	ObservableList<Account> InvitedGroupMembers = FXCollections.observableArrayList();
    	for(int i = 0; i < getTableData().size(); i++) {
			if(getTableData().get(i).getChecked()==true){
//				System.out.println(getTableData().get(i).getUsername() + " is marked as invited.");
				InvitedAccounts.add(getTableData().get(i));
			} 
			
    	} //Henter ut alle Account objektene knyttet til gruppene som er huket av og lagrer i "InvitedGroupMembers"
    	for(int i = 0; i < GroupData.size(); i++) {
    		if(GroupData.get(i).getChecked()==true){
    			DatabaseInterface dbi = new DatabaseInterface();
    			ObservableList<Account> accountList = FXCollections.observableArrayList();
    			for (int j = 0; j < GroupData.get(i).getMembers().size(); j++) {
    				accountList.add(dbi.getAccount(GroupData.get(i).getMembers().get(j)));
    			}
    			InvitedGroupMembers.addAll(accountList);
    		}
    	} //Finner ut om noen i gruppen allerede er inviterte. 
    	List<String> list = new ArrayList<String>();
    	for(int i=0; i < InvitedAccounts.size(); i++){
    		list.add(InvitedAccounts.get(i).getUsername());
    	}
		
    	for(int x=0; x < InvitedGroupMembers.size(); x++){
    		if(list.contains(InvitedGroupMembers.get(x).getUsername())){
    			
    		} else {
    			InvitedAccounts.add(InvitedGroupMembers.get(x));
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
    
    @FXML
    private void cancel(ActionEvent event){
    	Stage stage = (Stage) cancel_btn.getScene().getWindow();
    	stage.close();
    }
}
