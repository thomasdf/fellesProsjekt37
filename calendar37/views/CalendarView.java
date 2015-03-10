package views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class CalendarView extends Application {

	private String viewName = "Calendar";
	
	//The model for this view
	private models.Calendar model;
	
	//Variables we need defined outside the "start"-function
		//View-elements
	private GridPane calendar = new GridPane();
	private HBox header = new HBox();
	private Label cal_title = new Label("<kalendernavn>");
	private Label cur_month_year = new Label("<måned år>");
	private Button prev_month = new Button("Forrige");
	private Button next_month = new Button("Neste");
	private HBox footer = new HBox();
	private Button profile = new Button("Min profil");
	private Button tasks = new Button("Aktivitets-agenda");
	
		//Lists containing current days and activities
	private ArrayList<Label> days = new ArrayList<Label>();
	private ArrayList<VBox> day_activities = new ArrayList<VBox>();
	
		//Useful final variables
	private final List<String> months = Arrays.asList("Januar", "Februar", "Mars", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Desember");
	private final List<String> weekdays = Arrays.asList("Mandag", "Tirsdag", "Onsdag", "Torsdag", "Fredag", "Lørdag", "Søndag");
	
	//Set the values for THIS view, updateable:
	Calendar cal = Calendar.getInstance();
	int start_index = getFirstDayInMonth(cal.get(Calendar.YEAR)
			, cal.get(Calendar.MONTH)) != 1 ? getFirstDayInMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)) - 2 : 6;
	int end_index = start_index + cal.getActualMaximum(Calendar.DAY_OF_MONTH) - 1;
	
	Calendar prev_cal = new GregorianCalendar((cal.get(Calendar.MONTH) - 1)%12 == 0 ? cal.get(Calendar.YEAR) - 1 : cal.get(Calendar.YEAR)
			, (cal.get(Calendar.MONTH) - 1)%12, 1);
	
	Calendar next_cal = new GregorianCalendar((cal.get(Calendar.MONTH) + 1)%12 == 0 ? cal.get(Calendar.YEAR) + 1 : cal.get(Calendar.YEAR)
			, (cal.get(Calendar.MONTH) + 1)%12, 1);
	
	private void setMonth(int diff) {
		cal.set((cal.get(Calendar.MONTH) + diff)%12 == 0 && cal.get(Calendar.MONTH) != 1 ? cal.get(Calendar.YEAR) + diff : cal.get(Calendar.YEAR)
				, (cal.get(Calendar.MONTH) + diff)%12, 1);
		start_index = getFirstDayInMonth(cal.get(Calendar.YEAR)
				, cal.get(Calendar.MONTH)) != 1 ? getFirstDayInMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)) - 2 : 6;
		end_index = start_index + cal.getActualMaximum(Calendar.DAY_OF_MONTH) - 1;
		
		prev_cal.set((cal.get(Calendar.MONTH) - 1)%12 == 0 ? cal.get(Calendar.YEAR) - 1 : cal.get(Calendar.YEAR)
				, (cal.get(Calendar.MONTH) - 1)%12, 1);
		
		next_cal.set((cal.get(Calendar.MONTH) + 1)%12 == 0 ? cal.get(Calendar.YEAR) + 1 : cal.get(Calendar.YEAR)
				, (cal.get(Calendar.MONTH) + 1)%12, 1);
	}
	
	@Override public void start(Stage primaryStage) throws Exception {
		//Sets the root
		AnchorPane root = new AnchorPane();
		
		//Add style classes and id
		header.getStyleClass().add("header");
		cal_title.getStyleClass().add("title");
		cur_month_year.getStyleClass().add("month_year");
		prev_month.getStyleClass().add("left");
		next_month.getStyleClass().add("right");
		footer.getStyleClass().add("footer");
		calendar.setId("calendar");
		
		//Add actions
		prev_month.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				setMonth(-1);
				fillCalendar();
				updateActivitiesView();
			}
		});
		next_month.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				setMonth(1);
				fillCalendar();
				updateActivitiesView();
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
		header.getChildren().addAll(cal_title, cur_month_year, prev_month, next_month);
			//footer
		profile.setPrefWidth(256);
		profile.setPrefHeight(16);
		tasks.setPrefWidth(256);
		tasks.setPrefHeight(16);
		footer.getChildren().addAll(profile, tasks);
			//calendar
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
		
		//Add all nodes and init the scene and add css
		root.getChildren().addAll(header, calendar, footer);
		Scene scene = new Scene(root, 1600, 1024);
		scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
		//Initializes the stage and shows it
		primaryStage.setTitle(viewName);
		primaryStage.setScene(scene);
		primaryStage.show();
		//Sets the model for this view and updates the view according to it
		model = new models.Calendar(0, "admin");
		setModel(model);
	}
	
	//Set up bindings and listeners:
	private ListChangeListener<Integer> activitiesChangeListener = new ListChangeListener<Integer>() {
	        @SuppressWarnings("rawtypes")
			public void onChanged(ListChangeListener.Change change) {
	        	updateActivitiesView();
	        }
		};
	
	public void setModel(models.Calendar model) {
		if (this.model != null) {
			model.getActivities().removeListener(activitiesChangeListener);
		}
		if (this.model != null) {
			model.getActivities().addListener(activitiesChangeListener);
		}
		this.model = model;
		fillCalendar();
		updateActivitiesView();
	}
	
	private void fillCalendar() {
		//Titler
		cal_title.setText("Gruppe 37s kalender");
		cur_month_year.setText(getMonth(cal.get(Calendar.MONTH)) + " " + Integer.toString(cal.get(Calendar.YEAR)));
		
		//Forrige måned
		int start_prev_month = prev_cal.getActualMaximum(Calendar.DAY_OF_MONTH) - start_index + 1;
		for (int i = 0; i < start_index; i++) {
			days.get(i).setText(Integer.toString(start_prev_month));
			days.get(i).setOpacity(0.25);
			start_prev_month++;
		}
		
		//Gjeldende måned
		int date = 1;
		for (int i = start_index; i <= end_index; i++) {
			if (date == 1) {
				days.get(i).setText(getMonth(cal.get(Calendar.MONTH)).substring(0, 3) + " " + Integer.toString(date));
			} else {
				days.get(i).setText(Integer.toString(date));
			}
			days.get(i).setOpacity(1);
			date++;
		}
		
		//Neste måned
		date = 1;
		for (int i = end_index + 1; i < days.size(); i++) {
			if (date == 1) {
				days.get(i).setText(getMonth(next_cal.get(Calendar.MONTH)).substring(0, 3) + " " + Integer.toString(date));
			} else {
				days.get(i).setText(Integer.toString(date));
			}
			days.get(i).setOpacity(0.25);
			date++;
		}
	}
	
	private void updateActivitiesView() {
		for (int i = 0; i < days.size(); i++){
			day_activities.get(i).getChildren().clear();
		}
		for (int act : model.getActivities()) {
			if (cal.get(Calendar.MONTH) == 2){
				Button activity = new Button("Ny aktivitet");
				activity.getStyleClass().add("activity");
				day_activities.get(act).getChildren().add(activity);
			}
		}
	}

	private int getFirstDayInMonth(int year, int month) {
		Calendar c = Calendar.getInstance();
		c.set(year, month, 1);
		return c.get(Calendar.DAY_OF_WEEK);
	}
	
	private String getMonth(int i){
		return months.get(i);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
