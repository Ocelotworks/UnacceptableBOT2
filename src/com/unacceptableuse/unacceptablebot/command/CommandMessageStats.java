package com.unacceptableuse.unacceptablebot.command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class CommandMessageStats extends Command
{

	@Override
	public String[] getAliases()
	{
		return new String[] { "messagestats" };
	}

	@Override
	public String getHelp()
	{
		return "Usage: messagestats [-u <user> | --user <user>] [-ch <channel> | --channel <channel>] | Result: Grades the channel of several recorded stats";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{
		String target = sender.getNick();
		String targetChannel = channel.getName();
		float targetMessagesCount = 0.0f, channelMessagesCount = 0.0f;

		for (int i = 0; i < args.length; i++)
			if (args[i].toLowerCase().contains("--channel") || args[i].toLowerCase().contains("-ch"))
				targetChannel = args[i + 1].charAt(0) == '#' ? args[i + 1] : "#" + args[i + 1];
				else if (args[i].toLowerCase().contains("--user") || args[i].toLowerCase().contains("-u"))
					target = args[i + 1];

		try
		{
			final ArrayList<String> targetMessages = new ArrayList<String>();
			final ArrayList<String> channelMessages = new ArrayList<String>();

			final PreparedStatement ps = UnacceptableBot.getConfigHandler().sql.getPreparedStatement("SELECT Count(Message) AS MessageCount FROM `teknogeek_unacceptablebot`.`" + targetChannel + "`");

			final ResultSet rs = ps.executeQuery();
			while (rs.next())
				channelMessages.add(rs.getString("MessageCount"));
			channelMessagesCount = Integer.valueOf(channelMessages.get(0));

			final PreparedStatement ps1 = UnacceptableBot.getConfigHandler().sql.getPreparedStatement("SELECT Count(Message) AS MessageCount FROM `teknogeek_unacceptablebot`.`" + targetChannel + "` WHERE Username = '" + target + "'");

			final ResultSet rs1 = ps1.executeQuery();
			while (rs1.next())
				targetMessages.add(rs1.getString("MessageCount"));
			targetMessagesCount = Integer.valueOf(targetMessages.get(0));

			final float percent = (targetMessagesCount / channelMessagesCount) * 100;

			sendMessage("&BOLD" + target + "&RESET has sent &BOLD" + Math.round(targetMessagesCount) + "&RESET messages in &BOLD" + targetChannel + "&RESET. &BOLD" + new DecimalFormat("#.##").format(percent) + "&RESET% of all messages.", channel);

			/*
			 * sendMessage( "&BOLD" + target + "&RESET has sent &BOLD" + totalCount + "&RESET messages over &BOLD" + count.size() + "&RESET channels. &BOLDFuck knows I CBA fixing this&RESET% of all messages.", channel);
			 */
		} catch (final MySQLSyntaxErrorException e)
		{
			sendMessage("The channel " + targetChannel + " has not been logged.", message);
		} catch (final Exception e)
		{
			sendMessage("&REDSHIT!&RESET: " + e.toString(), channel);
		}
	}

}
