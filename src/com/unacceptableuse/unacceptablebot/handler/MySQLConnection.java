package com.unacceptableuse.unacceptablebot.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class MySQLConnection
{
	private static final String CONNECTION = "jdbc:mysql://boywanders.us:3306/";

	private static final String dbClassName = "com.mysql.jdbc.Driver";

	protected Connection c;
	
	private HashMap<String, String>cache = new HashMap<String, String>(); 

	private void attemptReconnect()
	{
		if (!isConnected())
			try
		{
				UnacceptableBot.log("SEVERE", "SQL", "SQL Connection lost. Reconnecting...");
				disconnect();
				connect();
		} catch (ClassNotFoundException | SQLException e)
		{
			UnacceptableBot.log("SEVERE", "SQL", "Unable to reconnect to SQL: " + e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * Connects to the MySQL server. Password is stored in static.properties
	 *
	 * @author UnacceptableUse, teknogeek
	 * @throws SQLException
	 *             Thrown if the username/password is rejected.
	 * @throws ClassNotFoundException
	 *             Thrown if the JDBC driver is not correctly installed.
	 */
	public void connect() throws SQLException, ClassNotFoundException
	{
		Class.forName(dbClassName);

		final Properties p = new Properties();
		p.put("user", "unacceptablebot");
		p.put("password", UnacceptableBot.getConfigHandler().getPassword("mysql"));

		c = DriverManager.getConnection(CONNECTION, p);

	}

	public void disconnect() throws SQLException
	{
		c.close();
	}

	public boolean excecute(final String sql) throws SQLException
	{
		return c.prepareStatement(sql).execute();
	}

	public int getAccessLevel(final String user)
	{
		try
		{
			final ResultSet rs = query("SELECT * FROM  teknogeek_settings.Access_Levels WHERE  `Username` =  '" + user + "' LIMIT 1");
			return rs.next() ? rs.getInt(2) : 0;
		} catch (final SQLException e)
		{
			e.printStackTrace();
			attemptReconnect();
			return 0;
		}
	}

	public String getChannels(final String setting)
	{
		try
		{
			final ResultSet rs = query("SELECT * FROM  `teknogeek_settings`.`Channels` WHERE  `Setting` =  '" + setting + "' LIMIT 1");
			return rs.next() ? rs.getString(2) : null;
		} catch (final SQLException e)
		{
			e.printStackTrace();
			attemptReconnect();
			return "Error " + e.getErrorCode() + " " + e.getLocalizedMessage();
		}
	}

	public String getConnectionHealth()
	{
		try
		{
			return "Connection: " + (c.isClosed() ? "&REDCLOSED&RESET" : "&GREENGOOD&RESET") + " Access: " + (c.isReadOnly() ? "&REDREAD-ONLY&RESET" : "&GREENREAD/WRITE&RESET" + " Validility: " + (c.isValid(10) ? "&GREENVALID&RESET" : "&REDTIMEOUT&RESET"));
		} catch (final SQLException e)
		{
			e.printStackTrace();
			attemptReconnect();
			return "Very, very bad. bad stuff is going on here. oh LORD JESUS SOMEONE SAVE US";
		}
	}

	public PreparedStatement getPreparedStatement(final String sql) throws SQLException
	{
		return c.prepareStatement(sql);
	}

	public String getSetting(final String setting)
	{
		if(cache.containsKey(setting)) 
			return cache.get(setting);
		try
		{
			PreparedStatement ps = getPreparedStatement("SELECT Value FROM  teknogeek_settings.Global_Settings WHERE  `Setting` =  ? LIMIT 1");
			ps.setString(1, setting);
			ResultSet rs = ps.executeQuery();
			String result = rs.next() ? rs.getString(1) : null; //Does returning "null" sound bad? Too bad!
			cache.put(setting, result);
			return result;
		} catch (final SQLException e)
		{
			e.printStackTrace();
			attemptReconnect();
			return "Error " + e.getErrorCode() + " " + e.getLocalizedMessage();
		}
	}

	public boolean incrementValue(final String setting, final int amt)
	{
		try
		{
			return excecute("UPDATE `teknogeek_settings`.`Global_Settings` SET `Value` = `Value`+" + amt + " WHERE `Setting` = '" + setting + "';");
		} catch (final SQLException e)
		{
			e.printStackTrace();
			attemptReconnect();
			return false;
		}
	}

	public boolean isConnected()
	{
		try
		{
			return c.isValid(10);
		} catch (final SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public ResultSet query(final String sql) throws SQLException
	{
		return c.prepareStatement(sql).executeQuery();
	}

	public boolean setAccessLevel(final String user, final int level)
	{
		try
		{
			return excecute("INSERT INTO `teknogeek_settings`.`Access_Levels` (`Username`, `Level`) VALUES ('" + user + "', '" + level + "') ON DUPLICATE KEY UPDATE Username=VALUES(Username);");
		} catch (final SQLException e)
		{
			e.printStackTrace();
			attemptReconnect();
			return false;
		}
	}

	public boolean setChannels(final String setting, final String value)
	{
		try
		{
			return excecute("INSERT INTO `teknogeek_settings`.`Channels` (`Setting`, `Value`) VALUES ('" + setting + "', '" + value + "') ON DUPLICATE KEY UPDATE Setting=VALUES(Setting);");
		} catch (final SQLException e)
		{
			e.printStackTrace();
			attemptReconnect();
			return false;
		}
	}

	public boolean setSetting(final String setting, final String value)
	{
		cache.put(setting, value);
		try
		{
			PreparedStatement ps = getPreparedStatement("INSERT INTO `teknogeek_settings`.`Global_Settings` (`Setting`, `Value`) VALUES (?, ?) ON DUPLICATE KEY UPDATE Setting=VALUES(Setting);" );
			ps.setString(1, setting);
			ps.setString(2, value);
			return ps.execute();
			

			// XXX: This seems inefficient. Poor database :(
		} catch (final SQLException e)
		{
			e.printStackTrace();
			attemptReconnect();
			return false;
		}
	}
}
