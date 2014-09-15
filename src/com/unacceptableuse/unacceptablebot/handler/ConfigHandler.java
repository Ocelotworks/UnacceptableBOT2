package com.unacceptableuse.unacceptablebot.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.pircbotx.User;

public class ConfigHandler {

	/*
	 * THIS IS WHERE THE BOT WILL CONTACT THE DATABASE, ANY CONFIGURATION WILL
	 * BE DRAWN FROM HERE
	 */

	public MySQLConnection sql = null;
	private Properties staticVars = null;

	/**
	 * Initializes the configurations, {@link MySQLConnection} and {@link Properties}
	 */
	public void init() {
		staticVars = new Properties();
		try {
			staticVars.load(new FileInputStream(new File("static.properties")));
			System.out.println(staticVars.getProperty("mysql"));
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		sql = new MySQLConnection();
		try {
			sql.connect();
		} catch (ClassNotFoundException e1) {

			e1.printStackTrace();
		} catch (SQLException e1) {

			e1.printStackTrace();
		}
	}
	
	public boolean isConnected()
	{
		return sql.isConnected();
	}

	public boolean isCommandDisabled(String command, String channel) {
		return getBoolean("cd:" + command + ":" + channel);
	}

	public int getUserLevel(User user) {
		return user.isIrcop() ? 10 : user.isVerified() ? sql.getAccessLevel(user.getNick()) : 0;
	}

	public void setUserLevel(User user, int level) {
		if(user.isVerified())
			sql.setAccessLevel(user.getNick(), level);
	}

	public String getPassword(String key) {
		return staticVars.getProperty(key);
	}

	public boolean getBoolean(String key) {
		String result = sql.getSetting(key);
		return result == null ? false : Boolean.parseBoolean(result);
	}

	public Integer getInteger(String key) {
		try{
			return Integer.parseInt(sql.getSetting(key));
		}catch(NumberFormatException e)
		{
			return 0;
		}
	}

	public long getLong(String key) {
		return Long.parseLong(sql.getSetting(key));
	}

	public String getString(String key) {
		return sql.getSetting(key);
	}

	public void setLong(String key, long val) {
		sql.setSetting(key, String.valueOf(val));
	}
	
	public void setFloat(String key, float val){
		sql.setSetting(key, String.valueOf(val));
	}
	
	public float getFloat(String key){
		return Float.parseFloat(sql.getSetting(key));
	}

	public void setBoolean(String key, boolean bool) {
		sql.setSetting(key, String.valueOf(bool));
	}

	public void setInteger(String key, Integer integer) {
		sql.setSetting(key, String.valueOf(integer));
	}

	public void setString(String key, String string) {
		sql.setSetting(key, string);
	}

	public void increment(String key, int amt) {
		sql.incrementValue(key, amt);
	}

	public void increment(String key) {
		increment(key, 1);
	}

	/**
	 * @author Neil - dvd604
	 * @param time
	 *            - the time the message was sent
	 * @param user
	 *            - the user who sent the message
	 * @param message
	 *            - the string sent by the user
	 * @param channel
	 *            - the channel that the user sent the message to
	 * **/
	public boolean setLog(String time, String user, String message,
			String channel) {
		try {
			PreparedStatement ps = sql.getPreparedStatement("INSERT INTO `teknogeek_unacceptablebot`.`"+channel+"` (`Time`, `Username`, `Message`) VALUES (?, ?, ?)");
			ps.setString(1, time);
			ps.setString(2, user);
			ps.setString(3, message);
			return ps.executeUpdate() == 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @author Neil - dvd604
	 * @param channel
	 *            - The channel to return the log for
	 * @param log
	 *            - Int, the row of the table to return. See method body for
	 *            help
	 * **/
	public ResultSet getLog(String channel, int log) {
		try {
			// This gets the logth record. It requires some black magic before
			// handing it a number
			// Similar to the stuff I did in the first fillmein.
			ResultSet rs = sql.query("SELECT Time,Username,Message FROM `teknogeek_unacceptablebot`.`" + channel
					+ "` ORDER BY ID LIMIT "+log+",1");
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();

		}
		return null;
	}
	
	public void setChannels(String chans){
		sql.setChannels("Channel_list", chans);
	}
	
	public String getChannels(){
		return sql.getChannels("Channel_list");
	}

	/**
	 * @author Neil - dvd604
	 * @param channel
	 *            - The table to create
	 * **/
	public boolean createChannelTable(String channel) throws SQLException {
		try {
			return sql.excecute("CREATE TABLE IF NOT EXISTS `teknogeek_unacceptablebot`.`"+channel+"` (ID int NOT NULL AUTO_INCREMENT, Time text,Username text,Message text, PRIMARY KEY (ID))");
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error occurred making channel table");
			return false;
		}

	}

	/**
	 * @author Neil - dvd604
	 * @param channel
	 *            - The table to query.
	 * **/
	public ResultSet logQuery(String channel) {
		try {
			return sql.query("SELECT MAX(ID) FROM `teknogeek_unacceptablebot`.`"+channel+"`");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
