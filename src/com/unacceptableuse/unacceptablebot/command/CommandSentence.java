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
	int startID = 0;

	@SuppressWarnings("unused")
	@Override
	public void performCommand(User sender, Channel channel, String message, String[] args)
	{
		int limit = message.contains("--amount=") ? Integer.parseInt(message.split("amount=")[1].split(" ")[0]) : 1;
		int context = message.contains("--context=") ? Integer.parseInt(message.split("context=")[1].split(" ")[0]) : 0;
		String word = "%" + message.substring(message.indexOf(" "), message.indexOf("-") == -1 ? message.length() : message.indexOf("-")) + "%";

		try
		{
			PreparedStatement ps = UnacceptableBot.getConfigHandler().sql.getPreparedStatement("SELECT Id,Username,Message FROM `teknogeek_unacceptablebot`.`" + channel.getName() + "` WHERE Message LIKE '" + word + "' ORDER BY RAND() LIMIT 1");
			ps.setString(1, word);
			// ps.setInt(2, limit > UnacceptableBot.getConfigHandler().getInteger("sentenceMaxLimit") ? 1 : limit);
			// ps.setInt(2, 1);
			ResultSet rs = ps.executeQuery();

			while (rs.next())
			{
				int id = rs.getInt(1);
				String sentence = "<" + rs.getString(2) + "> " + rs.getString(3);
				sendMessage(sentence, channel);
				if (context > 0)
				{
					startID = id - context;
					sendMessage("ID: " + id + " | Context: " + context + " | startID: " + startID, channel);
					for (int i = startID; i >= id; i++)
					{
						PreparedStatement cps = UnacceptableBot.getConfigHandler().sql.getPreparedStatement("SELECT Username,Message FROM `teknogeek_unacceptablebot`.`" + channel.getName() + "` WHERE Id=" + i);
						ResultSet crs = cps.executeQuery();
						crs.next();
						sendMessage("<" + crs.getString(1) + "> " + crs.getString(2), channel);
					}
					sendMessage("----", channel);
				}
			}
		} catch (SQLException e)
		{
			sendMessage("Error: " + e.toString() + " " + e.getMessage() + " " + e.getSQLState(), channel);
			e.printStackTrace();
		}

	}

	@Override
	public String[] getAliases()
	{

		return new String[] { "sentence" };
	}

	@Override
	public int requiredArguments()
	{
		return 1;
	}

	@Override
	public String getHelp()
	{
		return "Unfinished";
	}

}
