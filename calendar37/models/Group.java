package models;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A Group-class acts in very much the same way as an {@link Account}-class, although the Group-class will
 * ""belong"" to more than one user, which will lead to more than one user being able
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
	private ObservableList<String> membersList = FXCollections.observableList(new ArrayList<String>());
	private ObservableList<Integer> in_groupsList = FXCollections.observableList(new ArrayList<Integer>());
	private ObservableList<Integer> subgroupsList = FXCollections.observableList(new ArrayList<Integer>());
	
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
	
	public ObservableList<String> getMembers() {
		return membersList;
	}
	public void addMember(String member_id) {
		membersList.add(member_id);
	}
	public void setMembers(ArrayList<String> members) {
		membersList.addAll(members);
	}
	
	public ObservableList<Integer> getIn_groups() {
		return in_groupsList;
	}
	public void addIn_group(int group_id) {
		in_groupsList.add(group_id);
	}
	public void setIn_groups(ArrayList<Integer> in_groups) {
		in_groupsList.addAll(in_groups);
	}
	
	public ObservableList<Integer> getSubgroups() {
		return subgroupsList;
	}
	public void addSubgroup(int group_id) {
		subgroupsList.add(group_id);
	}
	public void setSubgroups(ArrayList<Integer> subgroups) {
		subgroupsList.addAll(subgroups);
	}
}
