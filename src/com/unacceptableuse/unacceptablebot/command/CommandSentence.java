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
		String word = "%"+message.substring(message.indexOf(" "), message.indexOf("-") == -1 ? message.length() : message.indexOf("-"))+"%";
		
		
		try
		{
			PreparedStatement ps = UnacceptableBot.getConfigHandler().sql.getPreparedStatement("SELECT Id,Username,Message FROM `teknogeek_unacceptablebot`.`"+channel.getName()+"` WHERE Message LIKE ? ORDER BY RAND() LIMIT ?");
			ps.setString(1, word);
			ps.setInt(2, limit > UnacceptableBot.getConfigHandler().getInteger("sentenceMaxLimit") ? 1 : limit);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next())
			{
				int id = rs.getInt(1);
				String sentence = "<"+rs.getString(2)+"> "+rs.getString(3);
				sendMessage(sentence, channel);
				if(context > 0)
				{
					for(int i = context; i > context; i--)
					{
						PreparedStatement cps = UnacceptableBot.getConfigHandler().sql.getPreparedStatement("SELECT Username,Message FROM `teknogeek_unacceptablebot`.`"+channel.getName()+"` WHERE Id="+(id-i)+" LIMIT 1");
						ResultSet crs = cps.executeQuery();
						crs.next();
						sendMessage("<"+crs.getString(1)+"> "+crs.getString(2), channel);
						
					}
					sendMessage("----", channel);
				}
				
			}
			
		} catch (SQLException e)
		{
			sendMessage("Error: "+e.toString()+" "+e.getMessage()+" "+e.getSQLState(), channel);
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
