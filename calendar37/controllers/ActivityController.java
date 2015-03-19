package controllers;

import utils.DatabaseInterface;
import models.Activity;
import models.Calendar;
import models.Invite;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ActivityController {

	//fxml
	@FXML ToggleButton tgb_status;
	@FXML Text ActivityView_TitleText;
	@FXML TextField ActivityView_CreatedBy;
	@FXML TextField ActivityView_GroupText;
	@FXML TextField ActivityView_RoomText;
	@FXML DatePicker ActivityView_StartDate;
	@FXML DatePicker ActivityView_EndDate;
	@FXML TextField ActivityView_StartTime;
	@FXML TextField ActivityView_EndTime;
	@FXML TextArea ActivityView_DescriptionText;
	@FXML Button btn_close;
	
	private String user_name;
	private int activity_id;
	
	DatabaseInterface dbi = new DatabaseInterface();
	
	public void init(String user_name, int activity_id) {
		this.user_name = user_name;
		this.activity_id = activity_id;
		
		try {
			Invite inv = dbi.getInvite(activity_id, user_name);
			if (inv != null) {
				if (inv.getStatus().equals("true")) {
					tgb_status.setSelected(true);
				} else {
					tgb_status.setSelected(false);
				}
				tgb_status.visibleProperty().set(true);
			} else {
				tgb_status.visibleProperty().set(false);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		Activity model = dbi.getActivity(activity_id);
		
		ActivityView_TitleText.setText("Aktivitet nr. " + activity_id);
		ActivityView_CreatedBy.setText(model.getActivity_owner());
		if (model.getParticipants().isEmpty() && tgb_status.isSelected()) {
			ActivityView_GroupText.setText(user_name);
		} else if (!model.getParticipants().isEmpty()){
			for (int cal_id : model.getParticipants()) {
				Calendar cal = dbi.getCalendar(cal_id);
				if (!cal.getIs_group_cal()) {
					String participant = cal.getCalendar_owner_user();
					String status = dbi.getInvite(activity_id, participant).getStatus() == "true" ? " [SKAL]" : dbi.getInvite(activity_id, participant).getStatus() == "false" ? " [SKAL IKKE]" : "";
					ActivityView_GroupText.appendText(participant + status + ", ");
				}
			}
			ActivityView_GroupText.setText(ActivityView_GroupText.getText(0, ActivityView_GroupText.getLength() - 2));
		}
		if (model.getRoom() != null) {
			if (!model.getRoom().isEmpty()) {
				ActivityView_RoomText.setText(model.getRoom());
			} else {
				ActivityView_RoomText.setText("-");
			}
		} else {
			ActivityView_RoomText.setText("-");
		}
		ActivityView_DescriptionText.setText(model.getDescription());
		ActivityView_StartDate.setValue(model.getStart_date());
		ActivityView_EndDate.setValue(model.getEnd_date());
		ActivityView_StartTime.setText(model.getFrom().toString());
		ActivityView_EndTime.setText(model.getTo().toString());
	}
	
	@FXML public void changeStatus() {
		dbi.changeInvitedStatus(activity_id, user_name);
	}
	
	@FXML public void close() {
		Stage stage = (Stage) btn_close.getScene().getWindow();
		stage.close();
	}
}
