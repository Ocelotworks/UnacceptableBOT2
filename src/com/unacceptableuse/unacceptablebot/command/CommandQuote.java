package com.unacceptableuse.unacceptablebot.command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

/**
 *
 * @author Neil
 *
 */
public class CommandQuote extends Command
{

	@Override
	public String[] getAliases()
	{
		return new String[] { "quote" };
	}

	@Override
	public String getHelp()
	{
		return "Usage: quote <user> [-c <number> | --count <number>] [-ch <channel> | --channel <channel>]";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{

		int count = 1;

		String quoteChannel = channel.getName();

		for (int i = 0; i < args.length; i++)
			if (args[i].toLowerCase().contains("--channel") || args[i].toLowerCase().contains("-ch"))
				quoteChannel = args[i + 1].startsWith("#") ? args[i + 1] : "#" + args[i + 1];
				else if (args[i].toLowerCase().contains("--count") || args[i].toLowerCase().contains("-c"))
					try
		{
						count = Integer.parseInt(args[i + 1]);
		} catch (ArrayIndexOutOfBoundsException | NumberFormatException e)
		{
			sendMessage("Something went wrong here", channel);
			UnacceptableBot.log("ERROR", "!quote", e.toString());
			e.printStackTrace();
		}

		try
		{
			for (int i = 0; i < count; i++)
			{
				final PreparedStatement ps = UnacceptableBot.getConfigHandler().sql.getPreparedStatement("SELECT Message FROM `teknogeek_unacceptablebot`.`" + quoteChannel + "` WHERE Username = '" + args[1] // No replace needed
						+ "' ORDER BY RAND() LIMIT " + count);
				final ResultSet rs = ps.executeQuery();
				if (rs.next())
					sendMessage("<" + args[1] + "> " + rs.getString(1), channel);
			}
		} catch (final SQLException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public int requiredArguments()
	{
		return 1;
	}

}