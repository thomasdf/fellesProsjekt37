package models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Activity {

	//Final attributes
	private final int activity_id;
	
	//Property-attributes
	private StringProperty descriptionProperty = new SimpleStringProperty();
	private StringProperty roomProperty = new SimpleStringProperty();
	private Property<LocalDate> dateProperty = new ObjectPropertyBase<LocalDate>(null) {
		@Override
		public Object getBean() {
			return this;
		}
		@Override
		public String getName() {
			return "date";
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
	private Property<ArrayList<Integer>> participantsProperty = new ObjectPropertyBase<ArrayList<Integer>>() {
		@Override
		public Object getBean() {
			return this;
		}
		@Override
		public String getName() {
			return "participants";
		}
	};
	
	//Constructor
	public Activity(int activity_id) {
		this.activity_id = activity_id;
	}
	
	//Getters, Setters & Properties
	public int getActivity_id() {
		return activity_id;
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
	
	public LocalDate getDate() {
		return dateProperty.getValue();
	}
	public void setDate(LocalDate date) {
		dateProperty.setValue(date);
	}
	public Property<LocalDate> dateProperty() {
		return dateProperty;
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
	
	public ArrayList<Integer> getParticipants() {
		return participantsProperty.getValue();
	}
	public void setParticipants(ArrayList<Integer> participants) {
		participantsProperty.setValue(participants);
	}
	public Property<ArrayList<Integer>> participantsProperty() {
		return participantsProperty;
	}
}
