package utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import models.*;
/**
 * 
 * DatabaseInterface provides a connection to the database. This is based on the
 * user for select, update, but not for administrating options like alter and drop.
 * <p>
 * Remeber to always close the connection when done with the object.
 * <p>
 * To use this class you must first provide the jdbc connector projects build path.
 * It can be found at http://dev.mysql.com/downloads/connector/j/.
 * <p>
 * 
 * @author Daniel Lines
 * @version %I%, %G%
 * @since 1.0
 */
public class DatabaseInterface	{
	
	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	/*
	 * TODO: DB_URL, USERNAME and PASSWORD must be changed when we get the database
	 * server up and running, these variables were used for testing purposes only.
	 * 
	 * Perhaps make class usable for different users by including credentials in the constructor
	 * instead of making it a static final field.
	 */
	private static final String DB_URL = "jdbc:mysql://localhost:3306/fellesprosjekt";
	private static final String USERNAME = "thomas";
	private static final String PASSWORD = "bringIt";
	
	private Connection connection;
	private Statement statement;
	private ResultSet resultset;
	
	/**
	 * Class constructor.
	 */
	public DatabaseInterface()	{
		try	{
			Class.forName(DB_DRIVER);
			this.connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			this.statement = this.connection.createStatement();
		}	catch (Exception e)	{
			System.out.println("Error from DatabaseInterface: " + e.getLocalizedMessage());
		}
	}
	
	/**
	 * Fetches a ResultSet from the database based on the given query.
	 * 
	 * @param query SQL query string
	 * @return ResultSet result
	 */ 
	public ResultSet getQuery(String query)	{
		try	{
			this.resultset = this.statement.executeQuery(query);
		}	catch (SQLException e)	{
			System.out.println("Error from DatabaseInterface: " + e.getLocalizedMessage());
		}
		return this.resultset;
	}
	
	/**
	 * Fetches all the rows and columns from a given table.
	 * 
	 * @param table_name the table to fetch from the database
	 */
	public ResultSet getWholeTable(String table_name)	{
		try	{
			this.resultset = this.statement.executeQuery("SELECT * FROM " + table_name);
		}	catch (SQLException e)	{
			System.out.println("Error from DatabaseInterface: " + e.getLocalizedMessage());
		}
		return this.resultset;
	}
	
	/**
	 * Fetches everything from the given table which agress with the given where constraint.
	 * 
	 * @param table_name name of the table you want to fetch from
	 * @param where_constraint constraint of what you want to fetch from the given table, format : <table_column>=<Something>
	 */
	public ResultSet getTableWhere(String table_name, String where_constraint)	{
		try	{
			this.resultset = this.statement.executeQuery("SELECT * FROM " + table_name + " WHERE " + where_constraint);
		}	catch (SQLException e)	{
			System.out.println("Error from DatabaseInterface: " + e.getLocalizedMessage());
		}
		return this.resultset;
	}
	
	
	/**
	 * Closes all connection to the database.
	 */
	private void closeItAll()	{
		
		if(this.resultset!=null)	{
			try	{
				this.resultset.close();
			}	catch(SQLException e)	{
				
			}
		}
		if(this.statement!=null)	{
			try	{
				this.statement.close();
			}	catch(SQLException e)	{
				
			}
		}
		if(this.connection!=null)	{
			try	{
				this.connection.close();
			}	catch(SQLException e)	{
				
			}
		}
	}
	
	/**
	 * Returns the employee-number registered in an account.
	 * @param user_name the user name which is the primary key of the account we want to find the employee number from.
	 * @return the employee-number for the account.
	 */
	public int getEmployeeNr(String user_name) {
		int employeeNr = 0;
		try	{
			this.resultset = this.statement.executeQuery("select person.employee_nr from person, account where account.employee_nr = person.employee_nr and account.user_name = " +"\"" + user_name +"\"");
			resultset.next();
			employeeNr = this.resultset.getInt(1);
		}	catch (SQLException e)	{
			System.out.println("Error from DatabaseInterface: " + e.getLocalizedMessage());
		}
		return employeeNr;
	}
	
	/**
	 * Returns the full name registered in an account.
	 * @param user_name the user name which is the primary key of the account we want to find the full name from.
	 * @return the full name for the account.
	 */
	public String getFullName(String user_name){
		String fullName = null;
		try{
			this.resultset = this.statement.executeQuery("select person.first_name, person.last_name from person, account where account.employee_nr = person.employee_nr and account.user_name = " +"\"" + user_name + "\"");
			resultset.next();
			fullName = this.resultset.getString(1) + " " + this.resultset.getString(2);
		}	catch (SQLException e)	{
			System.out.println("Error from DatabaseInterface: " + e.getLocalizedMessage());
		}
		return fullName;
	}
	
	/**
	 * Returns a mobile number registered in an account in the database.
	 * @param user_name the user name which is the primary key of the account we want to find the mobile number from.
	 * @return the mobile number for the account
	 */
	public String getMobile(String user_name){
		String mobile = null;
		try{
			this.resultset = this.statement.executeQuery("select person.mobile_nr from person, account where account.employee_nr = person.employee_nr and account.user_name = " +"\"" + user_name + "\"");
			resultset.next();
			mobile = this.resultset.getString(1);
		}	catch (SQLException e)	{
			System.out.println("Error from DatabaseInterface: " + e.getLocalizedMessage());
		}
		return mobile;
	}
	
	/**
	 * Returns a password registered in an account in the database.
	 * @param user_name the user name which is the primary key of the account we want to find the password from.
	 * @return the password for the account.
	 */
	public String getPassword(String user_name){ //this method is not a smart way to do things
		String password = null;
		try{
			this.resultset = this.statement.executeQuery("select account.user_password from account where account.user_name = " +"\"" + user_name + "\"");
			resultset.next();
			password = this.resultset.getString(1);
		}	catch (SQLException e)	{
			System.out.println("Error from DatabaseInterface: " + e.getLocalizedMessage());
		}
		return password;
	}
	
	/**
	 * Returns an ID for the calendar related to an account
	 * @param user_name the user name which is the primary key of the account we want to find the calendarID from.
	 * @return the calendardId for the account.
	 */
	public int getCalendarId(String user_name) {
		int calendarId = 0;
		try	{
			this.resultset = this.statement.executeQuery("select calendar.calendar_id from account, calendar, hascalendar where hascalendar.user_name = account.user_name and hascalendar.calendar_id = calendar.calendar_id and hascalendar.user_name = " +"\"" + user_name +"\"");
			resultset.next();
			calendarId = this.resultset.getInt(1);
		}	catch (SQLException e)	{
			System.out.println("Error from DatabaseInterface: " + e.getLocalizedMessage());
		}
		return calendarId;
	}
	
	/**
	 * Returns the Account-object corresponding to a given user_name
	 * @param user_name the user name which is the primary key for the account which we want returned.
	 * @return the Account-object for the user_name.
	 */
	public Account getAccount(String user_name){
		Account acc;
		int employee_nr = getEmployeeNr(user_name);
		String password = getPassword(user_name);
		acc = new Account(user_name, employee_nr, password);
		return acc;
	}
	
	/**
	 * Returns an ArrayList of all Activity-objects related to an Account
	 * @param user_name the user name which is the primary key for the account
	 * @return an ArrayList<Activity> with all Activity-objects related to an Account.
	 */
	public ArrayList<Activity> getAllActivities(String user_name) {
		ArrayList<Activity> activityList = new ArrayList<Activity>();
		try {
			ResultSet result = this.statement.executeQuery("select activity.activity_id, activity.description, activity.start_time, activity.end_time, activity.activity_date, activity.end_date, activity.owner_user_name, activity.room_name from activity, account, calendar, hascalendar where activity.calendar_id = calendar.calendar_id and hascalendar.user_name = account.user_name and hascalendar.calendar_id = calendar.calendar_id and hascalendar.user_name = " + "\"" + user_name + "\"");
			while(result.next()){
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
	
	public Person getPerson(String user_name){
		Person person = null;
		try{
			ResultSet result = this.statement.executeQuery("select * from account, person where account.employee_nr = person.employee_nr and user_name = " +"\"" + user_name + "\"");
			resultset.next();
			int employee_nr = result.getInt("employee_nr");
			String first_name = result.getString("first_name");
			String last_name = result.getString("last_name");
			String mobile_nr = result.getString("mobile_nr");
			String internal_nr = ""; //denne linjen skal fjernes når modellene oppdateres til å ikke lenger ha internal_nr
			person = new Person(employee_nr, first_name, last_name, internal_nr, mobile_nr);
		}	catch (SQLException e)	{
			System.out.println("Error from DatabaseInterface: " + e.getLocalizedMessage());
		}
		return person;
	}
}
