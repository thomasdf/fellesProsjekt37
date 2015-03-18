package controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ResourceBundle;

import models.Activity;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.text.Text;

public class ActivityController implements Initializable{

	
	//fxml
	@FXML
	Text ActivityView_DescriptionText;
	
	@FXML
	DatePicker ActivityView_StartDate;
	
	@FXML
	Text ActivityView_StartTime;
	
	@FXML
	Text ActivityView_EndTime;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Lager et Activity-objekt for test årsaker.
		//TODO: Bytt dette ut med henting fra databasen isteden.
		Activity TestActivity = new Activity(0);
		TestActivity.setDescription("Lek og kos for store og små :)");
		TestActivity.setRoom("Room 431");
		TestActivity.setDate(LocalDate.of(2015, Month.NOVEMBER, 20));
		TestActivity.setFrom(LocalTime.NOON);
		TestActivity.setTo(LocalTime.MIDNIGHT);
//		--------------------TEST slutt--------------
		ActivityView_DescriptionText.setText(TestActivity.getDescription());
		ActivityView_StartDate.setValue(TestActivity.getDate());
		ActivityView_StartDate.setDisable(true);
		ActivityView_StartTime.setText(TestActivity.getFrom().toString());
		ActivityView_EndTime.setText(TestActivity.getTo().toString());
		
		
		
	}
	
}
