package views;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Test_AgendaController{
	
	//Deklarerer modellen
	
	//Variabler for ting og tang i FXML-en
	@FXML private ChoiceBox chb_timeframe;
	
	//Listeners
	
	//Bruksmetoder
	
	//Init
	@FXML void initialize(){
		chb_timeframe.setItems(FXCollections.observableArrayList("1 Dag","1 Uke","1 Måned", "1 År", "All tid"));
		chb_timeframe.setValue("1 Måned");
	}
	
	//Vis om teksten er feil
	
	//Funksjoner for FXML-en
}
