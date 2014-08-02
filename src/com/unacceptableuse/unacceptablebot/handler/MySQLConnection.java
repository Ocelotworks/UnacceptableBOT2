package com.unacceptableuse.unacceptablebot.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class MySQLConnection
{
	private static final String dbClassName = "com.mysql.jdbc.Driver";

	private static final String CONNECTION = "jdbc:mysql://127.0.0.1/UnacceptableBot";

	protected Connection c;

	public void connect() throws SQLException, ClassNotFoundException
	{
		Class.forName(dbClassName);

	    Properties p = new Properties();
	    p.put("user", "root");
	    p.put("password", "1907");

	    c = DriverManager.getConnection(CONNECTION, p);
	}

	public String getSetting(String setting)
	{
		try
		{
			ResultSet rs = query("SELECT * FROM  Settings.Global_settings WHERE  `Setting` =  '" + setting + "' LIMIT 1");
			return rs.next() ? rs.getString(2) : null;
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