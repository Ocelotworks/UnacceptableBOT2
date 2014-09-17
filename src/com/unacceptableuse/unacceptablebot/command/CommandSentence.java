package com.unacceptableuse.unacceptablebot.command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

/**
 * 
 * @author Peter
 *
 */
public class CommandSentence extends Command
{

	@SuppressWarnings("unused")
	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args)
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
				int id = rs.getInt(1);
				String sentence = rs.getString(2);
				if(context > 0)
				{
					for(int i = context; i > context; i--)
					{
						PreparedStatement cps = UnacceptableBot.getConfigHandler().sql.getPreparedStatement("SELECT Message FROM `teknogeek_unacceptablebot`.`"+channel.getName()+"` WHERE Id = '?' LIMIT 1");
						cps.setInt(1, id-i);
						ResultSet crs = cps.executeQuery();
						//TODO: fix this
						
					}
					sendMessage("----", channel);
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

	@Override
	public String getHelp() {
		return "Unfinished";
	}

}
