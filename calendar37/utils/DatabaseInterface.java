package utils;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import models.Account;
import models.Activity;
import models.Group;
import models.Person;

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
 * @author Daniel Lines
 * @version %I%, %G%
 * @since 1.0
 */
public class DatabaseInterface {

	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	/*
	 * TODO: DB_URL, USERNAME and PASSWORD must be changed when we get the
	 * database server up and running, these variables were used for testing
	 * purposes only.
	 * 
	 * Perhaps make class usable for different users by including credentials in
	 * the constructor instead of making it a static final field.
	 */
	private static final String DB_URL = "jdbc:mysql://localhost:3306/fellesprosjekt";
	private static final String USERNAME = "daniel";
	private static final String PASSWORD = "bringIt1";

	private Connection connection;
	private Statement statement;
	private ResultSet result;

	/**
	 * Class constructor.
	 */
	public DatabaseInterface() {
		try {
			Class.forName(DB_DRIVER);
			this.connection = DriverManager.getConnection(DB_URL, USERNAME,
					PASSWORD);
			this.statement = this.connection.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Fetches a ResultSet from the database based on the given query.
	 * 
	 * @param query
	 *            SQL query string
	 * @return ResultSet result
	 */
	public ResultSet getQuery(String query) {
		try {
			this.connection = DriverManager.getConnection(DB_URL, USERNAME,
					PASSWORD);
			this.statement = connection.createStatement();
			this.result = statement.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this.result;
	}

	/**
	 * Closes all connection to the database.
	 */
	public void closeItAll() {

		if (this.result != null) {
			try {
				this.result.close();
			} catch (SQLException e) {

			}
		}
		if (this.statement != null) {
			try {
				this.statement.close();
			} catch (SQLException e) {

			}
		}
		if (this.connection != null) {
			try {
				this.connection.close();
			} catch (SQLException e) {

			}
		}
	}

	/**
	 * Fetches the activity with the given id from the database. Throws a
	 * SQLException if something goes wrong, then it returns an Activity object
	 * with no fields set but the id.
	 * 
	 * @param id
	 *            the identity of the activity in question
	 * @return returns an Activity model object with the information required.
	 */
	public Activity getActivity(int id) {
		Activity act = new Activity(id);
		ArrayList<Integer> part = new ArrayList<Integer>();
		try {
			ResultSet result = this
					.getQuery("select * from activity where activity.activity_id="
							+ id);
			if (result.next()) {
				act.setDescription(result.getString("description"));
				act.setFrom(result.getTime("start_time").toLocalTime());
				act.setTo(result.getTime("end_time").toLocalTime());
				act.setDate(result.getDate("activity_date").toLocalDate());
				act.setDescription(result.getString("description"));
			}
			result.close();
			result = this
					.getQuery("select user_name from invited where activity_id="
							+ id + " and invitation_status='true'");
			ArrayList<String> user_names = new ArrayList<String>();
			int count = 1;
			if (result.next()) {
				while (result.next()) {
					user_names.add(result.getString(count));
				}
				result.close();
				for (String x : user_names) {
					result = this
							.getQuery("select employee_nr from account where user_name='"
									+ x + "'");
					if (result.next()) {
						part.add(result.getInt(1));
					}
					result.close();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		act.setParticipants(part);
		return act;

	}

	/**
	 * Sets or updates the activity in question depending on if it exists
	 * already or not
	 * 
	 * @param activity
	 *            Activity object that should be updated
	 */
	public void setActivity(Activity activity) {

		try {
			ResultSet result = this
					.getQuery("select * from activity where activity_id="
							+ activity.getActivity_id());
			result.next();
			if (result.getString(1) != null) {
				result.close();
				this.statement.execute("UPDATE activity SET calendar_id="
						+ activity.getCalendar_id() + ", description="
						+ activity.getDescription() + ", activity_date="
						+ activity.getDate() + ", end_date="
						+ activity.getDate() + ", start_time="
						+ activity.getFrom() + ", end_time=" + activity.getTo()
						+ ", owner_user_name=" + activity.getOwner_user_name()
						+ ", room_name=" + activity.getRoom());
				/*
				 * if(activity.getRoom() != null) { result =
				 * this.getQuery("SELECT room_name FROM room WHERE room_name=" +
				 * activity.getRoom()); result.next(); if(result.getString(1)!=
				 * null) {
				 * 
				 * } else { throw new SQLException(); } } else {
				 * this.statement.execute("UPDATE activity calendar_id="); }
				 */
			} else {
				result.close();
				this.statement.execute("INSERT INTO activity VALUES ("
						+ calendar_id + ", " + activity.getDescription() + ", "
						+ activity.getDate() + ", " + activity.getDate() + ", "
						+ activity.getFrom() + ", " + activity.getTo() + ", "
						+ activity.getOwner_user_name() + ", "
						+ activity.getRoom());
				/*
				 * // check if user name exists result =
				 * this.getQuery("SELECT user_name FROM account WHERE user_name="
				 * + activity.getOwner_user_name()); result.next();
				 * if(result.getString(1) != null) { result.close(); // check if
				 * user has calendar result = this.getQuery(
				 * "SELECT calendar_id FROM hasCalendar WHERE user_name=" +
				 * activity.getOwner_user_name()); result.next(); int
				 * calendar_id = result.getInt(1); if(result.getString(1)!=null)
				 * { result.close(); // check if room_name exists
				 * if(activity.getRoom() != null) { result =
				 * this.getQuery("SELECT room_name FROM room WHERE room_name=" +
				 * activity.getRoom()); result.next(); if(result.getString(1) !=
				 * null) { result.close(); // insert the damn data
				 * this.statement.execute("INSERT INTO activity VALUES (" +
				 * calendar_id + ", " + activity.getDescription() + ", " +
				 * activity.getDate() + ", " + activity.getDate() + ", " +
				 * activity.getFrom() + ", " + activity.getTo() + ", " +
				 * activity.getOwner_user_name() + ", " + activity.getRoom()); }
				 * } } } else { throw new SQLException(); }
				 */
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the description of the activity defined by the id.
	 * 
	 * @param id
	 *            the identity of the activity in question
	 * @return String variable which contains the description
	 */
	public String getActivityDesc(int id) {
		String s = "";
		try {
			ResultSet result = this
					.getQuery("select description from activity where activity.activity_id="
							+ id);

			if (result.next()) {
				s = result.getString("description");
			}
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * Method to get the start and end time from the db of the given activity
	 * 
	 * @param id
	 *            the identity of the activity in question
	 * @return ArrayList<LocalTime> with start_time and end_time
	 */
	public ArrayList<LocalTime> getFromTo(int id) {
		ArrayList<LocalTime> time = new ArrayList<LocalTime>();
		try {
			ResultSet result = this
					.getQuery("select start_time, end_time from activity where activity.activity_id="
							+ id);
			if (result.next()) {
				time.add(result.getTime("start_time").toLocalTime());
				time.add(result.getTime("end_time").toLocalTime());
			}
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
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
	public int getCalendarId(int id) {
		int cal_id = 0;

		try {
			ResultSet result = this
					.getQuery("select calendar_id from activity where activity_id="
							+ id);
			if (result.next()) {
				cal_id = result.getInt("calendar_id");
			}
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cal_id;
	}

	/**
	 * Fetches the id of the room that connects with the activity. Returns an
	 * empty string if something goes wrong
	 * 
	 * @param id
	 *            the identity of the activity in question
	 * @return returns a String that is the primary key of the room
	 */
	public String getRoomName(int id) {
		String room_name = "";

		try {
			ResultSet result = this
					.getQuery("select room.* from activity, room where activity.room_name= room.room_name and activity_id="
							+ id);
			if (result.next()) {
				room_name = result.getString("room_name");
			}
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return room_name;
	}

	/**
	 * Fetches the group with this id from the database.
	 * 
	 * @param id
	 *            the identity of the group in question
	 * @return returns a group object
	 */
	public Group getGroup(int id) {
		Group group = null;
		ArrayList<String> member = new ArrayList<String>();
		ArrayList<Integer> sub_groups = new ArrayList<Integer>();
		try {
			ResultSet result = this
					.getQuery("select * from calendarGroup where calendarGroup.group_id="
							+ id);
			if (result.next()) {
				group = new Group(id, result.getString("group_name"));
			}
			result.close();
			ResultSet members = this
					.getQuery("select account.user_name from account, isMember where account.user_name=isMember.user_name and isMember.group_id="
							+ id);
			int count = 1;
			while (members.next()) {
				member.add(members.getString(1));
				count++;
			}
			group.setMembers(member);
			members.close();
			ResultSet sub_group = this
					.getQuery("select subGroup.subgroup_id from subGroup where supergroup_id="
							+ id);
			while (sub_group.next()) {
				sub_groups.add(sub_group.getInt(1));
			}
			group.setSubgroups(sub_groups);
			sub_group.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return group;
	}

	/**
	 * Fetches the user_name of the admin connected to the given group.
	 * 
	 * @param id
	 *            the id for the group in question
	 * @return returns a user name which is unique for that account
	 */
	public String getGroupAdmin(int id) {
		String admin_user_name = null;

		try {
			ResultSet result = this
					.getQuery("select account.user_name from calendargroup, account, ismember where account.user_name=ismember.username and ismember.group_id=calendargroup.group_id and calendargroup.group_id="
							+ id + " and ismember.role='admin'");
			if (result.next()) {
				admin_user_name = result.getString("user_name");
			}
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return admin_user_name;
	}

	/**
	 * Returns the employee-number registered in an account.
	 * 
	 * @param user_name
	 *            the user name which is the primary key of the account we want
	 *            to find the employee number from.
	 * @return the employee-number for the account.
	 */
	public int getEmployeeNr(String user_name) {
		int employeeNr = 0;
		try {
			this.result = this.statement
					.executeQuery("select person.employee_nr from person, account where account.employee_nr = person.employee_nr and account.user_name = "
							+ "\"" + user_name + "\"");
			result.next();
			employeeNr = this.result.getInt(1);
		} catch (SQLException e) {
			System.out.println("Error from DatabaseInterface: "
					+ e.getLocalizedMessage());
		}
		return employeeNr;
	}

	/**
	 * Returns the full name registered in an account.
	 * 
	 * @param user_name
	 *            the user name which is the primary key of the account we want
	 *            to find the full name from.
	 * @return the full name for the account.
	 */
	public String getFullName(String user_name) {
		String fullName = null;
		try {
			this.result = this.statement
					.executeQuery("select person.first_name, person.last_name from person, account where account.employee_nr = person.employee_nr and account.user_name = "
							+ "\"" + user_name + "\"");
			result.next();
			fullName = this.result.getString(1) + " "
					+ this.result.getString(2);
		} catch (SQLException e) {
			System.out.println("Error from DatabaseInterface: "
					+ e.getLocalizedMessage());
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
		try {
			this.result = this.statement
					.executeQuery("select person.mobile_nr from person, account where account.employee_nr = person.employee_nr and account.user_name = "
							+ "\"" + user_name + "\"");
			result.next();
			mobile = this.result.getString(1);
		} catch (SQLException e) {
			System.out.println("Error from DatabaseInterface: "
					+ e.getLocalizedMessage());
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
		try {
			this.result = this.statement
					.executeQuery("select account.user_password from account where account.user_name = "
							+ "\"" + user_name + "\"");
			result.next();
			password = this.result.getString(1);
		} catch (SQLException e) {
			System.out.println("Error from DatabaseInterface: "
					+ e.getLocalizedMessage());
		}
		return password;
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
		try {
			this.result = this.statement
					.executeQuery("select calendar.calendar_id from account, calendar, hascalendar where hascalendar.user_name = account.user_name and hascalendar.calendar_id = calendar.calendar_id and hascalendar.user_name = "
							+ "\"" + user_name + "\"");
			result.next();
			calendarId = this.result.getInt(1);
		} catch (SQLException e) {
			System.out.println("Error from DatabaseInterface: "
					+ e.getLocalizedMessage());
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
		int employee_nr = getEmployeeNr(user_name);
		String password = getPassword(user_name);
		acc = new Account(user_name, employee_nr, password);
		return acc;
	}

	/**
	 * Updates an account and creates a new account in the database if an
	 * account with this user_name is not in the database
	 * 
	 * @param account
	 *            the account we want to add to the database
	 */
	public void setAccount(Account account) {
		String user_name = account.getUsername();
		String password = account.getPassword();
		int employee_nr = account.getAccount_owner();
		try {
			ResultSet result = statement
					.executeQuery("select account.user_name from account where account.user_name = \""
							+ user_name + "\"");
			result.next();
			if (result.getString(1) != null) {// check if account with same id
												// already exists
				statement.executeUpdate("update account set employee_nr="
						+ employee_nr + ", user_password= " + "\"" + password
						+ "\"" + " where user_name = " + "\"" + user_name
						+ "\"");
			} else {// account does not exist, and new row created
				statement.executeUpdate("insert into account values ( \""
						+ user_name + "\", " + "\"" + password + "\", "
						+ employee_nr + ")");
			}
		} catch (SQLException e) {
			System.out.println("Error from DatabaseInterface: "
					+ e.getLocalizedMessage());
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
		try {
			ResultSet result = this.statement
					.executeQuery("select activity.activity_id, activity.description, activity.start_time, activity.end_time, activity.activity_date, activity.end_date, activity.owner_user_name, activity.room_name from activity, account, calendar, hascalendar where activity.calendar_id = calendar.calendar_id and hascalendar.user_name = account.user_name and hascalendar.calendar_id = calendar.calendar_id and hascalendar.user_name = "
							+ "\"" + user_name + "\"");
			while (result.next()) {
				Activity act;
				act = new Activity(result.getInt("activity_id"));
				act.setDescription(result.getString("description"));
				act.setFrom(result.getTime("start_time").toLocalTime());
				act.setTo(result.getTime("end_time").toLocalTime());
				act.setDate(result.getDate("date").toLocalDate());
				act.setDescription(result.getString("description"));
				// participants og room kommer etter dette

				activityList.add(act);
			}
		} catch (SQLException e) {
			System.out.println("Error from DatabaseInterface: "
					+ e.getLocalizedMessage());
		}
		return activityList;
	}

	public Person getPerson(String user_name) {
		Person person = null;
		try {
			ResultSet result = this.statement
					.executeQuery("select * from account, person where account.employee_nr = person.employee_nr and user_name = "
							+ "\"" + user_name + "\"");
			result.next();
			int employee_nr = result.getInt("employee_nr");
			String first_name = result.getString("first_name");
			String last_name = result.getString("last_name");
			String mobile_nr = result.getString("mobile_nr");
			String internal_nr = ""; // denne linjen skal fjernes når modellene
										// oppdateres til å ikke lenger ha
										// internal_nr
			person = new Person(employee_nr, first_name, last_name,
					internal_nr, mobile_nr);
		} catch (SQLException e) {
			System.out.println("Error from DatabaseInterface: "
					+ e.getLocalizedMessage());
		}
		return person;
	}

	public void setSubGroup(Group supergroup, Group subgroup) {

	}

}
