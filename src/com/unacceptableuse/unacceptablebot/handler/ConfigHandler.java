package com.unacceptableuse.unacceptablebot.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.variable.Level;

public class ConfigHandler
{

	/*
	 * THIS IS WHERE THE BOT WILL CONTACT THE DATABASE, ANY CONFIGURATION WILL BE DRAWN FROM HERE
	 */
	
	private MySQLConnection sql = null;
	private Properties staticVars = null; 
	
	
	public void init()
	{
		staticVars = new Properties();
		try
		{
			staticVars.load(new FileInputStream(new File("static.properties")));
			System.out.println(staticVars.getProperty("mysql"));
		} catch (FileNotFoundException e)
		{
			
			e.printStackTrace();
		} catch (IOException e)
		{
			
			e.printStackTrace();
		}
		sql = new MySQLConnection();
		try
		{
			sql.connect();
		} catch (ClassNotFoundException e1)
		{
			
			e1.printStackTrace();
		} catch (SQLException e1)
		{
			
			e1.printStackTrace();
		}
	}
	
	
	public boolean isCommandDisabled(String command, String channel)
	{
		return getBoolean("cd:"+command+":"+channel);
	}
	
	public Level getUserLevel(User user)
	{
		int intLevel = getInteger("al:"+user.getUserId().toString());
		return intLevel < -1 ? Level.BOT : intLevel == 0 ? Level.USER : intLevel <= 5 ? Level.TRUSTED : intLevel <= 100 ? Level.ADMIN : intLevel > 100 ? Level.SUPERADMIN : intLevel == -1 ? Level.BANNED : Level.USER;
	}
	
	public String getPassword(String key)
	{
		return staticVars.getProperty(key);
	}
	
	public boolean getBoolean(String key)
	{
		return Boolean.parseBoolean(sql.getSetting(key));
	}
	
	public Integer getInteger(String key)
	{
		return Integer.parseInt(sql.getSetting(key)); 
	}
	
	public long getLong(String key)
	{
		return Long.parseLong(sql.getSetting(key));
	}
	
	public String getString(String key)
	{
		return sql.getSetting(key); 
	}
	
	public void setLong(String key, long val)
	{
		sql.setSetting(key, String.valueOf(val));
	}
	
	public void setBoolean(String key, boolean bool)
	{
		sql.setSetting(key, String.valueOf(bool));
	}
	
	public void setInteger(String key, Integer integer)
	{
		sql.setSetting(key, String.valueOf(integer));
	}
	
	public void setString(String key, String string)
	{
		sql.setSetting(key, string);
	}
	
	
}
