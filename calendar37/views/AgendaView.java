package views;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import utils.DatabaseInterface;
import utils.Utilities;
import models.Activity;
import models.Invite;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class AgendaView {

	String viewName = "Agenda";
	
	//TESTVALUES
	private AnchorPane parent;
	private String user_name;
	private int cal_id;
	//TESTVALUES
	
	public AgendaView(AnchorPane parent, String user_name, int cal_id) {
		this.parent = parent;
		this.user_name = user_name;
		this.cal_id = cal_id;
	}
	
	//Init the DBI and utils
	private DatabaseInterface dbi = new DatabaseInterface();
	private Utilities utils = new Utilities();
	//The model for this view
	private models.Calendar model;
	
	//Variables we need defined outside the "start"-function
		//View-elements
	GridPane root;
	private Label ag_title = new Label("<min agenda>");
	private ChoiceBox<String> timeframe = new ChoiceBox<String>();
	private GridPane header = new GridPane();
	private VBox agenda_body = new VBox();
	private ScrollPane agenda = new ScrollPane(agenda_body);
	private Button profile = new Button("Min profil");
	private Button close = new Button("Lukk");
	private HBox footer = new HBox();
	
		//Useful final variables
	private int timeframe_index = 2;
	
	
	public void start(Stage primaryStage) throws Exception {
		//Sets the root
		root = new GridPane();
		
		//Add style classes, id and set size to screen
		root.styleProperty().set("-fx-background-color: #eeeefa");
		header.getStyleClass().add("header");
		ag_title.getStyleClass().add("title");
		agenda_body.getStyleClass().add("agenda-list");
		footer.getStyleClass().add("footer");
		
		//Add actions
		profile.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				openProfile();
			}
		});
		close.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				close();
			}
		});
		
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
		footer.getChildren().addAll(profile, close);
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
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				parent.disableProperty().set(false);
			}
		});
		primaryStage.setOnHiding(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				parent.disableProperty().set(false);
			}
		});
		
		//Sets the model for this view and updates the view according to it
		setModel(dbi.getCalendar(cal_id));
		
		//Sets focus to the profile-button
		profile.requestFocus();
	}
	
	//Set up bindings and listeners:
	private ListChangeListener<Integer> activitiesChangeListener = new ListChangeListener<Integer>() {
		        @SuppressWarnings("rawtypes")
				public void onChanged(ListChangeListener.Change change) {
		        	fillAgenda();
		    	}
			};
	
	/**
	 * Updates the global model-attribute and activates the listeners if it's needed,
	 * and updates the view with correct attributes and {@link Activity}s.
	 * 
	 * @param model
	 */
	private void setModel(models.Calendar model) {
		if (this.model != null) {
			model.getActivities().removeListener(activitiesChangeListener);
		}
		this.model = model;
		fillAgenda();
		if (this.model != null) {
			model.getActivities().addListener(activitiesChangeListener);
		}
	}
	
	/**
	 * Fills in all the dates with all the {@link Activity}s associated with that date.
	 */
	private void fillAgenda() {
		//Refresh the model with new activities
		model = dbi.getCalendar(cal_id);
		//Titler
		ag_title.setText((model.getIs_group_cal() ? model.getCalendar_owner_group() : model.getCalendar_owner_user()) + "s agenda");
		
		//Fjern tidligere aktiviteter
		agenda_body.getChildren().clear();
		
		//Fylle inn aktiviteter
		Map<LocalDate, ArrayList<String>> acts_on_day = new TreeMap<LocalDate, ArrayList<String>>();
		for (Activity cur_act : dbi.getAllActivities(user_name)) {
			LocalDate start_key = cur_act.getStart_date();
			LocalDate end_key = cur_act.getEnd_date();
			if (timeframe_index == 0 && start_key.isAfter(LocalDate.now().plus(Period.ofDays(1)))) {
				continue;
			} else if (timeframe_index == 1 && start_key.isAfter(LocalDate.now().plus(Period.ofWeeks(1)))) {
				continue;
			} else if (timeframe_index == 2 && start_key.isAfter(LocalDate.now().plus(Period.ofMonths(1)))) {
				continue;
			} else if (timeframe_index == 3 && start_key.isAfter(LocalDate.now().plus(Period.ofYears(1)))) {
				continue;
			}
			ArrayList<Invite> invites_on_act = dbi.getAllInvites(cur_act.getActivity_id());
			String status = "";
			for (Invite cur_inv : invites_on_act) {
				if (cur_inv.getInvited() == user_name) {
					status = ", Status: " + cur_inv.getStatus();
				}
			}
			if (end_key == null || end_key.equals(start_key)) {
				if (acts_on_day.containsKey(start_key)) {
					acts_on_day.get(start_key).add(utils.getFormattedActivity(cur_act, true) + status);
				} else {
					acts_on_day.put(start_key, new ArrayList<String>());
					acts_on_day.get(start_key).add(utils.getFormattedActivity(cur_act, true) + status);
				}
			} else {
				if (acts_on_day.containsKey(start_key)) {
					acts_on_day.get(start_key).add("> " + utils.getFormattedActivity(cur_act, true) + status);
				} else {
					acts_on_day.put(start_key, new ArrayList<String>());
					acts_on_day.get(start_key).add("> " + utils.getFormattedActivity(cur_act, true) + status);
				}
				if (acts_on_day.containsKey(end_key)) {
					acts_on_day.get(end_key).add("< " + utils.getFormattedActivity(cur_act, false));
				} else {
					acts_on_day.put(end_key, new ArrayList<String>());
					acts_on_day.get(end_key).add("< " + utils.getFormattedActivity(cur_act, false));
				}
			}
		}
			//for hver dato
		for (Entry<LocalDate, ArrayList<String>> entry : acts_on_day.entrySet()) {
			HBox date = new HBox();
			Label date_txt = new Label(
					(entry.getKey().getYear() != Calendar.getInstance().get(Calendar.YEAR) ? entry.getKey().getYear() + ": " : "")
					+ utils.weekdays.get(entry.getKey().getDayOfWeek().getValue() - 1).substring(0, 3)
					+ " " + entry.getKey().getDayOfMonth()
					+ ". " + utils.months.get(entry.getKey().getMonthValue() - 1).substring(0, 3));
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
	
	/**
	 * Opens up the view for an {@link Account} that is pressed in this {@link AgendaView},
	 * and retains information about in what {@link models.Calendar} the {@link Account} was pressed.
	 * 
	 * TODO: Fill in when we get an AccountView up and running.
	 */
	private void openProfile() {
	}
	
	/**
	 * Closes this view, and re-enables the {@link CalendarView}, commits nothing to the model when doing so.
	 */
	private void close() {
		Stage stage  = (Stage) root.getScene().getWindow();
		stage.close();
	}
}
