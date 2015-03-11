package views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class AgendaView extends Application {

	String viewName = "Agenda";
	
	
	//The model for this view
	private models.Calendar model;
	
	
	//Variables we need defined outside the "start"-function
		//View-elements
	private Label ag_title = new Label("<min agenda>");
	private ChoiceBox<String> timespan = new ChoiceBox<String>();
	private GridPane header = new GridPane();
	private VBox agenda_body = new VBox();
	private ScrollPane agenda = new ScrollPane(agenda_body);
	private Button close = new Button("<close>");
	private Button profile = new Button("<min profil>");
	private HBox footer = new HBox();
	
		//Lists containing current days and activities
	private ArrayList<Label> days = new ArrayList<Label>();
	private ArrayList<VBox> day_activities = new ArrayList<VBox>();
	
		//Useful final variables
	private final List<String> months = Arrays.asList("Januar", "Februar", "Mars", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Desember");
	private final List<String> weekdays = Arrays.asList("Mandag", "Tirsdag", "Onsdag", "Torsdag", "Fredag", "Lørdag", "Søndag");
	
	
	@Override public void start(Stage primaryStage) throws Exception{
		
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
		GridPane.setHgrow(agenda, Priority.ALWAYS);
		GridPane.setVgrow(agenda, Priority.ALWAYS);
		GridPane.setHgrow(footer, Priority.ALWAYS);
		GridPane.setHgrow(ag_title, Priority.ALWAYS);
//		AnchorPane.setTopAnchor(ag_title, 0.0);
//		AnchorPane.setLeftAnchor(ag_title, 0.0);
//		AnchorPane.setRightAnchor(timespan, 0.0);
//		AnchorPane.setBottomAnchor(timespan, 0.0);
			//header
		header.add(ag_title, 0, 0);
		header.add(timespan, 1, 0);
			//agenda
			//footer
		footer.getChildren().addAll(close, profile);
		
		
		//Add all nodes and init the scene and add css
		root.add(header, 0, 0);
		root.add(agenda, 0, 1);
		root.add(footer, 0, 2);
		Scene scene = new Scene(root, 512, 768);
		scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
		//Initializes the stage and shows it
		primaryStage.setTitle(viewName);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		//Sets focus to the profile-button
		profile.requestFocus();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
