package views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
//import javafx.scene.Parent;
//import javafx.fxml.FXMLLoader;

public class Test_CalendarView extends Application {

	String viewName = "Calendar";
//	String viewName = "Agenda";
//	String viewName = "Dialogue";
	
	@Override public void start(Stage primaryStage) throws Exception{
		
		//Sets the root
//		Parent root = FXMLLoader.load(getClass().getResource("/views/" + viewName + "View.fxml"));
		AnchorPane root = new AnchorPane();
		
		
		//The different nodes
		GridPane calendar = new GridPane();
		HBox header = new HBox();
		header.getStyleClass().add("header");
		Label cal_title = new Label("<kalendernavn>");
		cal_title.getStyleClass().add("title");
		Label cur_month = new Label("<måned>");
		cur_month.getStyleClass().add("month");
		Button prev_month = new Button("Forrige");
		prev_month.getStyleClass().add("left");
		Button next_month = new Button("Neste");
		next_month.getStyleClass().add("right");
		HBox footer = new HBox();
		footer.getStyleClass().add("footer");
		Button profile = new Button("Min profil");
		Button tasks = new Button("Aktivitets-agenda");
		//The view
			//General restraints
		AnchorPane.setTopAnchor(calendar, 64.0);
		AnchorPane.setLeftAnchor(calendar, 0.0);
		AnchorPane.setTopAnchor(header, 0.0);
		AnchorPane.setLeftAnchor(header, 0.0);
		AnchorPane.setBottomAnchor(footer, 0.0);
		AnchorPane.setLeftAnchor(footer, 0.0);
			//header
		header.getChildren().addAll(cal_title, cur_month, prev_month, next_month);
			//footer
		profile.setPrefWidth(256);
		profile.setPrefHeight(16);
		tasks.setPrefWidth(256);
		tasks.setPrefHeight(16);
		footer.getChildren().addAll(profile, tasks);
			//calendar
		calendar.setId("calendar");
		List<String> weekdays = Arrays.asList("Mandag", "Tirsdag", "Onsdag", "Torsdag", "Fredag", "Lørdag", "Søndag");
			//the days
		for (int i = 0; i < weekdays.size(); i++) {
			Label weekday = new Label(weekdays.get(i));
			weekday.getStyleClass().add("weekday");
			calendar.add(weekday, i, 0);
			GridPane.setHgrow(weekday, Priority.ALWAYS);
		}
		ArrayList<Label> days = new ArrayList<Label>();
		ArrayList<VBox> days_activities = new ArrayList<VBox>();
		for (int i = 1; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				Label day_title = new Label("<date>");
				VBox day_body = new VBox(2.0);
				VBox day = new VBox(4.0);
				days.add(day_title);
				days_activities.add(days.indexOf(day_title), day_body);
				day.setPrefHeight(9999);
				day.getStyleClass().add("day");
//				day_body.getChildren().add(new Button("<First Activity>"));
//				day_body.getChildren().add(new Button("<Second Activity>"));
				day.getChildren().addAll(day_title, day_body);
				calendar.add(day, j, i);
				GridPane.setHgrow(day, Priority.ALWAYS);
				day_title.getStyleClass().add("date");
			}
		}
		
		
		//TESTVALUES:
		String t_year = "2015";
		String t_month = "Mars";
		String t_next_month = "April";
		
		cal_title.setText("Gruppe 37s kalender");
		cur_month.setText(t_month + " " + t_year);
		for (int i = 0; i < days.size(); i++){
			int index = i + 25;
			Label title = days.get(i);
			String filler = "";
			if (index == 31){
				filler += t_month.substring(0, 3) + " ";
			} else if (index == 62){
				filler += t_next_month.substring(0, 3) + " ";
			}
			filler += Integer.toString(index%31 + 1);
			title.setText(filler);
			if (index < 31 || index > 61){
				title.setOpacity(0.5);
				days_activities.get(i).setOpacity(0.5);
			}
		}
		VBox container = days_activities.get(4);
		Button t_act1 = new Button("16:00 - Møte med sjefen");
		Button t_act2 = new Button("20:00 - Middag med kona");
		container.getChildren().addAll(t_act1, t_act2);
		container = days_activities.get(16);
		t_act1 = new Button("Besøk av tante");
		t_act2 = new Button("10:30 - Uksemøte");
		container.getChildren().addAll(t_act1, t_act2);
		container = days_activities.get(40);
		t_act1 = new Button("Fest med undassene");
		t_act2 = new Button("Nach med faglærer");
		container.getChildren().addAll(t_act1, t_act2);
		
		
		//Add all nodes and init the scene
		root.getChildren().addAll(header, calendar, footer);
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
