package controllers;


import java.net.URL;
import java.util.ResourceBundle;

import com.sun.javafx.scene.control.SelectedCellsMap;

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
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class InviteController implements Initializable{
	
	//misc variables
	private String currentTab = "Person";
 
    // The table and columns
    @FXML
    TableView<Account> PersonTable;
    
    @FXML 
    TableView<Group> GroupTable;
 
    @FXML
    TableColumn FirstName;
    @FXML
    TableColumn LastName;
    @FXML
    TableColumn checkBoxTableColumn;
    
    @FXML
    TableColumn GroupName;
    
    @FXML
    TableColumn CheckBoxGroup;
    
    
    @FXML
    Button CheckEverything;
    
    @FXML
    Button invite_btn;
    
    @FXML
    Tab groupTab;
    
    @FXML
    TabPane tabPane;
 
    // The table's data
//    ObservableList<Person_Test> data;
    
    //Store all the accounts from the database in this list:
    ObservableList<Account> PersonData;
    ObservableList<Group> GroupData;
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	//TODO: Implementer henting av ALLE accounts fra databasen (Og sette ALLE brukerne sine CHECKED variabler til false)
    	//TODO: 
    	
    	
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
        PersonData= db.getAllAccounts();
        
        //Get all groups and store them in GroupData
        GroupData = FXCollections.observableArrayList();
        GroupData= db.getAllGroups();
        
        //populate the table with groups / Persons
        PersonTable.setItems(PersonData);
        GroupTable.setItems(GroupData);
        
       
        //Close the initial connection
        db.closeItAll();
    }
 
     
    @FXML
    private void selectAll(ActionEvent event) {
    	if(currentTab=="Person"){
    		for(int i = 0; i < PersonData.size(); i++) {
    			PersonData.get(i).setChecked(true);
    		} 
    	}else {
    			for(int i = 0; i < GroupData.size(); i++) {
        			GroupData.get(i).setChecked(true);
    		}
    	}
    }
    
    @FXML
    private void unselectAll(ActionEvent event) {
    	for(int i = 0; i < PersonData.size(); i++) {
    		PersonData.get(i).setChecked(false);
    	}
    }
    
   
}



