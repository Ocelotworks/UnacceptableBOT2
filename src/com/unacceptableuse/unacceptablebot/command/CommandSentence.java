package com.unacceptableuse.unacceptablebot.command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class CommandSentence extends Command
{

	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args, PircBotX bot)
	{
		int limit = message.contains("--amount=") ? Integer.parseInt(message.split("amount=")[1].split(" ")[0]) : 1;
		int context = message.contains("--context=") ? Integer.parseInt(message.split("context=")[1].split(" ")[0]) : 0;
		String word = message.substring(message.indexOf(" "), message.indexOf("-") == -1 ? message.length() : message.indexOf("-"));
		
		try
		{
			PreparedStatement ps = UnacceptableBot.getConfigHandler().sql.getPreparedStatement("SELECT Id, Message FROM `teknogeek_unacceptablebot`.`"+channel.getName()+"` WHERE Message = '?' ORDER BY RAND() LIMIT ?");
			ps.setString(1, word);
			ps.setInt(2, limit > UnacceptableBot.getConfigHandler().getInteger("sentenceMaxLimit") ? 1 : limit);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next())
			{
				if(context > 0)
				{
					for(int i = 0; i < context; i++)
					{
						PreparedStatement cps = UnacceptableBot.getConfigHandler().sql.getPreparedStatement("SELECT Message from ");
						//not done
					}
				}
			}
			
		} catch (SQLException e)
		{
			
			e.printStackTrace();
		}
		
	}

	@Override
	public String[] getAliases()
	{
		
		return new String[]{"sentence"};
	}
	
	
	@Override
	public int requiredArguments()
	{
		return 1;
	}

}
