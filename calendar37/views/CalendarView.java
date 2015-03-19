package views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;

import controllers.AccountController;
import controllers.ActivityController;
import controllers.CreateActivityController;
import controllers.CreateGroupController;
import utils.DatabaseInterface;
import utils.Utilities;
import models.Activity;
import models.Invite;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.*;

public class CalendarView {

	private String viewName = "Calendar";
	
	private String user_name;
	private int cal_id;
	
	public CalendarView(String user_name, int cal_id) {
		this.user_name = user_name;
		this.cal_id = cal_id;
	}
	
	//Init the DBI and utils
	private DatabaseInterface dbi = new DatabaseInterface();
	private Utilities utils = new Utilities();
	//The model for this view
	private models.Calendar model;
	
	//The screen-size currently used
	Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
	double primWidth = primaryScreenBounds.getWidth() - 32;
	double primHeight = primaryScreenBounds.getHeight() - 155;
	
	//Variables we need defined outside the "start"-function
		//View-elements
	private AnchorPane root;
	private GridPane calendar = new GridPane();
	private GridPane header = new GridPane();
	private Label cal_title = new Label("<kalendernavn>");
	private Label cur_month_year = new Label("<m�ned �r>");
	private Button prev_month = new Button("Forrige");
	private Button next_month = new Button("Neste");
	private HBox footer = new HBox();
	private Button admin_panel = new Button("[ADMIN]");
	private Button create_group = new Button("Opprett gruppe");
	private Button create_activity = new Button("Opprett aktivitet");
	private Button profile = new Button("Min profil");
	private Button tasks = new Button("Aktivitets-agenda");
	private Button close = new Button("Lukk");
	
		//Lists containing current days and activities
	private ArrayList<Label> days = new ArrayList<Label>();
	private ArrayList<VBox> day_activities = new ArrayList<VBox>();
	
	//Set the values for THIS view, updateable:
	Calendar cal = Calendar.getInstance();
	int start_index = utils.getFirstDayInMonth(cal.get(Calendar.YEAR)
			, cal.get(Calendar.MONTH)) != 1 ? utils.getFirstDayInMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)) - 2 : 6;
	int end_index = start_index + cal.getActualMaximum(Calendar.DAY_OF_MONTH) - 1;
	
	Calendar prev_cal = new GregorianCalendar((cal.get(Calendar.MONTH) - 1)%12 == 0 ? cal.get(Calendar.YEAR) - 1 : cal.get(Calendar.YEAR)
			, (cal.get(Calendar.MONTH) - 1)%12, 1);
	
	Calendar next_cal = new GregorianCalendar((cal.get(Calendar.MONTH) + 1)%12 == 0 ? cal.get(Calendar.YEAR) + 1 : cal.get(Calendar.YEAR)
			, (cal.get(Calendar.MONTH) + 1)%12, 1);
	
	public void start(Stage primaryStage) throws Exception {
		//Sets the root
		root = new AnchorPane();
		
		//Add style classes, id and set size to screen
		root.styleProperty().set("-fx-background-color: #eeeefa");
		header.getStyleClass().add("header");
		header.setPrefWidth(primWidth);
		cal_title.getStyleClass().add("title");
		cur_month_year.getStyleClass().add("month_year");
		prev_month.getStyleClass().add("left");
		next_month.getStyleClass().add("right");
		calendar.setId("calendar");
		calendar.setMaxSize(primWidth, primHeight);
		calendar.setMinSize(primWidth, primHeight);
		footer.getStyleClass().add("footer");
		footer.setPrefWidth(primWidth);
		admin_panel.setStyle("-fx-font-weight: bold;");
		
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
		admin_panel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				openAdminPanel();
			}
		});
		create_group.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				openCreateGroup();
			}
		});
		create_activity.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				openCreateActivity();
			}
		});
		profile.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				openProfile();
			}
		});
		tasks.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				openAgenda();
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
		if (user_name.equals("lahey")) {
			footer.getChildren().add(admin_panel);
		}
		footer.getChildren().addAll(create_group, create_activity, profile, tasks, close);
			//calendar
			//the days
		for (int i = 0; i < utils.weekdays.size(); i++) {
			Label weekday = new Label(utils.weekdays.get(i));
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
		Scene scene = new Scene(root, primWidth, primHeight + 123);
		scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
		//Initializes the stage and shows it
		primaryStage.setTitle(viewName);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		//Sets the model for this view and updates the view according to it
		setModel(dbi.getCalendar(cal_id));
		
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
	
	/**
	 * Updates the global model-attribute and activates the listeners if it's needed,
	 * and updates the view with correct attributes and {@link Activity}s.
	 * 
	 * @param model
	 */
	public void setModel(models.Calendar model) {
		if (this.model != null) {
			model.getActivities().removeListener(activitiesChangeListener);
		}
		this.model = model;
		fillCalendar();
		updateActivitiesView();
		if (this.model != null) {
			model.getActivities().addListener(activitiesChangeListener);
		}
	}
	
	/**
	 * Fills in all the dates, month names, and sets the color/opacity of the dates according to what the current month is.
	 */
	private void fillCalendar() {
		//Titler
		cal_title.setText((model.getIs_group_cal() ? dbi.getGroup(model.getCalendar_owner_group()).getGroup_name() : model.getCalendar_owner_user()) + "s kalender");
		cur_month_year.setText(utils.months.get(cal.get(Calendar.MONTH)) + " " + Integer.toString(cal.get(Calendar.YEAR)));
		
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
				days.get(i).setText(utils.months.get(cal.get(Calendar.MONTH)).substring(0, 3) + " " + Integer.toString(date));
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
				days.get(i).setText(utils.months.get(next_cal.get(Calendar.MONTH)).substring(0, 3) + " " + Integer.toString(date));
			} else {
				days.get(i).setText(Integer.toString(date));
			}
			days.get(i).setOpacity(0.25);
			date++;
		}
	}
	
	/**
	 * Updates all the {@link Activity}s that this {@link CalendarView} is displaying according to what month the view is on.
	 */
	private void updateActivitiesView() {
		//Refresh the model with new activities
		model = dbi.getCalendar(cal_id);
		//Clear the activities
		for (int i = 0; i < days.size(); i++){
			day_activities.get(i).getChildren().clear();
		}
		//Personal activities
		ArrayList<Activity> all_acts = dbi.getAllActivities(user_name);
		Collections.sort(all_acts);
		for (Activity cur_act : all_acts) {
			try {
				if (cur_act.getStart_date().getYear() == cal.get(Calendar.YEAR) && cur_act.getStart_date().getMonthValue() == cal.get(Calendar.MONTH) + 1) {
					String formatted_act = (cur_act.getEnd_date() == null || cur_act.getEnd_date().equals(cur_act.getStart_date()) ?
							"" : "> ") + utils.getFormattedActivity(cur_act, true, 30);
					Button activity_btn = new Button(formatted_act);
					activity_btn.getStyleClass().add("personal-activity");
					activity_btn.setFocusTraversable(false);
					activity_btn.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							openActivity(cur_act.getActivity_id());
						}
					});
					day_activities.get(start_index + cur_act.getStart_date().getDayOfMonth() - 1).getChildren().add(activity_btn);
				}
				if (cur_act.getEnd_date() != null && !cur_act.getEnd_date().equals(cur_act.getStart_date())) {
					if (cur_act.getEnd_date().getYear() == cal.get(Calendar.YEAR) && cur_act.getEnd_date().getMonthValue() == cal.get(Calendar.MONTH) + 1) {
						String formatted_act = "< " + utils.getFormattedActivity(cur_act, false, 30);
						Button activity_btn = new Button(formatted_act);
						activity_btn.getStyleClass().add("personal-activity");
						activity_btn.setFocusTraversable(false);
						activity_btn.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								openActivity(cur_act.getActivity_id());
							}
						});
						day_activities.get(start_index + cur_act.getEnd_date().getDayOfMonth() - 1).getChildren().add(activity_btn);
					}
				}
			} catch (NullPointerException e) {
				System.err.println("[CUSTOM] NullPointerException in CalendarView: " + e.getMessage());
			}
		}
		//Invited
		for (Invite cur_inv: dbi.getUserInvitedTo(user_name)) {
			try {
				Activity cur_act = dbi.getActivity(cur_inv.getInvited_to());
				if (cur_act.getStart_date().getYear() == cal.get(Calendar.YEAR) && cur_act.getStart_date().getMonthValue() == cal.get(Calendar.MONTH) + 1) {
					String formatted_act = (cur_act.getEnd_date() == null || cur_act.getEnd_date().equals(cur_act.getStart_date()) ?
							"" : "> ") + utils.getFormattedActivity(cur_act, true, 30);
					Button activity_btn = new Button(formatted_act + ", S: " + (cur_inv.getStatus().equals("true") ? "ja" : "nei"));
					activity_btn.getStyleClass().add("group-activity");
					activity_btn.setFocusTraversable(false);
					activity_btn.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							openActivity(cur_act.getActivity_id());
						}
					});
					day_activities.get(start_index + cur_act.getStart_date().getDayOfMonth() - 1).getChildren().add(activity_btn);
				}
				if (cur_act.getEnd_date() != null && !cur_act.getEnd_date().equals(cur_act.getStart_date())) {
					if (cur_act.getEnd_date().getYear() == cal.get(Calendar.YEAR) && cur_act.getEnd_date().getMonthValue() == cal.get(Calendar.MONTH) + 1) {
						String formatted_act = "< " + utils.getFormattedActivity(cur_act, false, 30);
						Button activity_btn = new Button(formatted_act + ", S: " + (cur_inv.getStatus().equals("true") ? "ja" : "nei"));
						activity_btn.getStyleClass().add("group-activity");
						activity_btn.setFocusTraversable(false);
						activity_btn.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								openActivity(cur_act.getActivity_id());
							}
						});
						day_activities.get(start_index + cur_act.getEnd_date().getDayOfMonth() - 1).getChildren().add(activity_btn);
					}
				}
			} catch (NullPointerException e) {
				System.err.println("[CUSTOM] NullPointerException in CalendarView: " + e.getMessage());
			}
		}
	}
	
	/**
	 * Updates all the global variables in the {@link CalendarView} to a new month, either one previous,
	 * or the next month, specified by the diff-attribute.
	 * 
	 * @param diff
	 */
	private void setMonth(int diff) {
		cal.set((cal.get(Calendar.MONTH) + diff)%12 == 0 && cal.get(Calendar.MONTH) != 1 ? cal.get(Calendar.YEAR) + diff : cal.get(Calendar.YEAR)
				, (cal.get(Calendar.MONTH) + diff)%12, 1);
		start_index = utils.getFirstDayInMonth(cal.get(Calendar.YEAR)
				, cal.get(Calendar.MONTH)) != 1 ? utils.getFirstDayInMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)) - 2 : 6;
				end_index = start_index + cal.getActualMaximum(Calendar.DAY_OF_MONTH) - 1;
				
				prev_cal.set((cal.get(Calendar.MONTH) - 1)%12 == 0 ? cal.get(Calendar.YEAR) - 1 : cal.get(Calendar.YEAR)
						, (cal.get(Calendar.MONTH) - 1)%12, 1);
				
				next_cal.set((cal.get(Calendar.MONTH) + 1)%12 == 0 ? cal.get(Calendar.YEAR) + 1 : cal.get(Calendar.YEAR)
						, (cal.get(Calendar.MONTH) + 1)%12, 1);
	}
	
	/**
	 * Opens up the view for an {@link Activity} that is pressed in this {@link CalendarView},
	 * and retains information about in what {@link models.Calendar} the {@link Activity} was pressed.
	 * 
	 * @param activity_id
	 */
	private void openActivity(int activity_id) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ActivityView.fxml"));
			Parent root = (Parent) loader.load();
			ActivityController controller = (ActivityController) loader.getController();
			
			//Setter rett aktivitet
			controller.init(user_name, activity_id);
			
			//Lager scenen og stagen
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			
			//Disables this view
			CalendarView.this.root.disableProperty().set(true);
			
			//Initializes the stage and shows it
			stage.setTitle("Activity " + activity_id);
			stage.setScene(scene);
			stage.show();
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					CalendarView.this.root.disableProperty().set(false);
				}
			});
			stage.setOnHiding(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					CalendarView.this.root.disableProperty().set(false);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Opens up the administrator panel for this {@link CalendarView}.
	 */
	private void openAdminPanel() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AdminPanelView.fxml"));
			Parent root = (Parent) loader.load();
			
			//Lager scenen og stagen
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			
			//Disables this view
			CalendarView.this.root.disableProperty().set(true);
			
			//Initializes the stage and shows it
			stage.setTitle("Admin panel");
			stage.setScene(scene);
			stage.show();
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					CalendarView.this.root.disableProperty().set(false);
				}
			});
			stage.setOnHiding(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					CalendarView.this.root.disableProperty().set(false);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Opens up the view for an {@link Activity} that is pressed in this {@link CalendarView},
	 * and retains information about in what {@link models.Calendar} the {@link Activity} was pressed.
	 * 
	 * TODO: Venter på CreateGroupController
	 */
	private void openCreateGroup() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CreateGroupView.fxml"));
			Parent root = (Parent) loader.load();
			CreateGroupController controller = (CreateGroupController) loader.getController();
			
			//Setter rett aktivitet
//			controller.setActivity_id(activity_id);
			
			//Lager scenen og stagen
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			
			//Disables this view
			CalendarView.this.root.disableProperty().set(true);
			
			//Initializes the stage and shows it
			stage.setTitle("Opprett gruppe");
			stage.setScene(scene);
			stage.show();
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					CalendarView.this.root.disableProperty().set(false);
				}
			});
			stage.setOnHiding(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					CalendarView.this.root.disableProperty().set(false);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Opens up the view for an {@link Activity} that is pressed in this {@link CalendarView},
	 * and retains information about in what {@link models.Calendar} the {@link Activity} was pressed.
	 */
	private void openCreateActivity() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CreateActivityView.fxml"));
			Parent root = (Parent) loader.load();
			CreateActivityController controller = (CreateActivityController) loader.getController();
			
			//Lager scenen og stagen
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			
			//Setter rett aktivitet
			controller.setUserInfo(stage, user_name);
			
			//Disables this view
			CalendarView.this.root.disableProperty().set(true);
			
			//Initializes the stage and shows it
			stage.setTitle("Opprett aktivitet");
			stage.setScene(scene);
			stage.show();
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					CalendarView.this.root.disableProperty().set(false);
				}
			});
			stage.setOnHiding(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					CalendarView.this.root.disableProperty().set(false);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Opens up the view for an {@link Account} that is pressed in this {@link CalendarView},
	 * and retains information about in what {@link models.Calendar} the {@link Account} was pressed.
	 */
	private void openProfile() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AccountView.fxml"));
			Parent root = (Parent) loader.load();
			AccountController controller = (AccountController) loader.getController();
			
			//Setter rett aktivitet
			controller.setAccountView(user_name);
			
			//Lager scenen og stagen
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			
			//Disables this view
			CalendarView.this.root.disableProperty().set(true);
			
			//Initializes the stage and shows it
			stage.setTitle(user_name);
			stage.setScene(scene);
			stage.show();
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					CalendarView.this.root.disableProperty().set(false);
				}
			});
			stage.setOnHiding(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					CalendarView.this.root.disableProperty().set(false);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Opens up the view for an {@link Agenda} that is pressed in this {@link CalendarView},
	 * and retains information about in what {@link models.Calendar} the {@link Agenda} was pressed,
	 * as well 
	 */
	private void openAgenda() {
		AgendaView agenda = new AgendaView(root, user_name, cal_id);
		try {
			root.disableProperty().set(true);
			agenda.start(new Stage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Closes this view, as well as any other open view, commits nothing to the model when doing so.
	 */
	private void close() {
		System.exit(0);
	}
}
