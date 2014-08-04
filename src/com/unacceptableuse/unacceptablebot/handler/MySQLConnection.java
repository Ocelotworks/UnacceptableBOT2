package com.unacceptableuse.unacceptablebot.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySQLConnection
{
	public void test()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		
		Connection con = null;
		Statement statement = null;
		ResultSet result = null;
		
		String url = "jdbc:mysql://127.0.0.1";
		String user = "root";
		String password = "";
		
		
		try
		{
            con = DriverManager.getConnection(url, user, password);
            statement = con.createStatement();
            result = statement.executeQuery("SELECT VERSION()");

            if(result.next())
            {
                System.out.println(result.getString(1));
            }

        }
		catch(SQLException ex)
		{
            Logger lgr = Logger.getLogger(MySQLConnection.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } 
		finally
		{
            try
            {
                if(result != null)
                {
                    result.close();
                }
                
                if(statement != null)
                {
                	statement.close();
                }
                
                if(con != null)
                {
                    con.close();
                }

            }
            catch (SQLException ex)
            {
                Logger lgr = Logger.getLogger(MySQLConnection.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
	}
}