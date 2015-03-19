package views;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import controllers.ActivityController;
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
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
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
	
	private AnchorPane parent;
	private String user_name;
	private int cal_id;
	
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
		ArrayList<Activity> all_acts = dbi.getAllActivities(user_name);
		Collections.sort(all_acts);
		for (Activity cur_act : all_acts) {
			int cur_act_id = cur_act.getActivity_id();
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
			if (end_key == null || end_key.equals(start_key)) {
				if (acts_on_day.containsKey(start_key)) {
					acts_on_day.get(start_key).add(cur_act_id + utils.getFormattedActivity(cur_act, true, 42));
				} else {
					acts_on_day.put(start_key, new ArrayList<String>());
					acts_on_day.get(start_key).add(cur_act_id + utils.getFormattedActivity(cur_act, true, 42));
				}
			} else {
				if (acts_on_day.containsKey(start_key)) {
					acts_on_day.get(start_key).add(cur_act_id + "> " + utils.getFormattedActivity(cur_act, true, 40));
				} else {
					acts_on_day.put(start_key, new ArrayList<String>());
					acts_on_day.get(start_key).add(cur_act_id + "> " + utils.getFormattedActivity(cur_act, true, 40));
				}
				if (acts_on_day.containsKey(end_key)) {
					acts_on_day.get(end_key).add(cur_act_id + "< " + utils.getFormattedActivity(cur_act, false, 40));
				} else {
					acts_on_day.put(end_key, new ArrayList<String>());
					acts_on_day.get(end_key).add(cur_act_id + "< " + utils.getFormattedActivity(cur_act, false, 40));
				}
			}
		}
		for (Invite cur_inv : dbi.getUserInvitedTo(user_name)) {
			Activity cur_act = dbi.getActivity(cur_inv.getInvited_to());
			int cur_act_id = cur_act.getActivity_id();
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
			String status = ", S: " + (cur_inv.getStatus().equals("true") ? "ja" : "nei");
			if (end_key == null || end_key.equals(start_key)) {
				if (acts_on_day.containsKey(start_key)) {
					acts_on_day.get(start_key).add(cur_act_id + utils.getFormattedActivity(cur_act, true, 42) + status);
				} else {
					acts_on_day.put(start_key, new ArrayList<String>());
					acts_on_day.get(start_key).add(cur_act_id + utils.getFormattedActivity(cur_act, true, 42) + status);
				}
			} else {
				if (acts_on_day.containsKey(start_key)) {
					acts_on_day.get(start_key).add(cur_act_id + "> " + utils.getFormattedActivity(cur_act, true, 40) + status);
				} else {
					acts_on_day.put(start_key, new ArrayList<String>());
					acts_on_day.get(start_key).add(cur_act_id + "> " + utils.getFormattedActivity(cur_act, true, 40) + status);
				}
				if (acts_on_day.containsKey(end_key)) {
					acts_on_day.get(end_key).add(cur_act_id + "< " + utils.getFormattedActivity(cur_act, false, 40));
				} else {
					acts_on_day.put(end_key, new ArrayList<String>());
					acts_on_day.get(end_key).add(cur_act_id + "< " + utils.getFormattedActivity(cur_act, false, 40));
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
				Button date_act = new Button(act.substring(5));
				date_acts.getChildren().add(date_act);
				date_act.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						openActivity(Integer.valueOf(act.subSequence(0, 5).toString()));
					}
				});
			}
			date.getStyleClass().add("date");
			date_acts.getStyleClass().add("date-acts");
			agenda_body.getChildren().add(date);
		}
	}
	
	/**
	 * Opens up the view for an {@link Activity} that is pressed in this {@link CalendarView},
	 * and retains information about in what {@link models.Calendar} the {@link Activity} was pressed.
	 * 
	 * @param activity_id
	 * @throws IOException 
	 */
	private void openActivity(int activity_id) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ActivityView.fxml"));
			Parent root = (Parent) loader.load();
			ActivityController activity_controller = (ActivityController) loader.getController();
			
			//Setter rett aktivitet
			activity_controller.setActivity_id(activity_id);
			
			//Lager scenen og stagen
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			
			//Disables this view
			AgendaView.this.root.disableProperty().set(true);
			
			//Initializes the stage and shows it
			stage.setTitle("Activity " + activity_id);
			stage.setScene(scene);
			stage.show();
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					AgendaView.this.root.disableProperty().set(false);
				}
			});
			stage.setOnHiding(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					AgendaView.this.root.disableProperty().set(false);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
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
