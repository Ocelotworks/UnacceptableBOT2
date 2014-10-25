package com.unacceptableuse.unacceptablebot.command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class CommandCount extends Command
{

	@Override
	public void performCommand(User sender, Channel channel, String message, String[] args)
	{
		try
		{
			String phrase = message.replace(args[0], "").trim();
			PreparedStatement ps = UnacceptableBot.getConfigHandler().sql.getPreparedStatement("SELECT COUNT(*) FROM `teknogeek_unacceptablebot`.`" + channel.getName() + "` WHERE Message LIKE ?");
			ps.setString(1, "%" + phrase + "%");
			ResultSet rs = ps.executeQuery();
			rs.next();
			int amt = rs.getInt("COUNT(*)");

			sendMessage("The " + (args.length > 2 ? "phrase" : "word") + " '" + phrase + "' " + (amt == 0 ? "has never been used." : " has been used &BOLD" + amt + "&RESET times."), channel);

		} catch (Exception e)
		{
			sendMessage("There was an error trying to count this word.(" + e.toString() + ")", channel);
			e.printStackTrace();
		}

	}

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
	public int requiredArguments()
	{
		return 1;
	}

}
