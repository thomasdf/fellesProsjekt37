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
	private static final String USERNAME = "daniel";
	private static final String PASSWORD = "bringIt1";
	
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
	
}
