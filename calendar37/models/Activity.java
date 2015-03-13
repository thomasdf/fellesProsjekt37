package models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Activity is the class that is being initialized in the {@link Calendar} with an activity that certain
 * people could be invited to. It relates to, either {@link Account} or a {@link Group}, via an {@link Invite}.
 * The activity can push out a {@link Notification} to it's invited and/or attending subjects.
 * 
 * @author gruppe37
 * @version %I%, %G%
 */
public class Activity {

	//Final attributes
	private final int activity_id;
	private final int calendar_id;
	private final String activity_owner;
	
	//Property-attributes
	private StringProperty titleProperty = new SimpleStringProperty();
	private StringProperty descriptionProperty = new SimpleStringProperty();
	private StringProperty roomProperty = new SimpleStringProperty();
	private Property<LocalDate> start_dateProperty = new ObjectPropertyBase<LocalDate>(null) {
		@Override
		public Object getBean() {
			return this;
		}
		@Override
		public String getName() {
			return "start_date";
		}
	};
	private Property<LocalDate> end_dateProperty = new ObjectPropertyBase<LocalDate>(null) {
		@Override
		public Object getBean() {
			return this;
		}
		@Override
		public String getName() {
			return "end_date";
		}
	};
	private Property<LocalTime> fromProperty = new ObjectPropertyBase<LocalTime>(null) {
		@Override
		public Object getBean() {
			return this;
		}
		@Override
		public String getName() {
			return "from";
		}
	};
	private Property<LocalTime> toProperty = new ObjectPropertyBase<LocalTime>(null) {
		@Override
		public Object getBean() {
			return this;
		}
		@Override
		public String getName() {
			return "to";
		}
	};
	private ObservableList<Integer> participantsList = FXCollections.observableList(new ArrayList<Integer>());
	
	//Constructor
	public Activity(int activity_id, int calendar_id, String activity_owner) {
		this.activity_id = activity_id;
		this.calendar_id = calendar_id;
		this.activity_owner = activity_owner;
	}
	
	//Getters, Setters & Properties
	public int getActivity_id() {
		return activity_id;
	}
	public int getCalendar_id() {
		return calendar_id;
	}
	
	public String getActivity_owner() {
		return activity_owner;
	}
	
	public String getTitle() {
		return titleProperty.getValue();
	}
	public void setTitle(String title) {
		titleProperty.setValue(title);
	}
	public StringProperty titleProperty() {
		return titleProperty;
	}
	
	public String getDescription() {
		return descriptionProperty.getValue();
	}
	public void setDescription(String description) {
		descriptionProperty.setValue(description);
	}
	public StringProperty descriptionProperty() {
		return descriptionProperty;
	}
	
	public String getRoom() {
		return roomProperty.getValue();
	}
	public void setRoom(String room) {
		roomProperty.setValue(room);
	}
	public StringProperty roomProperty() {
		return roomProperty;
	}
	
	public LocalDate getStart_date() {
		return start_dateProperty.getValue();
	}
	public void setStart_date(LocalDate date) {
		start_dateProperty.setValue(date);
	}
	public Property<LocalDate> start_dateProperty() {
		return start_dateProperty;
	}
	
	public LocalDate getEnd_date() {
		return end_dateProperty.getValue();
	}
	public void setEnd_date(LocalDate date) {
		end_dateProperty.setValue(date);
	}
	public Property<LocalDate> end_dateProperty() {
		return end_dateProperty;
	}
	
	public LocalTime getFrom() {
		return fromProperty.getValue();
	}
	public void setFrom(LocalTime from) {
		fromProperty.setValue(from);
	}
	public Property<LocalTime> fromProperty() {
		return fromProperty;
	}
	
	public LocalTime getTo() {
		return toProperty.getValue();
	}
	public void setTo(LocalTime to) {
		toProperty.setValue(to);
	}
	public Property<LocalTime> toProperty() {
		return toProperty;
	}
	
	public ObservableList<Integer> getParticipants() {
		return participantsList;
	}
	public void addParticipant(int participant_id) {
		participantsList.add(participant_id);
	}
	public void setParticipants(ArrayList<Integer> participants) {
		participantsList.setAll(participants);
	}
}
