package utils;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import models.Account;
import models.Activity;
import models.Calendar;
import models.Group;
import models.Invite;
import models.Notification;

/**
 * 
 * DatabaseInterface provides a connection to the database. This is based on the
 * user for select, update, but not for administrating options like alter and
 * drop.
 * 
 * @author
 * @version %I%, %G%
 * @since 1.0
 */
public class DatabaseInterface {

	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	/*
	 * database server up and running, these variables were used for testing
	 * purposes only.
	 * 
	 * Perhaps make class usable for different users by including credentials in
	 * the constructor instead of making it a static final field.
	 */
	
	
	private static final String DB_URL = "jdbc:mysql://mysql.stud.ntnu.no/thomasdf_fellesprosjekt";
	private static final String USERNAME = "thomasdf_fellesp";

	private static final String PASSWORD = "bringIt";
	
	private String localTimetoDatabaseTime(LocalTime time){
		String dbTime = time.toString();
		return dbTime.substring(0,5) + ":00";
	}

	/**
	 * Fetches the activity with the given id from the database. Throws an
	 * SQLException if something goes wrong, then it returns an Activity object
	 * with no fields set but the id.
	 * 
	 * @param activity_id
	 *            the identity of the activity in question
	 * @return returns an Activity-model-object with the information required.
	 */
	public Activity getActivity(int activity_id) {
		Activity act = null;
		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;
		try {

			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method

			result = statement
					.executeQuery("select * from activity where activity.activity_id="
							+ activity_id);
			if (result.next()) {
				act = new Activity(result.getInt("activity_id"),
						result.getInt("calendar_id"),
						result.getString("owner_user_name"));
				act.setFrom(result.getTime("start_time").toLocalTime());
				act.setTo(result.getTime("end_time").toLocalTime());
				act.setStart_date(result.getDate("activity_date").toLocalDate());
				act.setEnd_date(result.getDate("end_date").toLocalDate());
				act.setDescription(result.getString("description"));
			}
			result.close();
			result = statement
					.executeQuery("select user_name from invited where activity_id="
							+ activity_id + " and invitation_status='true'");
			ArrayList<String> user_names = new ArrayList<String>();
			while (result.next()) {
				user_names.add(result.getString("user_name"));
			}
			ArrayList<Integer> calendar_ids_for_user_names = new ArrayList<>();
			for (int i = 0; i < user_names.size(); i++) {
				calendar_ids_for_user_names
						.add(getCalendarId(user_names.get(i)));
			}
			act.setParticipants(calendar_ids_for_user_names);
			
			//getRooms
			
			act.setRoom(getRoomName(activity_id));
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
		return act;
	}

/**
 * Creates a new Activity in the database, and creates relation between the new activity and the calendar in the database.
 * @param owner_user_name - the username for the owner/administrator of the activity
 * @param description - the description for the activity (max 256 characters)
 * @param activity_date - the start-date for the activity
 * @param end_date - the date the activity is to end
 * @param start_time - the time for the activity-start
 * @param end_time - the planned time for ending the activity
 * @param room_name - the name of the room which is to be used.
 * @return
 */
	public Activity setActivity(String owner_user_name, String description,
			LocalDate activity_date, LocalDate end_date, LocalTime start_time,
			LocalTime end_time, String room_name) {
		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;
		int calendar_id = 0;
		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method
			
			result = statement.executeQuery("select calendar.calendar_id from calendar, account, hasCalendar where account.user_name = hasCalendar.user_name and hasCalendar.calendar_id = calendar.calendar_id and account.user_name = '" + owner_user_name + "'");
			if(result.next()){
				calendar_id = result.getInt("calendar_id");
				
			} else {
				throw new SQLException("empty set");
			}
			
			if(room_name != null){
				room_name = "'" + room_name + "'";
			}
			
			statement.executeUpdate("INSERT INTO activity (calendar_id, description, activity_date, end_date, start_time, end_time, owner_user_name, room_name) VALUES ("
					+ calendar_id + ", '" + description + "', '" + activity_date.toString()
					+ "', '" + end_date.toString() + "', '" + localTimetoDatabaseTime(start_time) + "', '" + localTimetoDatabaseTime(end_time)
					+ "', '" + owner_user_name + "', " + room_name + ")");
			
			result.close();
			// find the id of the new activity
			result = statement
					.executeQuery("SELECT LAST_INSERT_ID()");
			if(result.next()){
			int activity_id = result.getInt(1);
			return getActivity(activity_id);
			}else{
				throw new SQLException();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
		return null;
	}
	
	
	/**
	 * Returns the description of the activity defined by the id.
	 * 
	 * @param activity_id
	 *            the identity of the activity in question
	 * @return String variable which contains the description
	 */
	public String getActivityDesc(int activity_id) {
		String s = "";
		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;
		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method
			result = statement
					.executeQuery("select description from activity where activity.activity_id="
							+ activity_id);

			if (result.next()) {
				s = result.getString("description");
			}
			result.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
		return s;
	}

	/**
	 * Method to get the start and end time from the database for the given activity
	 * 
	 * @param activity_id
	 *            the identity of the activity in question
	 * @return ArrayList<LocalTime> with start_time and end_time
	 */
	public ArrayList<LocalTime> getFromTo(int activity_id) {
		ArrayList<LocalTime> time = new ArrayList<LocalTime>();
		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;
		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method
			result = statement
					.executeQuery("select start_time, end_time from activity where activity.activity_id="
							+ activity_id);
			if (result.next()) {
				time.add(result.getTime("start_time").toLocalTime());
				time.add(result.getTime("end_time").toLocalTime());
			}
			result.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
		return time;
	}

	/**
	 * Fetched the calendar id of the given activity from the database. Returns
	 * 0 if something goes wrong
	 * 
	 * @param id
	 *            the identity of the activity in question
	 * @return integer calendar_id
	 */
	public int getCalendarId(int activity_id) {
		int cal_id = 0;

		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;

		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method
			result = statement
					.executeQuery("select calendar_id from activity where activity_id="
							+ activity_id);
			if (result.next()) {
				cal_id = result.getInt("calendar_id");
			}
			result.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
		return cal_id;
	}

	/**
	 * Creates a calendar-object from the database. The calendar-object is
	 * created as either a personal calendar or a group calendar depending on
	 * the returned values from the database.
	 * 
	 * @param calendar_id
	 *            the id for the calendar we want returned
	 * @return a Calendar-object from the database.
	 */
	@SuppressWarnings("resource")
	public Calendar getCalendar(int calendar_id) {
		Calendar cal = null;
		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;
		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method
			result = statement
					.executeQuery("select hasCalendar.user_name from hasCalendar where hasCalendar.calendar_id = "
							+ calendar_id);
			if (result.next()) { // the calendar_id corresponds to a user_name
									// in the database
				String user_name = result.getString("user_name");
				cal = new Calendar(calendar_id, user_name);

				ArrayList<Activity> activities_list = getAllActivities(cal
						.getCalendar_owner_user());
				ArrayList<Integer> act_list = new ArrayList<Integer>();

				for (int i = 0; i < activities_list.size(); i++) {
					act_list.add(activities_list.get(i).getActivity_id());
				}
				cal.setActivities(act_list);
				return cal;
			} else {
				result = statement
						.executeQuery("select groupHasCalendar.group_id from groupHasCalendar where groupHasCalendar.calendar_id = "
								+ calendar_id);
				if (result.next()) { // the calendar_id corresponds to a
										// group_id in the database
					int group_id = result.getInt("group_id");
					cal = new Calendar(calendar_id, group_id);
					ArrayList<Activity> activities_list = getAllActivities(group_id);
					ArrayList<Integer> act_list = new ArrayList<Integer>();

					for (int i = 0; i < activities_list.size(); i++) {
						act_list.add(activities_list.get(i).getActivity_id());
					}
					cal.setActivities(act_list);
					return cal;
				} else {
					throw new SQLException(
							"empty set returned from the database. This is a database inconsistency, and there is no calendar registered with this id");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
		return cal;
	}

	/**
	 * Fetches the name of the room that is booked for an activity. Returns an
	 * empty string if something goes wrong
	 * 
	 * @param activity_id
	 *            the identity of the activity in question
	 * @return returns a String that is the primary key of the room
	 */
	public String getRoomName(int activity_id) {
		String room_name = "";

		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;

		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method
			result = statement
					.executeQuery("select room.* from activity, room where activity.room_name= room.room_name and activity_id="
							+ activity_id);
			if (result.next()) {
				room_name = result.getString("room_name");
			}
			result.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
		return room_name;
	}
	
	/**
	 * Returns a list of the rooms available inside the dates/time
	 * @param start_date - the start date of the interval  we want to check if rooms are available
	 * @param end_date - the end date of the interval we want to check if rooms are available
	 * @param start_time - the start time of the interval we want to check if rooms are available
	 * @param end_time - the end time of the interval we want to check if rooms are available
	 * @return Returns an arrayList of rooms available
	 */
	public ArrayList<String> getAvailableRooms(LocalDate start_date, LocalDate end_date, LocalTime start_time, LocalTime end_time){
		ArrayList<String> room_list = new ArrayList<>();

		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;
		
		String db_start_time = localTimetoDatabaseTime(start_time);
		String db_end_time = localTimetoDatabaseTime(end_time);

		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method
			result = statement.executeQuery("SELECT room_name FROM activity WHERE "
					+ "(NOT(activity_date<='" + start_date.toString() + "' AND end_date>='"
					+ start_date.toString() + "') OR NOT(activity_date<='" + end_date.toString()
					+ "' AND end_date>='" + end_date.toString() + "')) AND "
					+ "(NOT(start_time<='" + db_start_time + "' AND end_time>='"
							+ db_start_time + "') OR NOT(start_time<='" + db_end_time
							+ "' AND end_time>='" + db_end_time + "'))");
			while(result.next())	{
				if(room_list.contains(result.getString("room_name")) || result.getString("room_name") == null)	{
					continue;
				}
				room_list.add(result.getString("room_name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
		return room_list;
	}
	
	/**
	 * Returns a list of all the rooms
	 * @return Returns an arrayList of rooms.
	 */
	public ArrayList<String> getAllRooms(){
		ArrayList<String> roomlist = new ArrayList<>();

		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;

		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method
			result = statement
					.executeQuery("select room.* from room");
			while(result.next()) {
				roomlist.add(result.getString("room_name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
		return roomlist;
	}
	
	/**
	 * Creates a group in the database.
	 * @param group_name - the name of the group
	 * @param members_user_name - a list of user names for the accounts that are in the group
	 * @return
	 */
	public Group setGroup(String group_name, ArrayList<String> members_user_name){
	Connection connection = null;
	ResultSet result = null;
	Statement statement = null;
	try {
		// create new connection and statement
		Class.forName(DB_DRIVER);
		connection = DriverManager
				.getConnection(DB_URL, USERNAME, PASSWORD);
		statement = connection.createStatement();
		// method
			
		statement.executeUpdate("INSERT INTO calendarGroup (group_name) VALUES ( '"
				+ group_name + " ')");
		// find the id of the new group
		result = statement
				.executeQuery("SELECT LAST_INSERT_ID()");
		if(result.next()){
		int group_id = result.getInt(1);
		
		if(members_user_name.size() > 0){
			String groupMemberStatement = "INSERT INTO isMember VALUES ";
			for (int i = 0; i < members_user_name.size(); i++) {
				groupMemberStatement += "(" + group_id + ", '" + members_user_name.get(i) + "')";
				if(i == (members_user_name.size() - 1)){
					groupMemberStatement += ";";
				} else {
					groupMemberStatement += ", ";
				}
			}
			statement.executeUpdate(groupMemberStatement);
		}
		return getGroup(group_id);
		}else{
			throw new SQLException();
		}
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		if (result != null) {
			try {
				result.close();
			} catch (SQLException e) {

			}
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {

			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {

			}
		}
	}
	return null;
		
	}

	/**
	 * Fetches the group with this id from the database.
	 * 
	 * @param group_id
	 *            the identity of the group in question
	 * @return returns a group object
	 */
	public Group getGroup(int group_id) {
		Group group = null;
		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;
		ArrayList<String> member = new ArrayList<String>();
		ArrayList<Integer> sub_groups = new ArrayList<Integer>();
		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method
			result = statement
					.executeQuery("select * from calendarGroup where calendarGroup.group_id="
							+ group_id);
			if (result.next()) {
				group = new Group(group_id, result.getString("group_name"));
			}
			result.close();
			result = statement
					.executeQuery("select account.user_name from account, isMember where account.user_name=isMember.user_name and isMember.group_id="
							+ group_id);
			while (result.next()) {
				member.add(result.getString(1));
			}
			group.setMembers(member);
			result.close();
			result = statement
					.executeQuery("select subGroup.subGroup_id from subGroup where superGroup_id="
							+ group_id);
			while (result.next()) {
				sub_groups.add(result.getInt(1));
			}
			group.setSubgroups(sub_groups);
			result.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
		return group;
	}
	
	/**
	 * Returns an ArrayList of all groups in the database.
	 * @return An ArrayList<Group> of all groups in the database.
	 */
	public ArrayList<Group>getAllGroups() {

		ArrayList<Group> groupsList= new ArrayList<>(); 

		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;

		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method
			result = statement
					.executeQuery("select calendarGroup.* from calendarGroup");
			while(result.next()){
				Group group = getGroup(result.getInt("group_id"));
				groupsList.add(group);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
		return groupsList;
	}

	/**
	 * Fetches the user_name of the admin connected to the given group.
	 * 
	 * @param group_id
	 *            the id for the group in question
	 * @return returns a user name which is unique for that account
	 */
	public String getGroupAdmin(int group_id) {
		String admin_user_name = null;
		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;
		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method
			result = statement
					.executeQuery("select account.user_name from calendarGroup, account, isMember where account.user_name=isMember.username and isMember.group_id=calendarGroup.group_id and calendarGroup.group_id="
							+ group_id + " and isMember.role='admin'");
			if (result.next()) {
				admin_user_name = result.getString("user_name");
			}
			result.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
		return admin_user_name;
	}

	/**
	 * Returns the full name for a person.
	 * @param user_name the user name for the account related to the person in question
	 * @return an ArrayList of the full name. The first element is the name, and the second element is the surname
	 */
	public ArrayList<String> getFullName(String user_name) {
		ArrayList<String> fullName = new ArrayList<String>();
		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;
		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method
			result = statement
					.executeQuery("select account.first_name, account.last_name from account where account.user_name = "
							+ "\"" + user_name + "\"");
			result.next();
			fullName.add(result.getString("first_name"));
			fullName.add(result.getString("last_name"));
		} catch (Exception e) {
			System.out.println("Error from DatabaseInterface: "
					+ e.getLocalizedMessage());
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
		return fullName;
	}

	/**
	 * Returns a mobile number registered in an account in the database.
	 * 
	 * @param user_name
	 *            the user name which is the primary key of the account we want
	 *            to find the mobile number from.
	 * @return the mobile number for the account
	 */
	public String getMobile(String user_name) {
		String mobile = null;

		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;

		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method
			result = statement
					.executeQuery("select account.mobile_nr from account where account.user_name = "
							+ "\"" + user_name + "\"");
			result.next();
			mobile = result.getString(1);
		} catch (Exception e) {
			System.out.println("Error from DatabaseInterface: "
					+ e.getLocalizedMessage());
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
		return mobile;
	}

	/**
	 * Returns a password registered in an account in the database.
	 * 
	 * @param user_name
	 *            the user name which is the primary key of the account we want
	 *            to find the password from.
	 * @return the password for the account.
	 */
	public String getPassword(String user_name) {
		String password = null;
		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;
		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method
			result = statement
					.executeQuery("select account.user_password from account where account.user_name = "
							+ "\"" + user_name + "\"");
			result.next();
			password = result.getString(1);
		} catch (Exception e) {
			System.out.println("Error from DatabaseInterface: "
					+ e.getLocalizedMessage());
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
		return password;
	}

	/**
	 * Checks if the user_name exists in the database.
	 * 
	 * @param user_name
	 *            the user_name we want to check if is in the database
	 * @return Returns boolean True if the user_name is in the database, false
	 *         otherwise.
	 */
	public boolean isUsername(String user_name) {
		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;
		boolean is_user_name = false;
		if (user_name.isEmpty() || user_name.equals("")) {
			return is_user_name;
		}
		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method
			result = statement
					.executeQuery("select account.user_name from account where account.user_name = \""
							+ user_name + "\"");
			if (result.next()) {
				if (result.getString("user_name").equals(user_name)) {
					is_user_name = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
		return is_user_name;
	}

	/**
	 * Returns an ID for the calendar related to an account
	 * 
	 * @param user_name
	 *            the user name which is the primary key of the account we want
	 *            to find the calendarID from.
	 * @return the calendardId for the account.
	 */
	public int getCalendarId(String user_name) {
		int calendarId = 0;
		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;
		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method
			result = statement
					.executeQuery("select calendar.calendar_id from account, calendar, hasCalendar where hasCalendar.user_name = account.user_name and hasCalendar.calendar_id = calendar.calendar_id and hasCalendar.user_name = "
							+ "\"" + user_name + "\"");
			result.next();
			calendarId = result.getInt("calendar_id");
		} catch (Exception e) {
			System.out.println("Error from DatabaseInterface: "
					+ e.getLocalizedMessage());
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
		return calendarId;
	}

	/**
	 * Returns the Account-object corresponding to a given user_name
	 * 
	 * @param user_name
	 *            the user name which is the primary key for the account which
	 *            we want returned.
	 * @return the Account-object for the user_name.
	 */
	public Account getAccount(String user_name) {
		Account acc;
		ArrayList<String> fullname = getFullName(user_name);
		String password = getPassword(user_name);
		String mobile_nr = getMobile(user_name);
		acc = new Account(user_name, password, fullname.get(0),
				fullname.get(1), mobile_nr);
		return acc;
	}

	/**
	 * Updates an account and creates a new account in the database if an
	 * account with this user_name is not in the database. Also Creates a new calendar for the user.
	 * 
	 * @param account
	 *            the account we want to add/update in the database
	 */
	public void setAccount(Account account) {
		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;
		String user_name = account.getUsername();
		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method
			result = statement
					.executeQuery("select account.user_name from account where account.user_name = \""
							+ user_name + "\"");
			if(result.next()){
			if (result.getString(1) != null) {// check if account with same id
												// already exists
				statement.executeUpdate("UPDATE account SET user_password='"
						+ account.getPassword() + "', first_name='"
						+ account.getFirst_name() + "', last_name='"
						+ account.getLast_name() + "', mobile_nr='"
						+ account.getMobile_nr() + "')");
			} else {
				throw new SQLException("Inconsistency in the database. If problem persists, contact system administrator");
			}
			}else{// account does not exist, and new row created + new calendar created for user
				statement.executeUpdate("INSERT INTO account VALUES ('"
						+ user_name + "', '" + account.getPassword() + "', '"
						+ account.getFirst_name() + "', '"
						+ account.getLast_name() + "', '"
						+ account.getMobile_nr() + "')");
				statement.executeUpdate("INSERT INTO calendar VALUES (NULL)");
				result.close();
				result = statement.executeQuery("SELECT LAST_INSERT_ID() as last_id");
				if(result.next()){
				int calendar_id = result.getInt("last_id");
				result.close();
				
				statement.executeUpdate("INSERT INTO hasCalendar VALUES ('" + user_name + "', " + calendar_id + ")");
				} else {
					throw new SQLException("Could not get last_id for calendar from database");
				}
			}
		} catch (Exception e) {
					e.printStackTrace();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
	}
	
	/**
	 * Removes an account from the system. Only to be used by admin-account. Check if user_name exists in database before use.
	 * @param account_user_name User name for the account to be removed.
	 */
	public void removeAccount(String account_user_name) {
		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;
		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			statement.executeUpdate("DELETE FROM account WHERE user_name='" + account_user_name + "'");
		} catch (Exception e) {
			System.out.println("Error from DatabaseInterface: "
					+ e.getLocalizedMessage());
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
	}

	/**
	 * Returns an ArrayList of all Activity-objects related to an Account
	 * 
	 * @param user_name
	 *            the user name which is the primary key for the account
	 * @return an ArrayList<Activity> with all Activity-objects related to an
	 *         Account.
	 */
	public ArrayList<Activity> getAllActivities(String user_name) {
		ArrayList<Activity> activityList = new ArrayList<Activity>();
		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;
		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method
			result = statement
					.executeQuery("select activity.activity_id, activity.description, activity.start_time, activity.end_time, activity.activity_date, activity.end_date, activity.owner_user_name, activity.room_name from activity, account, calendar, hasCalendar where activity.calendar_id = calendar.calendar_id and hasCalendar.user_name = account.user_name and hasCalendar.calendar_id = calendar.calendar_id and hasCalendar.user_name = "
							+ "\"" + user_name + "\"");
			while (result.next()) {
				Activity act = getActivity(result.getInt("activity_id"));
				if (act != null) {
					activityList.add(act);
				}
			}
		} catch (Exception e) {
			System.out.println("Error from DatabaseInterface: "
					+ e.getLocalizedMessage());
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
		return activityList;
	}

	/**
	 * Returns an ArrayList of all Activity-objects related to a Group
	 * 
	 * @param group_id - the identity for the group we want to find all acitivities for.
	 * @return an ArrayList<Activity> with all Activity-objects related to a group
	 */
	public ArrayList<Activity> getAllActivities(int group_id) {
		ArrayList<Activity> activityList = new ArrayList<Activity>();
		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;
		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method
			result = statement
					.executeQuery("select activity.activity_id, activity.description, activity.start_time, activity.end_time, activity.activity_date, activity.end_date, activity.owner_user_name, activity.room_name from activity, calendarGroup, calendar, groupHasCalendar where activity.calendar_id = calendar.calendar_id and groupHasCalendar.group_id = calendarGroup.group_id and groupHasCalendar.calendar_id = calendar.calendar_id and groupHasCalendar.group_id = "
							+ group_id);
			while (result.next()) {
				Activity act = getActivity(result.getInt("activity_id"));
				if (act != null) {
					activityList.add(act);
				}
			}
		} catch (Exception e) {
			System.out.println("Error from DatabaseInterface: "
					+ e.getLocalizedMessage());
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
		return activityList;
	}


	/**
	 * Creates a supgroup-supergroup relation between groups
	 * 
	 * @param supergroup_id
	 *            the id for the group that is going to be the parent in the
	 *            relationship
	 * @param subgroup_id
	 *            the id for the group that is going to be the child in the
	 *            relationship
	 */
	public void setSubGroup(int supergroup_id, int subgroup_id) {
		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;
		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method
			statement.executeUpdate("insert into subGroup values("
					+ subgroup_id + ", " + supergroup_id + ")");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
	}

	/**
	 * Invites a user to the given activity
	 * 
	 * @param activity_id
	 *            id of the activity in question
	 * @param user_name
	 *            user_name that should be invited
	 */
	public void inviteAccount(int activity_id, String user_name) {
		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;
		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method
			statement.executeUpdate("INSERT INTO invited VALUES(" + activity_id
					+ ", '" + user_name + "', 'false')");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
	}
	
	/**
	 * invites all members of a group to an activity
	 * @param activity_id the id for the activity
	 * @param group_id the id for the group
	 */
	public void inviteGroup(int activity_id, int group_id) {
		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;
		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method
			// get user_names in group
			result = statement
					.executeQuery("SELECT user_name FROM isMember WHERE group_id="
							+ group_id);
			ArrayList<String> invited = new ArrayList<String>();
			while (result.next()) {
				invited.add(result.getString("user_name"));
			}
			result.close();
			// invite people in group
			for (String inv : invited) {
				this.inviteAccount(activity_id, inv);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
	}

	/**
	 * Returns an invite-object between an activity and an account. This is used
	 * to get information about one invite when we know both the activity and
	 * the user_name
	 * 
	 * @param activity_id
	 *            the id for the activity in the relation
	 * @param user_name
	 *            the user_name for the account in the relation
	 * @return One Invite-object
	 */
	public Invite getInvite(int activity_id, String user_name) {

		Invite invite = null;

		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;

		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method
			result = statement
					.executeQuery("SELECT invited.activity_id, invited.user_name, invited.invitation_status, activity.owner_user_name FROM activity, invited WHERE activity.activity_id = invited.activity_id and activity.activity_id = "
							+ activity_id
							+ " AND invited.user_name='"
							+ user_name + "'");
			if(result.next()){
				invite = new Invite(result.getString("owner_user_name"),
						result.getString("user_name"), result.getInt("activity_id"));
				invite.setStatus(result.getString("invitation_status"));				
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
		return invite;
	}

	/**
	 * Returns an arrayList of all the invites related to an account, to find out which activities the user is invited to.
	 * @param user_name the user_name for the user we want to get all invites from
	 * @return returns an arraylist of invites
	 */
	public ArrayList<Invite> getUserInvitedTo(String user_name) {
		ArrayList<Invite> invitesList = new ArrayList<>();

		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;

		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method
			result = statement
					.executeQuery("SELECT invited.activity_id, invited.user_name, invited.invitation_status, activity.owner_user_name FROM activity, invited, account where activity.activity_id = invited.activity_id and invited.user_name = account.user_name and account.user_name =  \""
							+ user_name + "\"");
			while (result.next()) {
				Invite invite = new Invite(result.getString("owner_user_name"),
						result.getString("user_name"),
						result.getInt("activity_id"));
				invite.setStatus(result.getString("invitation_status"));
				invitesList.add(invite);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
		return invitesList;
	}

	/**
	 * Returns a list of Invite-objects that are related to an activity in the
	 * database.
	 * 
	 * @param activity_id
	 *            the id for the activity we want to get all invites related to.
	 * @return an ArrayList of invite-objects
	 */
	@SuppressWarnings("resource")
	public ArrayList<Invite> getAllInvites(int activity_id) {
		ArrayList<Invite> invites = new ArrayList<Invite>();
		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;
		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method
			// first get owner
			result = statement
					.executeQuery("SELECT owner_user_name FROM activity WHERE activity_id="
							+ activity_id);
			result.next();
			String owner_user_name = result.getString("owner_user_name");
			// then get all the invited people
			result = statement
					.executeQuery("SELECT * FROM invited WHERE activity_id="
							+ activity_id);
			while (result.next()) {
				Invite invite = new Invite(owner_user_name,
						result.getString("user_name"), activity_id);
				invite.setStatus(result.getString("invitation_status"));
				invites.add(invite);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
		return invites;
	}

	/**
	 * Checks status to invitation on the given user to the given activity
	 * 
	 * @param activity_id
	 *            the given activity's id
	 * @param user_name
	 *            the user_name of the user invited
	 * @return boolean the status of the invitation
	 */
	public boolean isComing(int activity_id, String user_name) {
		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;
		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method
			result = statement
					.executeQuery("SELECT invitation_status FROM invited WHERE activity_id="
							+ activity_id
							+ " AND user_name='"
							+ user_name
							+ "'");
			result.next();
			boolean is_coming = result.getString(1).equals("true") ? false
					: true;
			result.close();
			return is_coming;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
		return false;
	}

	/**
	 * Changes status of invite to the opposite of what it is
	 * 
	 * @param activity_id the id for the activity
	 * @param user_name the username for the user
	 */
	public void changeInvitedStatus(int activity_id, String user_name) {
		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;
		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method
			result = statement
					.executeQuery("SELECT invitation_status FROM invited WHERE activity_id="
							+ activity_id
							+ " AND user_name='"
							+ user_name
							+ "'");
			result.next();
			String status = result.getString(1).equals("true") ? "false"
					: "true";
			result.close();
			statement.executeUpdate("UPDATE invited SET invitation_status='"
					+ status + "' WHERE activity_id=" + activity_id
					+ " AND user_name='" + user_name + "'");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
	}

	/**
	 * fetches all accounts from the database
	 * @return returns an ArrayList of account-objects
	 */
	public ArrayList<Account> getAllAccounts() {
		ArrayList<Account> people = new ArrayList<Account>();
		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;
		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method
			result = statement.executeQuery("SELECT * FROM account");
			while (result.next()) {
				people.add(new Account(result.getString("user_name"), result
						.getString("user_password"), result
						.getString("first_name"),
						result.getString("last_name"), result
								.getString("mobile_nr")));

			}
			result.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
		return people;
	}

	/**
	 * Returns an alarm(notification-object) from the database based on the
	 * user_name and activity_id given
	 * 
	 * @param user_name
	 *            the user_name we want the alarm for
	 * @param activity_id
	 *            the activity_id we want the alarm for
	 * @return returns a notification-object from information in the database
	 */
	public Notification getAlarm(String user_name, int activity_id) {

		Notification notification = null;

		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;

		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method
			result = statement
					.executeQuery("select alarm.* from alarm, account, activity where alarm.activity_id = activity.activity_id and account.user_name = alarm.user_name and activity.activity_id = "
							+ activity_id
							+ " and account.user_name = "
							+ "\""
							+ user_name + "\"");
			if (result.next()) {
				/* if alarm-object is to be used.
				String alarm_user_name = result.getString("user_name");
				int alarm_activity_id = result.getInt("activity_id");
				LocalTime alarm_time = result.getTime("alarm_time")
						.toLocalTime();
				*/
				String alarm_description = result.getString("description");
				notification = new Notification(alarm_description);
			} else {
				throw new Exception(
						"empty set returned from database, there is no alarm with this user_name and activity_id registered in the dabase");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
		return notification;
	}
	
	/**
	 * Returns an alarm(notification-object) from the database based on the
	 * user_name and activity_id given
	 * 
	 * @param user_name
	 *            the user_name we want the alarm for
	 * @return returns an ArrayList of notification-objects from the information in the database. If there are no alarms registered to this user_name an empty ArrayList is returned.
	 */
	public ArrayList<Notification>getAllAlarms(String user_name) {

		ArrayList<Notification> notificationlist = new ArrayList<>(); 

		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;

		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method
			result = statement
					.executeQuery("select alarm.* from alarm, account, activity where alarm.activity_id = activity.activity_id and account.user_name = alarm.user_name"
							+ " and account.user_name = "
							+ "\""
							+ user_name + "\"");
			if(result.next()){
				do{
					/* if alarm-object is to be used.
					String alarm_user_name = result.getString("user_name");
					int alarm_activity_id = result.getInt("activity_id");
					LocalTime alarm_time = result.getTime("alarm_time")
							.toLocalTime();
					*/
					String alarm_description = result.getString("description");
					Notification notification = new Notification(alarm_description);
					notificationlist.add(notification);
				} while(result.next());
			} else{//no alarms corresponding to that user_name
				return notificationlist;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
		return notificationlist;
	}
	
	/**
	 * returns an ArrayList of notification-objects from the database based on the
	 * user_name and activity_id given
	 * @param activity_id
	 *            the activity_id we want the alarm for
	 * @return returns an ArrayList of notification-objects from the information in the database. If there are no alarms registered to this activity_id an empty ArrayList is returned.
	 */
	public ArrayList<Notification>getAllAlarms(int activity_id) {

		ArrayList<Notification> notificationlist = new ArrayList<>(); 

		Connection connection = null;
		ResultSet result = null;
		Statement statement = null;

		try {
			// create new connection and statement
			Class.forName(DB_DRIVER);
			connection = DriverManager
					.getConnection(DB_URL, USERNAME, PASSWORD);
			statement = connection.createStatement();
			// method
			result = statement
					.executeQuery("select alarm.* from alarm, account, activity where alarm.activity_id = activity.activity_id and account.user_name = alarm.user_name"
							+ " and activity.activity_id = "
							+ activity_id);
			if(result.next()){
				do{
					/* if alarm-object is to be used.
					String alarm_user_name = result.getString("user_name");
					int alarm_activity_id = result.getInt("activity_id");
					LocalTime alarm_time = result.getTime("alarm_time")
							.toLocalTime();
					*/
					String alarm_description = result.getString("description");
					Notification notification = new Notification(alarm_description);
					notificationlist.add(notification);
				} while(result.next());
			} else{//no alarms corresponding to that user_name
				return notificationlist;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {

				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {

				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {

				}
			}
		}
		return notificationlist;
	}
}
