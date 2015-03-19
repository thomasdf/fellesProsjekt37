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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class CreateGroupController implements Initializable{
	
	String currentTab = "";
	
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
    @FXML TextField GroupName_field;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	// TODO Auto-generated method stub
		
		//listener - listens on tabchange
		tabPane.getSelectionModel().selectedItemProperty().addListener(
    		    new ChangeListener<Tab>() {
    		        @Override
    		        public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
    		            System.out.println("Tab Selection changed to: "+t1.getText());
    		            currentTab = t1.getText();
    		        }
    		    }
    		);
		//----------------------------------------------------------------------------------\\
		
		DatabaseInterface db = new DatabaseInterface();
		
		ObservableList<Account> Accounts = FXCollections.observableArrayList(db.getAllAccounts()); //Inneholder ALLE accountene i databasen
		ObservableList<Group> Groups = FXCollections.observableArrayList(db.getAllGroups()); // Inneholder ALLE gruppene i databasen
		ObservableList<String> list = FXCollections.observableArrayList("");
		for(int x = 0; x < Groups.size(); x++){
			list.add(Groups.get(x).getGroup_name() + " - ID ="+Groups.get(x).getGroup_id());
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
	
	//Knappefunksjoner
//-----------------------------------------------------------------------------------
	 @FXML
	    private void selectAll(ActionEvent event) {
	    	if(currentTab.equals("Gruppe")){
	    		for(int i = 0; i < GroupTable.getItems().size(); i++) {
	    			GroupTable.getItems().get(i).setChecked(true);
	    		} 
	    	}else {
	    			for(int i = 0; i < PersonTable.getItems().size(); i++) {
	    				PersonTable.getItems().get(i).setChecked(true);        			
	    		}
	    	}
	    }
	    
	 @FXML
	    private void unselectAll(ActionEvent event) {
	    	if(currentTab.equals("Gruppe")){
	    		for(int i = 0; i < GroupTable.getItems().size(); i++) {
	    			GroupTable.getItems().get(i).setChecked(false);
	    		} 
	    	}else {
	    			for(int i = 0; i < PersonTable.getItems().size(); i++) {
	    				PersonTable.getItems().get(i).setChecked(false);        			
	    		}
	    	}
	    }
	 
	 @FXML
	 private void cancel(ActionEvent event){
		Stage stage = (Stage) cancel_btn.getScene().getWindow();
	    stage.close();
	 }
//--------------------------------------------------------------------------
	
	private ArrayList<String> findInvited(){
		ObservableList<Account> InvitedAccounts = FXCollections.observableArrayList();
    	ObservableList<Account> InvitedGroupMembers = FXCollections.observableArrayList();
   //------------------Find checked accounts in PersonTable and add them to the final array----------------\\
    	for(int x = 0; x < PersonTable.getItems().size(); x++){
    		if(PersonTable.getItems().get(x).getChecked().equals(true)){
    			InvitedAccounts.add(PersonTable.getItems().get(x));
    		}
    	}
    //------------------Find checked groups in GroupTable and convert its members to Account objects--------\\
    	for(int i = 0; i < GroupTable.getItems().size(); i++) {
    		if(GroupTable.getItems().get(i).getChecked()==true){
    			DatabaseInterface dbi = new DatabaseInterface();
    			ObservableList<Account> accountList = FXCollections.observableArrayList();
    			for (int j = 0; j < GroupTable.getItems().get(i).getMembers().size(); j++) {
    				accountList.add(dbi.getAccount(GroupTable.getItems().get(i).getMembers().get(j)));
    			}
    			InvitedGroupMembers.addAll(accountList);
    		}
    	}
    //-----------------Checks the invited accounts to the invited group members to make sure there are noe duplicates------\\
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
    	for(int x=0; x < InvitedAccounts.size(); x++){
    		System.out.println(InvitedAccounts.get(x).getUsername());
    	}
    	//Finally, return the complete array with all the invited accounts
    	ArrayList<String> Usernames = new ArrayList<String>();
    	for(int x=0; x < InvitedAccounts.size(); x++){
    		Usernames.add(InvitedAccounts.get(x).getUsername());
    	}
    	return Usernames;
	}
	
	
	private int getSupergroupID(String groupname){
		int indexOfId = groupname.lastIndexOf("- ID =") + 6;
		int ID = Integer.parseInt(groupname.substring(indexOfId));
		return ID;
	}
	
	
	@FXML
	private void createGroup(ActionEvent event){
	//-------Checks if you selected a group in the dropdown menu (subgroup)-----\\
		if(subgroup_menu.getValue().equals("")){
			if(GroupName_field.getText().equals("")){
				System.out.println("Du må gi gruppen et navn.");
			} else {
				DatabaseInterface db = new DatabaseInterface();
				db.setGroup(GroupName_field.getText(), findInvited());
			}
	//-------This kicks in if a user chose a group in the dropdown menu-----\\
		} else {
			if(GroupName_field.getText().equals("")){
				System.out.println("Du må gi gruppen et navn.");
			} else {
				DatabaseInterface db = new DatabaseInterface();
				String supergroup_name = (String) subgroup_menu.getValue();
				int SubGroupId = db.setGroup(GroupName_field.getText(), findInvited()).getGroup_id();
				int SuperGroupId = getSupergroupID(supergroup_name);
				db.setSubGroup(SuperGroupId, SubGroupId);
				
			}
		}
		
	}
	
}