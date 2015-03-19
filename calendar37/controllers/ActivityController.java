package controllers;

import utils.DatabaseInterface;
import models.Activity;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ActivityController {

	//fxml
	@FXML Text ActivityView_TitleText;
	@FXML Text ActivityView_CreatedBy;
	@FXML Text ActivityView_GroupText;
	@FXML Text ActivityView_DescriptionText;
	@FXML DatePicker ActivityView_StartDate;
	@FXML DatePicker ActivityView_EndDate;
	@FXML Text ActivityView_StartTime;
	@FXML Text ActivityView_EndTime;
	@FXML Button btn_close;
	
	public void setActivity_id(int activity_id) {
		DatabaseInterface dbi = new DatabaseInterface();
		Activity model = dbi.getActivity(activity_id);
		
		ActivityView_TitleText.setText("Aktivitet nr. " + activity_id);
		ActivityView_CreatedBy.setText(model.getActivity_owner());
		ActivityView_GroupText.setText(model.getParticipants().size() > 1 ? "~ GRUPPE ~" : "~ PERSONLIG ~");
		ActivityView_DescriptionText.setText(model.getDescription());
		ActivityView_StartDate.setValue(model.getStart_date());
		ActivityView_StartDate.setDisable(true);
		ActivityView_EndDate.setValue(model.getEnd_date());
		ActivityView_EndDate.setDisable(true);
		ActivityView_StartTime.setText(model.getFrom().toString());
		ActivityView_EndTime.setText(model.getTo().toString());
	}
	
	@FXML public void close() {
		Stage stage = (Stage) btn_close.getScene().getWindow();
		stage.close();
	}
}
