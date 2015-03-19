package views;

import utils.DatabaseInterface;
import controllers.LoginController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class MainApplication extends Application {

	Parent root;
	LoginController login_controller;
	String user_name;
	int cal_id;
	
	DatabaseInterface dbi = new DatabaseInterface();
	
	@Override public void start(Stage primaryStage) throws Exception{
		
		//Sets the root
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginView.fxml"));
		root = (Parent) loader.load();
		login_controller = (LoginController) loader.getController();
		login_controller.setStage(primaryStage);
		Scene scene = new Scene(root);
		
		//Adds a css-stylesheet to the scene
		scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
		
		//Initializes the stage and shows it
		primaryStage.setTitle("Calendar37 - Logg inn");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				System.exit(0);
			}
		});
		primaryStage.setOnHiding(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				if (login_controller.isOk()) {
					user_name = login_controller.getUser_name();
					cal_id = dbi.getCalendarId(user_name);
					openCalendar();
				} else {
					we.consume();
				}
			}
		});
	}
	
	private void openCalendar() {
		CalendarView calendar = new CalendarView(user_name, cal_id);
		try {
			calendar.start(new Stage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
