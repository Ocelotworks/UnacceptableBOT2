package com.unacceptableuse.unacceptablebot.command;

import java.sql.SQLException;

import org.pircbotx.PircBotX;

import com.unacceptableuse.unacceptablebot.handler.MySQLConnection;
import com.unacceptableuse.unacceptablebot.variable.Level;

public class CommandSql extends Command
{
	public CommandSql()
	{
		
	}
	
	@Override
	public void performCommand(String target, String[] args, String message, PircBotX bot)
	{
		MySQLConnection con = new MySQLConnection();
		try
		{
			con.connect();
		}
		catch (ClassNotFoundException | SQLException e)
		{
			e.printStackTrace();
		}
		sendMessage(con.getSetting(), target, bot);
		try
		{
			con.disconnect();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	
	public void sendMessage(String message, String target, PircBotX ub)
	{
		ub.sendIRC().message(target, message);
	}
	
	@Override
	public String[] getAliases()
	{
		return new String[]{"sql"};
	}
	
	public Level getAccessLevel()
	{
		return Level.USER;
	}
	
	public int requiredArguments()
	{
		return 0;
	}
	
}