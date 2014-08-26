package com.unacceptableuse.unacceptablebot.command;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.variable.Level;

/**
 * 
 * @author Peter
 *
 */
public class CommandSql extends Command
{
	public CommandSql()
	{
		
	}
	
	@Override
	public void performCommand(User sender, Channel channel, String message, String[] args, PircBotX bot)
	{
		if(args[1].equals("set"))
		{
			UnacceptableBot.getConfigHandler().setString(args[2], args[3]);
		}else if(args[1].equals("get")) 
		{
			sendMessage(bot, UnacceptableBot.getConfigHandler().getString(args[2]), channel.getName());
		}else if(args[1].equals("connect"))
		{
			try
			{
				UnacceptableBot.getConfigHandler().sql.connect();
			} catch (ClassNotFoundException e)
			{
				
				e.printStackTrace();
			} catch (SQLException e)
			{
				
				e.printStackTrace();
			}
		}else if(args[1].equals("query"))
		{
			try{
			ResultSet rs = UnacceptableBot.getConfigHandler().sql.query(message.split(args[2])[1].replace("&CHANNEL",channel.getName()));
			while(rs.next())
			{
				StringBuilder stb = new StringBuilder();
				for(int i = 1; i == rs.getMetaData().getColumnCount(); i++)
				{
					stb.append(rs.getString(i));
				}
				sendMessage(bot, stb.toString(), channel);
				
			}
			}catch(Exception e)
			{
				sendMessage(bot, e.getMessage(), channel);
			}
			
		}else
		{
			sendMessage(bot, "Unrecoginized argument "+args[1], channel.getName());
		}
		
	}
	
	
	
	@Override
	public String[] getAliases()
	{
		return new String[]{"sql"};
	}
	
	
	public int requiredArguments()
	{
		return 0;
	}

	@Override
	public Level getAccessLevel() {
		return Level.SUPERADMIN;
	}
	
}
