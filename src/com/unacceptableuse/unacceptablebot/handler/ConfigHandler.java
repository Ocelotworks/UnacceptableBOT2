package com.unacceptableuse.unacceptablebot.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import org.pircbotx.User;

public class ConfigHandler
{

	/*
	 * THIS IS WHERE THE BOT WILL CONTACT THE DATABASE, ANY CONFIGURATION WILL BE DRAWN FROM HERE
	 */

	public MySQLConnection sql = null;
	private Properties staticVars = null;
	private ArrayList<String> chanTables = new ArrayList<String>();


	/**
	 * @author Neil - dvd604
	 * @param channel
	 *            - The table to create
	 * **/
	public boolean createChannelTable(final String channel) throws SQLException
	{
		try
		{
			if(chanTables.contains(channel))return true;
			chanTables.add(channel);
			return sql.excecute("CREATE TABLE IF NOT EXISTS `stevie`.`" + channel + "` (ID int NOT NULL AUTO_INCREMENT, Time text,Username text,Message text, PRIMARY KEY (ID))");
		} catch (final SQLException e)
		{
			e.printStackTrace();
			System.err.println("Error occurred making channel table");
			return false;
		}

	}

	public boolean getBoolean(final String key)
	{
		final String result = sql.getSetting(key);
		return result == null ? false : Boolean.parseBoolean(result);
	}

	public String getChannels()
	{
		return sql.getChannels("Channel_list");
	}

	public float getFloat(final String key)
	{
		return Float.parseFloat(sql.getSetting(key));
	}

	public String getHomeChannel()
	{
		return sql.getSetting("homeChannel");
	}

	public Integer getInteger(final String key)
	{
		try
		{
			return Integer.parseInt(sql.getSetting(key));
		} catch (final NumberFormatException e)
		{
			return 0;
		}
	}

	/**
	 * @author Neil - dvd604
	 * @param channel
	 *            - The channel to return the log for
	 * @param log
	 *            - Int, the row of the table to return. See method body for help
	 * **/
	public ResultSet getLog(final String channel, final int log)
	{
		try
		{
			// This gets the logth record. It requires some black magic before
			// handing it a number
			// Similar to the stuff I did in the first fillmein.
			final ResultSet rs = sql.query("SELECT Time,Username,Message FROM `stevie`.`" + channel + "` ORDER BY ID LIMIT " + log + ",1");
			return rs;
		} catch (final SQLException e)
		{
			e.printStackTrace();

		}
		return null;
	}

	public long getLong(final String key)
	{
		return Long.parseLong(sql.getSetting(key));
	}

	public String getPassword(final String key)
	{
		return staticVars.getProperty(key);
	}

	public String getString(final String key)
	{
		String get = sql.getSetting(key);
		return get == null ? "not set" : get;
	}

	public int getUserLevel(final User user)
	{
		return user.isIrcop() ? 10 : user.isVerified() ? sql.getAccessLevel(user.getNick()) : 0;
	}

	public void increment(final String key)
	{
		increment(key, 1);
	}

	public void increment(final String key, final int amt)
	{
		sql.incrementValue(key, amt);
	}

	/**
	 * Initializes the configurations, {@link MySQLConnection} and {@link Properties}
	 */
	public void init()
	{
		staticVars = new Properties();
		try
		{
			staticVars.load(new FileInputStream(new File("static.properties")));
			System.out.println(staticVars.getProperty("mysql"));
		} catch (final FileNotFoundException e)
		{

			e.printStackTrace();
		} catch (final IOException e)
		{

			e.printStackTrace();
		}
		sql = new MySQLConnection();
		try
		{
			sql.connect();
		} catch (final ClassNotFoundException e1)
		{

			e1.printStackTrace();
		} catch (final SQLException e1)
		{

			e1.printStackTrace();
		}
	}

	public boolean isCommandDisabled(final String command, final String channel)
	{
		return getBoolean("cd:" + command + ":" + channel);
	}

	public boolean isConnected()
	{
		return sql.isConnected();
	}

	/**
	 * @author Neil - dvd604
	 * @param channel
	 *            - The table to query.
	 * **/
	public ResultSet logQuery(final String channel)
	{
		try
		{
			return sql.query("SELECT MAX(ID) FROM `stevie`.`" + channel + "`");
		} catch (final SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public void setBoolean(final String key, final boolean bool)
	{
		sql.setSetting(key, String.valueOf(bool));
	}

	public void setChannels(final String chans)
	{
		sql.setChannels("Channel_list", chans);
	}

	public void setFloat(final String key, final float val)
	{
		sql.setSetting(key, String.valueOf(val));
	}

	public void setInteger(final String key, final Integer integer)
	{
		sql.setSetting(key, String.valueOf(integer));
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
	public boolean setLog(final String time, final String user, final String message, final String channel)
	{
		try
		{
			String safeMessage = message.replaceAll("(;|\\s)(exec|execute|select|insert|update|delete|create|alter|drop|rename|truncate|backup|restore)\\s", "ಠ_ಠ ");
			return sql.excecute("INSERT INTO `stevie`.`" + channel + "` (`Time`, `Username`, `Message`) VALUES ("+time+", "+user+", "+safeMessage+");");
		} catch (final SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public void setLong(final String key, final long val)
	{
		sql.setSetting(key, String.valueOf(val));
	}

	public void setString(final String key, final String string)
	{
		sql.setSetting(key, string);
	}

	public void setUserLevel(final User user, final int level)
	{
		if (user.isVerified())
			sql.setAccessLevel(user.getNick(), level);
	}


}
