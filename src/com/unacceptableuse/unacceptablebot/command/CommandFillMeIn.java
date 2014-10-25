package com.unacceptableuse.unacceptablebot.command;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.handler.ConfigHandler;

/**
 *
 * @author Neil
 *
 */
public class CommandFillMeIn extends Command
{

	@Override
	public String[] getAliases()
	{
		return new String[] { "fillmein", "fmi" };
	}

	@Override
	public String getHelp()
	{
		return "Usage: fmi <number> | Result: Shows you the last specified number of messages";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{
		String id = "";
		try
		{
			final ConfigHandler config = UnacceptableBot.getConfigHandler();
			final ResultSet rs = config.logQuery(channel.getName());
			rs.next();
			id = rs.getString(1);
			System.out.println("id");
		} catch (final SQLException e)
		{
			e.printStackTrace();
			System.out.println("Error from CommandFillMeIn.class. Ranging from line 20 to line 24");
		}
		try
		{
			for (int i = 0; i < Integer.parseInt(args[1]); i++)
			{
				final ResultSet rs = UnacceptableBot.getConfigHandler().getLog(channel.getName(), Integer.parseInt(id) - i);
				rs.next();
				UnacceptableBot.getBot().sendIRC().notice(sender.getNick(), "[" + rs.getString(1) + "] <" + rs.getString(2) + "> " + rs.getString(3));
			}
		} catch (final SQLException e)
		{
			e.printStackTrace();
			System.out.println("Error from CommandFillMeIn.class. Ranging from line 29 to line 38");
		}
	}

	@Override
	public int requiredArguments()
	{
		return 1;
	}

}
