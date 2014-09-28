package com.unacceptableuse.unacceptablebot.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class MySQLConnection
{
	private static final String dbClassName = "com.mysql.jdbc.Driver";

	private static final String CONNECTION = "jdbc:mysql://hp.matrixdevuk.pw:3306/";

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
		p.put("password", UnacceptableBot.getConfigHandler().getPassword("mysql"));

		c = DriverManager.getConnection(CONNECTION, p);

	}
	
	public boolean isConnected()
	{
		try
		{
			return c.isValid(10);
		} catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public String getConnectionHealth()
	{
		try
		{
			return 	"Connection: "+(c.isClosed() ? "&REDCLOSED&RESET" : "&GREENGOOD&RESET")+" Access: "+(c.isReadOnly() ? "&REDREAD-ONLY&RESET" : "&GREENREAD/WRITE&RESET"+" Validility: "+(c.isValid(10) ? "&GREENVALID&RESET" : "&REDTIMEOUT&RESET"));
		} catch (SQLException e)
		{
			e.printStackTrace();
			attemptReconnect();
			return "Very, very bad. bad stuff is going on here. oh LORD JESUS SOMEONE SAVE US";
		}
	}


	public String getSetting(String setting) {
		try {
			PreparedStatement ps = getPreparedStatement("SELECT Value FROM  teknogeek_settings.Global_Settings WHERE  `Setting` =  ? LIMIT 1");
			ps.setString(1, setting);
			ResultSet rs = ps.executeQuery();
			return rs.next() ? rs.getString(1) : null;
		} catch (SQLException e) {
			e.printStackTrace();
			attemptReconnect();
			return "Error " + e.getErrorCode() + " " + e.getLocalizedMessage();
		}
	}

	public boolean setSetting(String setting, String value) {
		try {
			if(!excecute("SELECT EXISTS(SELECT * FROM `teknogeek_settings`.`Global_Settings` WHERE Value =".concat(value).concat(";"))){
				return excecute("INSERT INTO `teknogeek_settings`.`Global_Settings` (`Setting`, `Value`) VALUES ('"+ setting + "', '" + value + "');"); //ON DUPLICATE KEY UPDATE Setting=VALUES(Setting);");
			} else {
				return excecute("UPDATE `teknogeek_settings`.`Global_Settings` SET Value='".concat(value).concat("' WHERE 'Setting' = ".concat(setting)));
			}
			
			//XXX: This seems inefficient. Poor database :(
		} catch (SQLException e) {
			e.printStackTrace();
			attemptReconnect();
			return false;
		}
	}
	
	public int getAccessLevel(String user) {
		try {
			ResultSet rs = query("SELECT * FROM  teknogeek_settings.Access_Levels WHERE  `Username` =  '"+ user + "' LIMIT 1");
			return rs.next() ? rs.getInt(2) : 0;
		} catch (SQLException e) {
			e.printStackTrace();
			attemptReconnect();
			return 0;
		}
	}

	public boolean setAccessLevel(String user, int level) {
		try {
			return excecute("INSERT INTO `teknogeek_settings`.`Access_Levels` (`Username`, `Level`) VALUES ('"+ user + "', '" + level + "') ON DUPLICATE KEY UPDATE Username=VALUES(Username);");
		} catch (SQLException e) {
			e.printStackTrace();
			attemptReconnect();
			return false;
		}
	}

	public boolean incrementValue(String setting, int amt) {
		try {
			return excecute("UPDATE `teknogeek_settings`.`Global_Settings` SET `Value` = `Value`+"+amt+" WHERE `Setting` = '"+setting+"';");
		} catch (SQLException e) {
			e.printStackTrace();
			attemptReconnect();
			return false;
		}
	}
	
	public boolean setChannels(String setting, String value) {
		try {
			return excecute("INSERT INTO `teknogeek_settings`.`Channels` (`Setting`, `Value`) VALUES ('"+ setting + "', '" + value + "') ON DUPLICATE KEY UPDATE Setting=VALUES(Setting);");
		} catch (SQLException e) {
			e.printStackTrace();
			attemptReconnect();
			return false;
		}
	}
	
	public String getChannels(String setting) {
		try {
			ResultSet rs = query("SELECT * FROM  `teknogeek_settings`.`Channels` WHERE  `Setting` =  '"+ setting + "' LIMIT 1");
			return rs.next() ? rs.getString(2) : null;
		} catch (SQLException e) {
			e.printStackTrace();
			attemptReconnect();
			return "Error " + e.getErrorCode() + " " + e.getLocalizedMessage();
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
	
	private void attemptReconnect() {
		if(!isConnected()){
			try {
				UnacceptableBot.log("SEVERE", "SQL", "SQL Connection lost. Reconnecting...");
				disconnect();
				connect();
			} catch (ClassNotFoundException | SQLException e) {
				UnacceptableBot.log("SEVERE", "SQL", "Unable to reconnect to SQL: "+e.toString());
				e.printStackTrace();
			}
		}
	}
}
