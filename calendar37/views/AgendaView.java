package views;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import models.Activity;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class AgendaView extends Application {

	String viewName = "Agenda";
	
	
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
	
	//Variables we need defined outside the "start"-function
		//View-elements
	private Label ag_title = new Label("<min agenda>");
	private ChoiceBox<String> timeframe = new ChoiceBox<String>();
	private GridPane header = new GridPane();
	private VBox agenda_body = new VBox();
	private ScrollPane agenda = new ScrollPane(agenda_body);
	private Button close = new Button("Lukk");
	private Button profile = new Button("Min profil");
	private HBox footer = new HBox();
	
		//Useful final variables
	private final List<String> months = Arrays.asList("Januar", "Februar", "Mars", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Desember");
	private final List<String> weekdays = Arrays.asList("Mandag", "Tirsdag", "Onsdag", "Torsdag", "Fredag", "Lørdag", "Søndag");
	private int timeframe_index = 2;
	
	
	@Override public void start(Stage primaryStage) throws Exception{
		//TESPRINTING
		
		//TESPRINTING
		
		//Sets the root
		GridPane root = new GridPane();
		
		//Add style classes, id and set size to screen
		root.styleProperty().set("-fx-background-color: #eeeefa");
		header.getStyleClass().add("header");
		ag_title.getStyleClass().add("title");
		agenda_body.getStyleClass().add("agenda-list");
		footer.getStyleClass().add("footer");
		
		//Add actions
		
		
		//The view
			//General restraints
		GridPane.setHgrow(header, Priority.ALWAYS);
		GridPane.setHgrow(ag_title, Priority.ALWAYS);
		GridPane.setHgrow(agenda, Priority.ALWAYS);
		GridPane.setVgrow(agenda, Priority.ALWAYS);
		GridPane.setHgrow(footer, Priority.ALWAYS);
		agenda.setHbarPolicy(ScrollBarPolicy.NEVER);
		agenda.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		agenda.setFitToWidth(true);
		agenda.setFitToHeight(true);
			//header
		header.add(ag_title, 0, 0);
		header.add(timeframe, 1, 0);
			//agenda
		timeframe.setItems(FXCollections.observableArrayList("1 Dag","1 Uke","1 Måned", "1 År", "All tid"));
		timeframe.getSelectionModel().select(timeframe_index);
			//footer
		footer.getChildren().addAll(close, profile);
			//managing timeframes
		timeframe.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
						int index = timeframe.getSelectionModel().getSelectedIndex();
						timeframe_index = index;
						fillAgenda();
			}
		});
		
		//Add all nodes, init the scene and add css
		root.add(header, 0, 0);
		root.add(agenda, 0, 1);
		root.add(footer, 0, 2);
		Scene scene = new Scene(root, 512, 768);
		scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
		//Initializes the stage and shows it
		primaryStage.setTitle(viewName);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		//TESTVALUES
			//activity
		act1 = new Activity(0, 0, "admin");
		act1.setTitle("Møte undass");
		act1.setStart_date(LocalDate.of(2015, 3, 13));
		act1.setFrom(LocalTime.of(12, 0));
		act1.setTo(LocalTime.of(12, 30));
			//activity
		act2 = new Activity(1, 0, "admin");
		act2.setTitle("Gruppearbeid");
		act2.setStart_date(LocalDate.of(2015, 3, 15));
		act2.setEnd_date(LocalDate.of(2015, 3, 18));
		act2.setFrom(LocalTime.of(16, 0));
		act2.setTo(LocalTime.of(17, 30));
			//activity
		act3 = new Activity(2, 0, "admin");
		act3.setTitle("Middag med den altfor, altfor, altfor, altfor, altfor store familien min");
		act3.setStart_date(LocalDate.of(2015, 4, 24));
			//activity
		act4 = new Activity(3, 0, "admin");
		act4.setTitle("Travel dag!");
		act4.setStart_date(LocalDate.of(2015, 4, 24));
			//activity
		act5 = new Activity(4, 0, "admin");
		act5.setTitle("Travel dag!");
		act5.setStart_date(LocalDate.of(2015, 4, 24));
			//activity
		act6 = new Activity(5, 0, "admin");
		act6.setTitle("Travel dag!");
		act6.setStart_date(LocalDate.of(2015, 4, 24));
			//activity
		act7 = new Activity(6, 0, "admin");
		act7.setTitle("Travel dag!");
		act7.setStart_date(LocalDate.of(2015, 4, 24));
			//activity
		act8 = new Activity(7, 0, "admin");
		act8.setTitle("Planlegger i forveien, si!");
		act8.setStart_date(LocalDate.of(2016, 7, 22));
			//activity
		act9 = new Activity(8, 0, "admin");
		act9.setTitle("Party party");
		act9.setStart_date(LocalDate.of(2015, 7, 2));
		act9.setFrom(LocalTime.of(20, 0));
			//activity
		act10 = new Activity(9, 0, "admin");
		act10.setTitle("Travel dag!");
		act10.setStart_date(LocalDate.of(2015, 4, 24));
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
	private ListChangeListener<Integer> activitiesChangeListener = new
			ListChangeListener<Integer>() {
	        @SuppressWarnings("rawtypes")
			public void onChanged(
				ListChangeListener.Change change) {
		        	fillAgenda();
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
		fillAgenda();
	}
	
	private void fillAgenda() {
		//Titler
		ag_title.setText((model.getIs_group_cal() ? model.getCalendar_owner_group() : model.getCalendar_owner_user()) + "s agenda");
		
		//Fjern tidligere aktiviteter
		agenda_body.getChildren().clear();
		
		//Fylle inn aktiviteter
		ArrayList<Integer> activites = new ArrayList<Integer>(model.getActivities());
		Map<LocalDate, ArrayList<String>> acts_on_day = new TreeMap<LocalDate, ArrayList<String>>();
		for (int act : activites) {
			LocalDate start_key = getActivity(act).getStart_date();
			LocalDate end_key = getActivity(act).getEnd_date();
			if (timeframe_index == 0 && start_key.isAfter(LocalDate.now().plus(Period.ofDays(1)))) {
				continue;
			} else if (timeframe_index == 1 && start_key.isAfter(LocalDate.now().plus(Period.ofWeeks(1)))) {
				continue;
			} else if (timeframe_index == 2 && start_key.isAfter(LocalDate.now().plus(Period.ofMonths(1)))) {
				continue;
			} else if (timeframe_index == 3 && start_key.isAfter(LocalDate.now().plus(Period.ofYears(1)))) {
				continue;
			}
			if (end_key == null) {
				if (acts_on_day.containsKey(start_key)) {
					acts_on_day.get(start_key).add(getFormattedActivity(getActivity(act), true));
				} else {
					acts_on_day.put(start_key, new ArrayList<String>());
					acts_on_day.get(start_key).add(getFormattedActivity(getActivity(act), true));
				}
			} else {
				if (acts_on_day.containsKey(start_key)) {
					acts_on_day.get(start_key).add("> " + getFormattedActivity(getActivity(act), true));
				} else {
					acts_on_day.put(start_key, new ArrayList<String>());
					acts_on_day.get(start_key).add("> " + getFormattedActivity(getActivity(act), true));
				}
				if (acts_on_day.containsKey(end_key)) {
					acts_on_day.get(end_key).add("< " + getFormattedActivity(getActivity(act), false));
				} else {
					acts_on_day.put(end_key, new ArrayList<String>());
					acts_on_day.get(end_key).add("< " + getFormattedActivity(getActivity(act), false));
				}
			}
		}
			//for hver dato
		for (Entry<LocalDate, ArrayList<String>> entry : acts_on_day.entrySet()) {
			HBox date = new HBox();
			Label date_txt = new Label(
					(entry.getKey().getYear() != Calendar.getInstance().get(Calendar.YEAR) ? entry.getKey().getYear() + ": " : "")
					+ weekdays.get(entry.getKey().getDayOfWeek().getValue() - 1).substring(0, 3)
					+ " " + entry.getKey().getDayOfMonth()
					+ ". " + months.get(entry.getKey().getMonthValue() - 1).substring(0, 3));
			VBox date_acts = new VBox();
			date.getChildren().addAll(date_txt, date_acts);
				//for hver act. på enhver dato
			for (String act : entry.getValue()) {
				Button date_act = new Button(act);
				date_acts.getChildren().add(date_act);
			}
			date.getStyleClass().add("date");
			date_acts.getStyleClass().add("date-acts");
			agenda_body.getChildren().add(date);
		}
	}
	
	private String getFormattedActivity(Activity act, boolean from) {
		String ret_str = "";
		if (from) {
			if (act.getFrom() != null) {
				ret_str += act.getFrom() + ": ";
			}
		} else {
			if (act.getTo() != null) {
				ret_str += act.getTo() + ": ";
			}
		}
		if (act.getTitle() != null) {
			ret_str += act.getTitle();
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
	
	public static void main(String[] args) {
		launch(args);
	}
}
