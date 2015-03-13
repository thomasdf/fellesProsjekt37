package utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import models.Activity;
import models.Group;
import models.Invite;

public class Main {
	
	public static void main(String[] args)	{
		
		DatabaseInterface db = new DatabaseInterface();
		db.inviteGroup(90909, 12121);
		System.out.println("Success!");
		db.closeItAll();
	}
	
	public static void printActivity(Activity act)	{
		System.out.println("Description: " + act.getDescription());
		System.out.println("Id: " + act.getActivity_id());
		System.out.println("Room: " + act.getRoom());
		System.out.println("Date: " + act.getDate());
		System.out.println("Start time: " + act.getFrom());
		System.out.println("End time: " + act.getTo());
		ObservableList<Integer> part = act.getParticipants();
		System.out.println("Participants:\n ");
		for(int i = 0; i < part.size(); i++)	{
			System.out.println(part.get(i));
		}
	}
	
	public static void printGroup(Group group)	{
		System.out.println("\n\n\n");
		System.out.println("Id: " + group.getGroup_id());
		System.out.println("Name: " + group.getGroup_name());
		ObservableList<String> part = group.getMembers();
		System.out.println("Members:\n ");
		for(String x : part)	{
			System.out.println(x);
		}
		ObservableList<Integer> sub = group.getSubgroups();
		System.out.println("SubGroups:\n ");
		for(Integer x : sub)	{
			System.out.println(x);
		}
	}

}
