package views;


import java.net.URL;
import java.util.ResourceBundle;





import models.Person_Test;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class Test_InviteView implements Initializable{
	
	
 
    // The table and columns
    @FXML
    TableView<Person_Test> PersonTable;
 
    @FXML
    TableColumn FirstName;
    @FXML
    TableColumn LastName;
    @FXML
    TableColumn checkBoxTableColumn;
    
    @FXML
    Button CheckEverything;
 
    // The table's data
    ObservableList<Person_Test> data;
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set up the table data
        FirstName.setCellValueFactory(
            new PropertyValueFactory<Person_Test,String>("first_name")
        );
        LastName.setCellValueFactory(
            new PropertyValueFactory<Person_Test,String>("last_name")
        );
 
        data = FXCollections.observableArrayList();
        PersonTable.setItems(data);
    }    
 
    static long nextId = 1;
     
    @FXML
    private void handleButtonAction(ActionEvent event) {
        Person_Test test0 = new Person_Test("Kari","Karisen");
        Person_Test test1 = new Person_Test("Aaron","Xander");
        Person_Test test2 = new Person_Test("Zacharias","Bøllesen");


        data.add(test0);
        data.add(test1);
        data.add(test2);
    }
}



