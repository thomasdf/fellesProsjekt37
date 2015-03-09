package utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
	 * Returns an ID for the calendar related to a group
	 * @param groupId the groupID which is the primary key for the group we want to find the calendar from.
	 * @return a calendarId for the groupCalendar.
	 */
	public int getCalendarId(int groupId) {
		int calendarId = 0;
		try	{
			this.resultset = this.statement.executeQuery("select calendar.calendar_id from calendargroup, calendar, grouphascalendar where calendar.calendar_id = grouphascalendar.calendar_id and calendargroup.group_id = grouphascalendar.group_id and calendarGroup.group_id = " + groupId);
			resultset.next();
			calendarId = this.resultset.getInt(1);
		}	catch (SQLException e)	{
			System.out.println("Error from DatabaseInterface: " + e.getLocalizedMessage());
		}
		return calendarId;
	}
	
}
