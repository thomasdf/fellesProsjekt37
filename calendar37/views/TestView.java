package views;

import java.util.Iterator;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
//import javafx.scene.Parent;
//import javafx.fxml.FXMLLoader;

public class TestView extends Application {

	String viewName = "Calendar";
//	String viewName = "Agenda";
//	String viewName = "Dialogue";
	
	@Override public void start(Stage primaryStage) throws Exception{
		
		//Sets the root and creates the scene with a give size
//		Parent root = FXMLLoader.load(getClass().getResource("/views/" + viewName + "View.fxml"));
		AnchorPane root = new AnchorPane();
		
		
		//The different nodes
		GridPane calendar = new GridPane();
		Button profile = new Button("Min profil");
		//Add all nodes
		root.getChildren().addAll(calendar, profile);
		//The view
		AnchorPane.setTopAnchor(calendar, 0.0);
		AnchorPane.setBottomAnchor(calendar, 32.0);
		AnchorPane.setLeftAnchor(calendar, 0.0);
		AnchorPane.setRightAnchor(calendar, 0.0);
		AnchorPane.setBottomAnchor(profile, 4.0);
		AnchorPane.setLeftAnchor(profile, 8.0);
			//profile
		profile.setPrefWidth(256);
		profile.setPrefHeight(16);
			//calendar
		calendar.setId("calendar");
		calendar.add(new Label("Mandag"), 0, 0);
		calendar.add(new Label("Tirsdag"), 1, 0);
		calendar.add(new Label("Onsdag"), 2, 0);
		calendar.add(new Label("Torsdag"), 3, 0);
		calendar.add(new Label("Fredag"), 4, 0);
		calendar.add(new Label("Lørdag"), 5, 0);
		calendar.add(new Label("Søndag"), 6, 0);
		for (Node child : calendar.getChildren()) {
			GridPane.setHgrow(child, Priority.ALWAYS);
		}
		for (int i = 0; i < 7; i++) {
			for (int j = 1; j < 7; j++) {
				Label day_title = new Label("<ph>");
				VBox day_body = new VBox(2.0);
				VBox day = new VBox(4.0);
				day.getChildren().addAll(day_title, day_body);
				calendar.add(day, i, j);
			}
		}
			//the days
		
		
		//init the scene
		Scene scene = new Scene(root, 1600, 1024);
		
		//Adds a css-stylesheet to the scene
		scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
		
		//Initializes the stage and shows it
		primaryStage.setTitle(viewName);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
