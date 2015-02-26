package models;

import java.util.ArrayList;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;

/**
 * 
 * 
 * @author gruppe37
 * @version %I%, %G%
 */
public class Account {

	//Final attributes
	private final String username;
	private final int account_owner;
	private final String password;
	
	//Property-attributes
	private Property<ArrayList<Integer>> member_ofProperty = new ObjectPropertyBase<ArrayList<Integer>>() {
		@Override
		public Object getBean() {
			return this;
		}
		@Override
		public String getName() {
			return "member_of";
		}
	};
	private Property<ArrayList<Integer>> calendarsProperty = new ObjectPropertyBase<ArrayList<Integer>>() {
		@Override
		public Object getBean() {
			return this;
		}
		@Override
		public String getName() {
			return "calendars";
		}
	};
	
	//Constructor
	public Account(String username, int account_owner, String password) {
		this.username = username;
		this.account_owner = account_owner;
		this.password = password;
	}
	
	//Getters, Setters & Properties
	public String getUsername() {
		return username;
	}
	
	public int getAccount_owner() {
		return account_owner;
	}
	
	public String getPassword() {
		return password;
	}
	
	public ArrayList<Integer> getMember_of() {
		return member_ofProperty.getValue();
	}
	public void setMember_of(ArrayList<Integer> member_of) {
		member_ofProperty.setValue(member_of);
	}
	public Property<ArrayList<Integer>> member_ofProperty() {
		return member_ofProperty;
	}
	
	public ArrayList<Integer> getCalendars() {
		return calendarsProperty.getValue();
	}
	public void setCalendars(ArrayList<Integer> calendars) {
		calendarsProperty.setValue(calendars);
	}
	public Property<ArrayList<Integer>> calendarsProperty() {
		return calendarsProperty;
	}
}
