package utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseInterface	{
	
	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	/*
	 * TODO: DB_URL, USERNAME and PASSWORD must be changed when we get the database
	 * server up and running, these variables were used for testing purposes only.
	 */
	private static final String DB_URL = "jdbc:mysql://localhost:3306/fellesprosjekt";
	private static final String USERNAME = "daniel";
	private static final String PASSWORD = "bringIt1";
	
	private Connection connection;
	private Statement statement;
	private ResultSet resultset;
	
	public DatabaseInterface()	{
		try	{
			Class.forName(DB_DRIVER);
			this.connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			this.statement = this.connection.createStatement();
		}	catch (Exception e)	{
			System.out.println("Error from DatabaseInterface: " + e.getLocalizedMessage());
		}
	}
	
	public ResultSet getQuery(String query)	{
		try	{
			this.resultset = this.statement.executeQuery(query);
		}	catch (SQLException e)	{
			System.out.println("Error from DatabaseInterface: " + e.getLocalizedMessage());
		}
		return this.resultset;
	}
	
	public ResultSet getWholeTable(String table_name)	{
		try	{
			this.resultset = this.statement.executeQuery("SELECT * FROM " + table_name);
		}	catch (SQLException e)	{
			System.out.println("Error from DatabaseInterface: " + e.getLocalizedMessage());
		}
		return this.resultset;
	}
	
	public ResultSet getTableWhere(String table_name, String where_constraint)	{
		try	{
			this.resultset = this.statement.executeQuery("SELECT * FROM " + table_name + " WHERE " + where_constraint);
		}	catch (SQLException e)	{
			System.out.println("Error from DatabaseInterface: " + e.getLocalizedMessage());
		}
		return this.resultset;
	}
	
	
	/*
	 * Closes all the connections, always call this method after you are done with the result set
	 */
	public void closeItAll()	{
		
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
