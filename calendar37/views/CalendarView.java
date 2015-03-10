package views;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import models.Activity;
import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.*;

public class CalendarView extends Application {

	private String viewName = "Calendar";
	
	//The model for this view
	private models.Calendar model;
	
	//TESTVALUES
	//The activities for this view
	Activity act1;
	Activity act2;
	Activity act3;
	Activity act4;
	Activity act5;
	Activity act6;
	Activity act7;
	Activity act8;
	Activity act9;
	Activity act10;
	//TESTVALUES
	
	//The screen-size currently used
	Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
	
	//Variables we need defined outside the "start"-function
		//View-elements
	private GridPane calendar = new GridPane();
	private GridPane header = new GridPane();
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
		//TESPRINTING
		
		//TESPRINTING
		
		//Sets the rootd
		AnchorPane root = new AnchorPane();
		
		//Add style classes, id and set size to screen
		root.styleProperty().set("-fx-background-color: #eeeefa");
		header.getStyleClass().add("header");
		header.setPrefWidth(primaryScreenBounds.getWidth());
		cal_title.getStyleClass().add("title");
		cur_month_year.getStyleClass().add("month_year");
		prev_month.getStyleClass().add("left");
		next_month.getStyleClass().add("right");
		calendar.setId("calendar");
		calendar.setMaxSize(primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight() - 123);
		calendar.setMinSize(primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight() - 123);
		footer.getStyleClass().add("footer");
		footer.setPrefWidth(primaryScreenBounds.getWidth());
		
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
		GridPane.setHgrow(cal_title, Priority.ALWAYS);
			//header
		header.add(cal_title, 0, 0);
		header.add(cur_month_year, 1, 0);
		header.add(prev_month, 2, 0);
		header.add(next_month, 3, 0);
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
				ScrollPane day_wrapper = new ScrollPane(day_body);
				day_wrapper.setHbarPolicy(ScrollBarPolicy.NEVER);
				day_wrapper.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
				days.add(day_title);
				day_activities.add(days.indexOf(day_title), day_body);
				day.getChildren().addAll(day_title, day_wrapper);
				calendar.add(day, j, i);
				day.getStyleClass().add("day");
				day_title.getStyleClass().add("date");
				day_wrapper.getStyleClass().add("white");
				day_body.getStyleClass().add("white");
				GridPane.setVgrow(day, Priority.ALWAYS);
				day_wrapper.setFitToWidth(true);
				day_wrapper.setFitToHeight(true);
			}
		}
		
		//Add all nodes and init the scene and add css
		root.getChildren().addAll(header, calendar, footer);
		Scene scene = new Scene(root, primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight());
		scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
		//Initializes the stage and shows it
		primaryStage.setTitle(viewName);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		//TESTVALUES
			//activity
		act1 = new Activity(0, "admin");
		act1.setDescription("Møte undass");
		act1.setDate(LocalDate.of(2015, 3, 13));
		act1.setFrom(LocalTime.of(12, 0));
		act1.setTo(LocalTime.of(12, 30));
			//activity
		act2 = new Activity(1, "admin");
		act2.setDescription("Middag med den store familien min");
		act2.setDate(LocalDate.of(2015, 3, 15));
		act2.setFrom(LocalTime.of(16, 0));
		act2.setTo(LocalTime.of(17, 30));
			//activity
		act3 = new Activity(2, "admin");
		act3.setDescription("Gruppearbeid");
		act3.setDate(LocalDate.of(2015, 4, 24));
			//activity
		act4 = new Activity(3, "admin");
		act4.setDescription("Travel dag!");
		act4.setDate(LocalDate.of(2015, 4, 24));
		//activity
		act5 = new Activity(4, "admin");
		act5.setDescription("Travel dag!");
		act5.setDate(LocalDate.of(2015, 4, 24));
		//activity
		act6 = new Activity(5, "admin");
		act6.setDescription("Travel dag!");
		act6.setDate(LocalDate.of(2015, 4, 24));
		//activity
		act7 = new Activity(6, "admin");
		act7.setDescription("Travel dag!");
		act7.setDate(LocalDate.of(2015, 4, 24));
		//activity
		act8 = new Activity(7, "admin");
		act8.setDescription("Travel dag!");
		act8.setDate(LocalDate.of(2015, 4, 24));
		//activity
		act9 = new Activity(8, "admin");
		act9.setDescription("Travel dag!");
		act9.setDate(LocalDate.of(2015, 4, 24));
		//activity
		act10 = new Activity(9, "admin");
		act10.setDescription("Travel dag!");
		act10.setDate(LocalDate.of(2015, 4, 24));
			//the calendar-model
		model = new models.Calendar(0, "admin");
			//add activities to the calendar
		model.getActivities().addAll(act1.getActivity_id(), act2.getActivity_id(), act3.getActivity_id(), act4.getActivity_id()
				, act5.getActivity_id(), act6.getActivity_id(), act7.getActivity_id(), act8.getActivity_id(), act9.getActivity_id()
				, act10.getActivity_id());
		//TESTVALUES
		
		//Sets the model for this view and updates the view according to it
		setModel(model);
		
		//Sets focus to the profile-button
		profile.requestFocus();
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
			try {
				Activity cur_act = getActivity(act);
				if (cur_act.getDate().getYear() == cal.get(Calendar.YEAR) && cur_act.getDate().getMonthValue() == cal.get(Calendar.MONTH) + 1) {
					String formatted_act = getFormattedActivity(cur_act);
					Button activity_btn = new Button(formatted_act);
					activity_btn.getStyleClass().add("activity");
					activity_btn.setFocusTraversable(false);
					day_activities.get(start_index + cur_act.getDate().getDayOfMonth() - 1).getChildren().add(activity_btn);
				}
			} catch (NullPointerException e) {
				System.err.println("NullPointerException: " + e.getMessage());
			}
		}
	}
	
	private String getFormattedActivity(Activity act) {
		String ret_str = "";
		if (act.getFrom() != null) {
			ret_str += act.getFrom() + ": ";
		}
		if (act.getDescription() != null) {
			ret_str += act.getDescription();
		}
		return ret_str;
	}
	
	//FIKS NÅR VI FÅR INN DATABASEN!
	private Activity getActivity(int i) {
		if (i == 0) {
			return act1;
		} else if (i == 1) {
			return act2;
		} else if (i == 2) {
			return act3;
		} else if (i == 3) {
			return act4;
		} else if (i == 4) {
			return act5;
		} else if (i == 5) {
			return act6;
		} else if (i == 6) {
			return act7;
		} else if (i == 7) {
			return act8;
		} else if (i == 8) {
			return act9;
		} else if (i == 9) {
			return act10;
		} else {
			return null;
		}
	}
	//FIKS NÅR VI FÅR INN DATABASEN!
	
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
