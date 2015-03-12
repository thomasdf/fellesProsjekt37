package models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class Person_Test {
	
	public Person_Test(String firstname, String lastname){
		first_name.set(firstname);
		last_name.set(lastname);
	}
	
	//Oppsett for checkbox i tableview
//-------------------------------------------------------
	private SimpleBooleanProperty checked = new SimpleBooleanProperty(false);
	   // other columns here

	    public SimpleBooleanProperty checkedProperty() {
	        return this.checked;
	    }

	    public java.lang.Boolean getChecked() {
	        return this.checkedProperty().get();
	    }

	    public void setChecked(final java.lang.Boolean checked) {
	        this.checkedProperty().set(checked);
	    }
//---------------------------------------------------
	    
        public SimpleStringProperty first_name = new SimpleStringProperty();
        public SimpleStringProperty last_name = new SimpleStringProperty();
         
 
        public String getFirst_name() {
            return first_name.get();
        }
 
        public String getLast_name() {
            return last_name.get();
        }
 
    }
