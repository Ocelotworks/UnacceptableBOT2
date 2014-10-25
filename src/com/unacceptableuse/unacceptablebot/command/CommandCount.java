package com.unacceptableuse.unacceptablebot.command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class CommandCount extends Command
{

	@Override
	public String[] getAliases()
	{
		return new String[] { "count" };
	}

	@Override
	public String getHelp()
	{
		return "";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{
		try
		{
			final String phrase = message.replace(args[0], "").trim();
			final PreparedStatement ps = UnacceptableBot.getConfigHandler().sql.getPreparedStatement("SELECT COUNT(*) FROM `teknogeek_unacceptablebot`.`" + channel.getName() + "` WHERE Message LIKE ?");
			ps.setString(1, "%" + phrase + "%");
			final ResultSet rs = ps.executeQuery();
			rs.next();
			final int amt = rs.getInt("COUNT(*)");

			sendMessage("The " + (args.length > 2 ? "phrase" : "word") + " '" + phrase + "' " + (amt == 0 ? "has never been used." : " has been used &BOLD" + amt + "&RESET times."), channel);

		} catch (final Exception e)
		{
			sendMessage("There was an error trying to count this word.(" + e.toString() + ")", channel);
			e.printStackTrace();
		}

	}

	@Override
	public int requiredArguments()
	{
		return 1;
	}

}
