package com.unacceptableuse.unacceptablebot.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.variable.Level;

public class ConfigHandler {

	/*
	 * THIS IS WHERE THE BOT WILL CONTACT THE DATABASE, ANY CONFIGURATION WILL
	 * BE DRAWN FROM HERE
	 */

	private MySQLConnection sql = null;
	private Properties staticVars = null;

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

	public boolean isCommandDisabled(String command, String channel) {
		return getBoolean("cd:" + command + ":" + channel);
	}

	public Level getUserLevel(User user) {
		Integer intLevel = getInteger("al:" + user.getUserId().toString());
		return intLevel == null ? Level.USER : Level.intToLevel(intLevel);
	}

	public void setUserLevel(String uuid, Level level) {
		setInteger("al:" + uuid, Level.levelToInt(level));
	}

	public String getPassword(String key) {
		return staticVars.getProperty(key);
	}

	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(sql.getSetting(key));
	}

	public Integer getInteger(String key) {
		return Integer.parseInt(sql.getSetting(key));
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
			return sql.excecute("INSERT INTO `database`.`" + channel
					+ "` (`Time`, `Username`, `Message`) VALUES ('" + time
					+ "', '" + user + ", '" + message + "'');");
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
			ResultSet rs = sql.query("SELECT * FROM `database`.`" + channel
					+ "` ORDER BY ID LIMIT n,1");
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();

		}
		return null;
	}

	/**
	 * @author Neil - dvd604
	 * @param channel
	 *            - The table to create
	 * **/
	public boolean createChannelTable(String channel) throws SQLException {
		try {
			sql.excecute("CREATE TABLE IF NOT EXISTS 'Channels'.'" + channel
					+ "'('ID' int,'Time' text,'Username' text,'Message' text);");
			return sql.excecute("ALTER TABLE 'Channels'.'" + channel
					+ "' ADD PRIMARY KEY (ID);");
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
			return sql.query("SELECT ID FROM Channels." + channel
					+ " ORDER BY date DESC LIMIT 1");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
