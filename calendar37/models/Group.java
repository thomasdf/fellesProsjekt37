package models;

import java.util.ArrayList;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;

/**
 * A Group-class acts in very much the same way as an {@link Account}-class, although the Group-class will
 * ""belong"" to more than one {@link Person}, which will lead to more than one {@link Person} being able
 * to see, edit and in other ways modify the {@link Calendar} it's linked to.
 * 
 * @author gruppe37
 * @version %I%, %G%
 */
public class Group {

	//Final attributes
	private final int group_id;
	private final String group_name;
	
	//Property-attributes
	private Property<ArrayList<String>> membersProperty = new ObjectPropertyBase<ArrayList<String>>() {
		@Override
		public Object getBean() {
			return this;
		}
		@Override
		public String getName() {
			return "members";
		}
	};
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
	private Property<ArrayList<Integer>> subgroupsProperty = new ObjectPropertyBase<ArrayList<Integer>>() {
		@Override
		public Object getBean() {
			return this;
		}
		@Override
		public String getName() {
			return "subgroups";
		}
	};
	
	//Constructor
	public Group(int group_id, String group_name) {
		this.group_id = group_id;
		this.group_name = group_name;
	}
	
	//Getters, Setters & Properties
	public int getGroup_id() {
		return group_id;
	}
	
	public String getGroup_name() {
		return group_name;
	}
	
	public ArrayList<String> getMembers() {
		return membersProperty.getValue();
	}
	public void setMembers(ArrayList<String> members) {
		membersProperty.setValue(members);
	}
	public Property<ArrayList<String>> membersProperty() {
		return membersProperty;
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
	
	public ArrayList<Integer> getSubgroups() {
		return subgroupsProperty.getValue();
	}
	public void setSubgroups(ArrayList<Integer> subgroups) {
		subgroupsProperty.setValue(subgroups);
	}
	public Property<ArrayList<Integer>> subgroupsProperty() {
		return subgroupsProperty;
	}
}
