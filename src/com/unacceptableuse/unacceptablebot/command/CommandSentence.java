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

	@Override
	public String[] getAliases()
	{

		return new String[] { "sentence" };
	}

	@Override
	public String getHelp()
	{
		return "Unfinished";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{
		message.contains("--amount=");
		Integer.parseInt(message.split("amount=")[1].split(" ")[0]);
		final int context = message.contains("--context=") ? Integer.parseInt(message.split("context=")[1].split(" ")[0]) : 0;
		final String word = "%" + message.substring(message.indexOf(" "), message.indexOf("-") == -1 ? message.length() : message.indexOf("-")) + "%";

		try
		{
			final PreparedStatement ps = UnacceptableBot.getConfigHandler().sql.getPreparedStatement("SELECT Id,Username,Message FROM `stevie`.`" + channel.getName() + "` WHERE Message LIKE '" + word + "' ORDER BY RAND() LIMIT 1");
			ps.setString(1, word);
			// ps.setInt(2, limit > UnacceptableBot.getConfigHandler().getInteger("sentenceMaxLimit") ? 1 : limit);
			// ps.setInt(2, 1);
			final ResultSet rs = ps.executeQuery();

			while (rs.next())
			{
				final int id = rs.getInt(1);
				final String sentence = "<" + rs.getString(2) + "> " + rs.getString(3);
				sendMessage(sentence, channel);
				if (context > 0)
				{
					startID = id - context;
					sendMessage("ID: " + id + " | Context: " + context + " | startID: " + startID, channel);
					for (int i = startID; i >= id; i++)
					{
						final PreparedStatement cps = UnacceptableBot.getConfigHandler().sql.getPreparedStatement("SELECT Username,Message FROM `stevie`.`" + channel.getName() + "` WHERE Id=" + i);
						final ResultSet crs = cps.executeQuery();
						crs.next();
						sendMessage("<" + crs.getString(1) + "> " + crs.getString(2), channel);
					}
					sendMessage("----", channel);
				}
			}
		} catch (final SQLException e)
		{
			sendMessage("Error: " + e.toString() + " " + e.getMessage() + " " + e.getSQLState(), channel);
			e.printStackTrace();
		}

	}

	@Override
	public int requiredArguments()
	{
		return 1;
	}

}
