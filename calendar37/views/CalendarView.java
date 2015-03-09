package views;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class CalendarView extends Application {

	String viewName = "Calendar";
	
	//The model for this view
	models.Calendar model;
	
	//Variables we need defined outside the "start"-function
		//View-elements
	GridPane calendar = new GridPane();
	HBox header = new HBox();
	Label cal_title = new Label("<kalendernavn>");
	Label cur_month = new Label("<måned>");
	Label cur_year = new Label("<år>");
	Button prev_month = new Button("Forrige");
	Button next_month = new Button("Neste");
	HBox footer = new HBox();
	Button profile = new Button("Min profil");
	Button tasks = new Button("Aktivitets-agenda");
	
		//Lists containing current days and activities
	ArrayList<Label> days = new ArrayList<Label>();
	ArrayList<VBox> day_activities = new ArrayList<VBox>();
	
		//Useful final variables
	final List<String> months = Arrays.asList("Januar", "Februar", "Mars", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Desember");
	final List<String> weekdays = Arrays.asList("Søndag", "Mandag", "Tirsdag", "Onsdag", "Torsdag", "Fredag", "Lørdag");
	
	//Set the values for THIS view, updateable:
	Calendar cal = Calendar.getInstance();
	int year = cal.get(Calendar.YEAR);
	int month = cal.get(Calendar.MONTH);
	int days_in_month = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	int first_day_in_month = getFirstDayInMonth();
	int start_index = first_day_in_month != 1 ? first_day_in_month - 2 : 6;
	int end_index = start_index + days_in_month - 1;
	
	@Override public void start(Stage primaryStage) throws Exception {
		//TESTPRINTING
		System.out.println("year=" + year
				+ "\nmonth=" + month + "=" + getMonth(month)
				+ "\ndays_in_month=" + days_in_month
				+ "\nfirst_day_in_month=" + first_day_in_month + "=" + getWeekday(first_day_in_month)
				+ "\nstart_index=" + start_index
				+ "\nend_index=" + end_index
		);
		//TESTPRINTING
		
		//Sets the root
		AnchorPane root = new AnchorPane();
		
		
		//Add style classes
		header.getStyleClass().add("header");
		cal_title.getStyleClass().add("title");
		cur_month.getStyleClass().add("month_year");
		cur_year.getStyleClass().add("month_year");
		prev_month.getStyleClass().add("left");
		next_month.getStyleClass().add("right");
		footer.getStyleClass().add("footer");
		
		//Add actions
		prev_month.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				updateActivities();
			}
		});
		next_month.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				updateActivities();
			}
		});
		
		//The view
			//General restraints
		AnchorPane.setTopAnchor(calendar, 64.0);
		AnchorPane.setLeftAnchor(calendar, 0.0);
		AnchorPane.setTopAnchor(header, 0.0);
		AnchorPane.setLeftAnchor(header, 0.0);
		AnchorPane.setBottomAnchor(footer, 0.0);
		AnchorPane.setLeftAnchor(footer, 0.0);
			//header
		header.getChildren().addAll(cal_title, cur_month, cur_year, prev_month, next_month);
			//footer
		profile.setPrefWidth(256);
		profile.setPrefHeight(16);
		tasks.setPrefWidth(256);
		tasks.setPrefHeight(16);
		footer.getChildren().addAll(profile, tasks);
			//calendar
		calendar.setId("calendar");
			//the days
		for (int i = 0; i < weekdays.size(); i++) {
			Label weekday = new Label(weekdays.get(i));
			weekday.getStyleClass().add("weekday");
			calendar.add(weekday, i, 0);
			GridPane.setHgrow(weekday, Priority.ALWAYS);
		}
		for (int i = 1; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				Label day_title = new Label("<date>");
				VBox day_body = new VBox(2.0);
				VBox day = new VBox(4.0);
				days.add(day_title);
				day_activities.add(days.indexOf(day_title), day_body);
				day.setPrefHeight(9999);
				day.getStyleClass().add("day");
				day.getChildren().addAll(day_title, day_body);
				calendar.add(day, j, i);
				GridPane.setHgrow(day, Priority.ALWAYS);
				day_title.getStyleClass().add("date");
			}
		}
		
		
		//TESTVALUES:
		String t_year = "2015";
		String t_month = "Mars";
		String t_next_month = "April";
		
		cal_title.setText("Gruppe 37s kalender");
		cur_month.setText(t_month + " " + t_year);
		for (int i = 0; i < days.size(); i++){
			int index = i + 25;
			Label title = days.get(i);
			String filler = "";
			if (index == 31){
				filler += t_month.substring(0, 3) + " ";
			} else if (index == 62){
				filler += t_next_month.substring(0, 3) + " ";
			}
			filler += Integer.toString(index%31 + 1);
			title.setText(filler);
			if (index < 31 || index > 61){
				title.setOpacity(0.5);
				day_activities.get(i).setOpacity(0.5);
			}
		}
		VBox container = day_activities.get(4);
		Button t_act1 = new Button("16:00 - Møte med sjefen");
		Button t_act2 = new Button("20:00 - Middag med kona");
		container.getChildren().addAll(t_act1, t_act2);
		container = day_activities.get(16);
		t_act1 = new Button("Besøk av tante");
		t_act2 = new Button("10:30 - Uksemøte");
		container.getChildren().addAll(t_act1, t_act2);
		container = day_activities.get(40);
		t_act1 = new Button("Fest med undassene");
		t_act2 = new Button("Nach med faglærer");
		container.getChildren().addAll(t_act1, t_act2);
		
		
		//Add all nodes and init the scene
		root.getChildren().addAll(header, calendar, footer);
		Scene scene = new Scene(root, 1600, 1024);
		
		//Adds a css-stylesheet to the scene
		scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
		
		//Initializes the stage and shows it
		primaryStage.setTitle(viewName);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	//Set up bindings and listeners:
	ChangeListener<ArrayList<Integer>> activitiesChangeListener = (property, oldValue, newValue) -> { updateActivities(); };
	
	public void setModel(models.Calendar model) {
		if (this.model != null) {
			model.activitiesProperty().removeListener(activitiesChangeListener);
		}
		this.model = model;
		updateActivities();
		if (this.model != null) {
			model.activitiesProperty().addListener(activitiesChangeListener);
		}
	}
	
	private void fillCalendar() {
		
	}
	
	private void updateActivities() {
		for (int i = 0; i < days.size(); i++){
			day_activities.get(i).getChildren().clear();
		}
	}

	private int getFirstDayInMonth() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 1);
		return c.get(Calendar.DAY_OF_WEEK);
	}
	
	private String getMonth(int i){
		return months.get(i);
	}
	
	private String getWeekday(int i){
		return weekdays.get(i - 1);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
