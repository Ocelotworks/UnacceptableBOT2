package com.unacceptableuse.unacceptablebot.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class MySQLConnection {
	private static final String dbClassName = "com.mysql.jdbc.Driver";

	private static final String CONNECTION = "jdbc:mysql://hp.matrixdevuk.pw/";

	protected Connection c;

	/**
	 * Connects to the MySQL server. Password is stored in static.properties
	 * @author UnacceptableUse, teknogeek
	 * @throws SQLException Thrown if the username/password is rejected.
	 * @throws ClassNotFoundException Thrown if the JDBC driver is not correctly installed.
	 */
	public void connect() throws SQLException, ClassNotFoundException {
		Class.forName(dbClassName);

		Properties p = new Properties();
		p.put("user", "unacceptablebot");
		p.put("password",UnacceptableBot.getConfigHandler().getPassword("mysql"));

		c = DriverManager.getConnection(CONNECTION, p);

	}

	public String getSetting(String setting) {
		try {
			ResultSet rs = query("SELECT * FROM  teknogeek_settings.Global_Settings WHERE  `Setting` =  '"+ setting + "' LIMIT 1");
			return rs.next() ? rs.getString(2) : null;
		} catch (SQLException e) {
			e.printStackTrace();
			return "Error " + e.getErrorCode() + " " + e.getLocalizedMessage();
		}
	}

	public boolean setSetting(String setting, String value) {
		try {
			return excecute("INSERT INTO `teknogeek_settings`.`Global_Settings` (`Setting`, `Value`) VALUES ('"+ setting + "', '" + value + "') ON DUPLICATE KEY UPDATE Setting=VALUES(Setting);");
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public int getAccessLevel(String user) {
		try {
			ResultSet rs = query("SELECT * FROM  teknogeek_settings.Access_Levels WHERE  `Username` =  '"+ user + "' LIMIT 1");
			return rs.next() ? rs.getInt(2) : 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public boolean setAccessLevel(String user, int level) {
		try {
			return excecute("INSERT INTO `teknogeek_settings`.`Access_Levels` (`Username`, `Level`) VALUES ('"+ user + "', '" + level + "') ON DUPLICATE KEY UPDATE Username=VALUES(Username);");
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean incrementValue(String setting, int amt) {
		try {
			return excecute("UPDATE `teknogeek_settings`.`Access_Levels` SET `Value` = `Value`+"+amt+" WHERE `Setting` = '"+setting+"';");
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public PreparedStatement getPreparedStatement(String sql) throws SQLException
	{
		return c.prepareStatement(sql);
	}

	public ResultSet query(String sql) throws SQLException {
		return c.prepareStatement(sql).executeQuery();
	}

	public boolean excecute(String sql) throws SQLException {
		return c.prepareStatement(sql).execute();
	}

	public void disconnect() throws SQLException {
		c.close();
	}
}
