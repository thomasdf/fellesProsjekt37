package utils;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import models.Activity;
import models.Group;

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
	private static final String USERNAME = "daniel";
	private static final String PASSWORD = "bringIt1";
	
	private Connection connection;
	private Statement statement;
	private ResultSet result;
	
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
			this.connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			this.statement = connection.createStatement();
		    this.result = statement.executeQuery(query);
		}	catch (SQLException e)	{
			System.out.println("Error from DatabaseInterface: " + e.getLocalizedMessage());
		}
		return this.result;
	}
	
	
	/**
	 * Closes all connection to the database.
	 */
	public void closeItAll()	{
		
		if(this.result!=null)	{
			try	{
				this.result.close();
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
	 * Fetches the activity with the given id from the database. Throws
	 * a SQLException if something goes wrong, then it returns an
	 * Activity object with no fields set but the id.
	 * 
	 * @param id the identity of the activity in question
	 * @return returns an Activity model object with the information required.
	 */
	public Activity getActivity(int id)	{
		Activity act = new Activity(id);
		try	{
			ResultSet result = this.getQuery("select * from activity where activity.activity_id=" + id);		
			act.setDescription(result.getString("description"));
			act.setFrom(result.getTime("start_time").toLocalTime());
			act.setTo(result.getTime("end_time").toLocalTime());
			act.setDate(result.getDate("date").toLocalDate());
			act.setDescription(result.getString("description"));
			// participants og room kommer etter dette
		}	catch(SQLException e)	{
			e.printStackTrace();
		}
		return act;
		
	}
	
	
	/**
	 * Returns the description of the activity defined by the id.
	 * 
	 * @param id the identity of the activity in question
	 * @return String variable which contains the description
	 */
	public String getActivityDesc(int id)	{
		String s = "";
		try	{
			ResultSet result = this.getQuery("select description from activity where activity.activity_id=" + id);
			
			if(result.next())	{
				s = result.getString("description");
			}
		}	catch (SQLException e)	{
			e.printStackTrace();
		}
		return s;
	}
	
	/**
	 * Method to get the start and end time from the db of the
	 * given activity
	 * 
	 * @param id the identity of the activity in question
	 * @return ArrayList<LocalTime> with start_time and end_time
	 */
	public ArrayList<LocalTime>	getFromTo(int id)	{
		ArrayList<LocalTime> time = new ArrayList<LocalTime>();
		try	{
			ResultSet result = this.getQuery("select start_time, end_time from activity where activity.activity_id="+id);
			if(result.next())	{	
				time.add(result.getTime("start_time").toLocalTime());
				time.add(result.getTime("end_time").toLocalTime());
			}
		}	catch(SQLException e)	{
			e.printStackTrace();
		}
		return time;
	}
	
	/**
	 * Fetched the calendar id of the given activity from the database.
	 * Returns 0 if something goes wrong
	 * 
	 * @param id the identity of the activity in question
	 * @return int calendar_id
	 */
	public int getCalendarId(int id)	{
		int cal_id = 0;
		
		try	{
			ResultSet result = this.getQuery("select calendar_id from activity where activity_id=" + id);
			if(result.next())	{
				cal_id = result.getInt("calendar_id");
			}
		}	catch(SQLException e)	{
			e.printStackTrace();
		}
		return cal_id;
	}
	
	/**
	 * Fetches the id of the room that connects with the activity.
	 * Returns an empty string if something goes wrong
	 * 
	 * @param id the identity of the activity in question
	 * @return returns a String that is the primary key of the room 
	 */
	public String getRoomName(int id)	{
		String room_name = "";
		
		try	{
			ResultSet result = this.getQuery("select room.* from activity, room where activity.room_name= room.room_name and activity_id="+id);
			if(result.next())	{
				room_name = result.getString("room_name");
			}
		}	catch(SQLException e)	{
			e.printStackTrace();
		}
		return room_name;
	}
	
	/**
	 * Fetches the group with this id from the database.
	 * 
	 * @param id the identity of the group in question
	 * @return returns a group object
	 */
	public Group getGroup(int id)	{
		Group group = null;
		ArrayList<String> member = new ArrayList<String>();
		try	{
			ResultSet result = this.getQuery("select * from calendargroup where calendargroup.group_id="+id);
			if(result.next())	{
				group = new Group(id, result.getString("name"));
			}
			ResultSet members = this.getQuery("select account.user_name from account, calendargroup where account.group_id_id=calendargroup.group_id and calendargroup.group_id="+id);
			int count = 0;
			while(members.next())	{
				member.add(members.getString(count));
				count++;
			}
			group.setMembers(member);;
		}	catch(SQLException e)	{
			e.printStackTrace();
		}
		return group;
	}
	
	/**
	 * Fetches the user_name of the admin connected to the given group.
	 * 
	 * @param id the id for the group in question
	 * @return returns a user name which is unique for that account
	 */
	public String getGroupAdmin(int id)	{
		String admin_user_name = null;
		
		try	{
			ResultSet result = this.getQuery("select account.user_name from calendargroup, account, ismember where account.user_name=ismember.username and ismember.group_id=calendargroup.group_id and calendargroup.group_id=" + id + " and ismember.role='admin'");
			if(result.next())	{
				admin_user_name = result.getString("user_name");
			}
		}	catch (SQLException e)	{
			e.printStackTrace();
		}
		return admin_user_name;
	}
	
}
