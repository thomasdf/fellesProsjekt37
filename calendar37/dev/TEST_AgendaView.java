package dev;



import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

public class TEST_AgendaView extends Application {

//	String viewName = "Calendar";
//	String viewName = "Agenda";
//	String viewName = "Dialogue";
	String viewName = "Invite";
//	String viewName = "Activity";
	
	@Override public void start(Stage primaryStage) throws Exception{
		
		//Sets the root
		Parent root = FXMLLoader.load(getClass().getResource("/views/CreateActivity.fxml"));
		Scene scene = new Scene(root);
		
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
