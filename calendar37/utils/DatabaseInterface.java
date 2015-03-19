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
import models.Room;

/**
 * 
 * DatabaseInterface provides a connection to the database. This is based on the
 * user for select, update, but not for administrating options like alter and
 * drop.
 * <p>
 * Remeber to always close the connection when done with the object.
 * <p>
 * To use this class you must first provide the jdbc connector projects build
 * path. It can be found at http://dev.mysql.com/downloads/connector/j/.
 * <p>
 * 
 * @author
 * @version %I%, %G%
 * @since 1.0
 */
public class DatabaseInterface {

	/*
	 * Mal for � lage databaseinterface-metoder. husk � lage
	 * javadoc-dokumentasjon til hver metode
	 * 
	 * public Object getObject(params) {
	 * 
	 * Object object = null;
	 * 
	 * Connection connection = null; ResultSet result = null; Statement
	 * statement = null;
	 * 
	 * try { // create new connection and statement Class.forName(DB_DRIVER);
	 * connection = DriverManager .getConnection(DB_URL, USERNAME, PASSWORD);
	 * statement = connection.createStatement(); // method result = statement
	 * .executeQuery(
	 * "SELECT invited.activity_id, invited.user_name, invited.invitation_status, activity.owner_user_name FROM activity, invited WHERE activity.activity_id = invited.activity_id and activity.activity_id = "
	 * + activity_id + " AND invited.user_name='" + user_name + "'");
	 * result.next(); invite = new Invite(result.getString("owner_user_name"),
	 * result.getString("user_name"), result.getInt("activity_id"));
	 * invite.setStatus(result.getString("invitation_status")); } catch
	 * (Exception e) { e.printStackTrace(); } finally { if (result != null) {
	 * try { result.close(); } catch (SQLException e) {
	 * 
	 * } } if (statement != null) { try { statement.close(); } catch
	 * (SQLException e) {
	 * 
	 * } } if (connection != null) { try { connection.close(); } catch
	 * (SQLException e) {
	 * 
	 * } } } return object; }
	 */

	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	/*
	 * TODO: DB_URL, USERNAME and PASSWORD must be changed when we get the
	 * database server up and running, these variables were used for testing
	 * purposes only.
	 * 
	 * Perhaps make class usable for different users by including credentials in
	 * the constructor instead of making it a static final field.
	 */

	//local
	/*
	private static final String DB_URL = "jdbc:mysql://localhost:3306/fellesprosjekt";
	private static final String USERNAME = "fellesprosjekt";

	private static final String PASSWORD = "bringIt";
	*/
	
	//ekstern
	private static final String DB_URL = "jdbc:mysql://mysql.stud.ntnu.no/thomasdf_fellesprosjekt";
	private static final String USERNAME = "thomasdf_fellesp";

	private static final String PASSWORD = "bringIt";
	
	private String localTimetoDatabaseTime(LocalTime time){
		String dbTime = time.toString();
		return dbTime.substring(0,5) + ":00";
	}

	/**
	 * Fetches the activity with the given id from the database. Throws a
	 * SQLException if something goes wrong, then it returns an Activity object
	 * with no fields set but the id.
	 * 
	 * @param activity_id
	 *            the identity of the activity in question
	 * @return returns an Activity model object with the information required.
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
	 * 
	 * 
	 * @param owner_user_name
	 * @param activity_date
	 * @param end_date
	 * @param start_time
	 * @param end_time
	 * @param room_name
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
			statement.executeUpdate("INSERT INTO activity (calendar_id, description, activity_date, end_date, start_time, end_time, owner_user_name, room_name) VALUES ("
					+ calendar_id + ", '" + description + "', '" + activity_date.toString()
					+ "', '" + end_date.toString() + "', '" + localTimetoDatabaseTime(start_time) + "', '" + localTimetoDatabaseTime(end_time)
					+ "', '" + owner_user_name + "', '" + room_name + "')");
			
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
	
	
	/*

	/**
	 * Sets or updates the activity in question depending on if it exists
	 * already or not
	 * 
	 * @param activity
	 *            Activity object that should be updated
	 */
	
	/*
	public void setActivity(Activity activity) {
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
					.executeQuery("select * from activity where activity_id="
							+ activity.getActivity_id());
			result.next();
			if (result.getString(1) != null) {
				result.close();
				result = statement
						.executeQuery("SELECT calendar_id FROM hasCalendar WHERE user_name="
								+ activity.getActivity_owner());
				result.next();
				int calendar_id = result.getInt(1);
				statement.executeUpdate("UPDATE activity SET calendar_id="
						+ calendar_id + ", description="
						+ activity.getDescription() + ", activity_date="
						+ activity.getStart_date() + ", end_date="
						+ activity.getEnd_date() + ", start_time="
						+ activity.getFrom() + ", end_time=" + activity.getTo()
						+ ", owner_user_name=" + activity.getActivity_owner()
						+ ", room_name=" + activity.getRoom());
				/*
				 * if(activity.getRoom() != null) { result =
				 * statement.executeQuery
				 * ("SELECT room_name FROM room WHERE room_name=" +
				 * activity.getRoom()); result.next(); if(result.getString(1)!=
				 * null) {
				 * 
				 * } else { throw new SQLException(); } } else {
				 * statement.executeUpdate("UPDATE activity calendar_id="); }
				 */
	/*
			} else {
				result.close();
				result = statement
						.executeQuery("SELECT calendar_id FROM hasCalendar WHERE user_name="
								+ activity.getActivity_owner());
				result.next();
				int calendar_id = result.getInt(1);
				statement.executeUpdate("INSERT INTO activity VALUES ("
						+ activity.getDescription() + ", "
						+ activity.getStart_date() + ", "
						+ activity.getEnd_date() + ", " + activity.getFrom()
						+ ", " + activity.getTo() + ", "
						+ activity.getActivity_owner() + ", "
						+ activity.getRoom() + ")");
				/*
				 * // check if user name exists result = statement.executeQuery(
				 * "SELECT user_name FROM account WHERE user_name=" +
				 * activity.getOwner_user_name()); result.next();
				 * if(result.getString(1) != null) { result.close(); // check if
				 * user has calendar result = statement.executeQuery(
				 * "SELECT calendar_id FROM hasCalendar WHERE user_name=" +
				 * activity.getOwner_user_name()); result.next(); int
				 * calendar_id = result.getInt(1); if(result.getString(1)!=null)
				 * { result.close(); // check if room_name exists
				 * if(activity.getRoom() != null) { result =
				 * statement.executeQuery
				 * ("SELECT room_name FROM room WHERE room_name=" +
				 * activity.getRoom()); result.next(); if(result.getString(1) !=
				 * null) { result.close(); // insert the damn data
				 * statement.executeUpdate("INSERT INTO activity VALUES (" +
				 * calendar_id + ", " + activity.getDescription() + ", " +
				 * activity.getDate() + ", " + activity.getDate() + ", " +
				 * activity.getFrom() + ", " + activity.getTo() + ", " +
				 * activity.getOwner_user_name() + ", " + activity.getRoom()); }
				 * } } } else { throw new SQLException(); }
				 */
	/*
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
	
	 */

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
	 * Method to get the start and end time from the db of the given activity
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
	 * Fetches the id of the room that connects with the activity. Returns an
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
	 * @param start_date Localdate
	 * @param end_date Localdate
	 * @param start_time LocalTime
	 * @param end_time LocalTime
	 * @return Returns an arrayList of rooms available
	 */
	public ArrayList<Room> getAvailableRooms(LocalDate start_date, LocalDate end_date, LocalTime start_time, LocalTime end_time){
		return new ArrayList<Room>();
	}
	
	/**
	 * Returns a list of all the rooms
	 * @return Returns an arrayList of rooms.
	 */
	public ArrayList<Room> getAllRooms(){
		ArrayList<Room> roomlist = new ArrayList<>();

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
				Room room = new Room();
				room.setCapacity(result.getInt("capacity"));
				room.setRoom_name(result.getString("room_name"));
				roomlist.add(room);
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

	/*
	 * 
	 * /** Returns the employee-number registered in an account.
	 * 
	 * @param user_name the user name which is the primary key of the account we
	 * want to find the employee number from.
	 * 
	 * @return the employee-number for the account.
	 */

	/*
	 * public int getEmployeeNr(String user_name) { int employeeNr = 0; try {
	 * result = statement.executeQuery(
	 * "select person.employee_nr from person, account where account.employee_nr = person.employee_nr and account.user_name = "
	 * + "\"" + user_name + "\""); result.next(); employeeNr = result.getInt(1);
	 * } catch (SQLException e) {
	 * System.out.println("Error from DatabaseInterface: " +
	 * e.getLocalizedMessage()); } return employeeNr; }
	 */

	/**
	 * Returns the full name registered in an account.
	 * 
	 * @param user_name
	 *            the user name which is the primary key of the account we want
	 *            to find the full name from.
	 * @return the full name for the account.
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
	public String getPassword(String user_name) { // this method is not a smart
													// way to do things
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
	 * account with this user_name is not in the database
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
			}else{// account does not exist, and new row created
				statement.executeUpdate("INSERT INTO account VALUES ('"
						+ user_name + "', '" + account.getPassword() + "', '"
						+ account.getFirst_name() + "', '"
						+ account.getLast_name() + "', '"
						+ account.getMobile_nr() + "')");
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
	}
	
	/**
	 * Removes an account from the system. NB: only to use by admin user. Check if user_name exists in database before use.
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
	 * Returns an ArrayList of all Activity-objects related to an Account
	 * 
	 * @param user_name
	 *            the user name which is the primary key for the account
	 * @return an ArrayList<Activity> with all Activity-objects related to an
	 *         Account.
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

	/*
	 * 
	 * public Person getPerson(String user_name) { Person person = null; try {
	 * ResultSet result = statement .executeQuery(
	 * "select * from account, person where account.employee_nr = person.employee_nr and user_name = "
	 * + "\"" + user_name + "\""); result.next(); int employee_nr =
	 * result.getInt("employee_nr"); String first_name =
	 * result.getString("first_name"); String last_name =
	 * result.getString("last_name"); String mobile_nr =
	 * result.getString("mobile_nr"); String internal_nr = ""; // denne linjen
	 * skal fjernes n�r modellene // oppdateres til � ikke lenger ha //
	 * internal_nr person = new Person(employee_nr, first_name, last_name,
	 * internal_nr, mobile_nr); } catch (SQLException e) {
	 * System.out.println("Error from DatabaseInterface: " +
	 * e.getLocalizedMessage()); } return person; }
	 */

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
			result.next();
			invite = new Invite(result.getString("owner_user_name"),
					result.getString("user_name"), result.getInt("activity_id"));
			invite.setStatus(result.getString("invitation_status"));
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
	 * @param activity_id
	 * @param user_name
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
