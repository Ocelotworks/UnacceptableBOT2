package com.unacceptableuse.unacceptablebot.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class MySQLConnection
{
	private static final String dbClassName = "com.mysql.jdbc.Driver";

	private static final String CONNECTION = "jdbc:mysql://localhost";

	protected Connection c;

	public void connect() throws SQLException, ClassNotFoundException
	{
		Class.forName(dbClassName);

	    Properties p = new Properties();
	    p.put("user", "root");
	    p.put("password", "");

	    c = DriverManager.getConnection(CONNECTION, p);
	}

	public String getSetting()
	{
		try
		{
			ResultSet rs = query("SELECT data FROM UnacceptableBot.botTest");
			System.out.println(rs);
			return rs.next() ? rs.getString(1) : null;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return "Error " + e.getErrorCode() + " " + e.getLocalizedMessage();
		}
	}

	public ResultSet query(String sql) throws SQLException
	{
		return c.prepareStatement(sql).executeQuery();
	}

	public boolean excecute(String sql) throws SQLException
	{
		return c.prepareStatement(sql).execute();
	}

	public void disconnect() throws SQLException
	{
		 c.close();
	}
}