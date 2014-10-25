package com.unacceptableuse.unacceptablebot.command;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.variable.Level;

/**
 *
 * @author Peter
 *
 */
public class CommandSql extends Command
{
	public CommandSql()
	{

	}

	@Override
	public Level getAccessLevel()
	{
		return Level.SUPERADMIN;
	}

	@Override
	public String[] getAliases()
	{
		return new String[] { "sql" };
	}

	@Override
	public String getHelp()
	{
		return "System Command";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{
		if (args[1].equals("set"))
			UnacceptableBot.getConfigHandler().setString(args[2], args[3]);
		else if (args[1].equals("get"))
			sendMessage(UnacceptableBot.getConfigHandler().getString(args[2]), channel);
		else if (args[1].equals("connect"))
			try
		{
				UnacceptableBot.getConfigHandler().sql.connect();
		} catch (final ClassNotFoundException e)
		{

			e.printStackTrace();
		} catch (final SQLException e)
		{

			e.printStackTrace();
		}
		else if (args[1].equals("health"))
			sendMessage(UnacceptableBot.getConfigHandler().sql.getConnectionHealth(), channel);
		else if (args[1].equals("query"))
			try
		{
				final ResultSet rs = UnacceptableBot.getConfigHandler().sql.query(message.split(args[1])[1].replace("&CHANNEL", channel.getName()).replace("&FQ", "`"));
				while (rs.next())
				{
					final StringBuilder stb = new StringBuilder();
					for (int i = 1; i == rs.getMetaData().getColumnCount(); i++)
						stb.append(rs.getString(i));
					sendMessage(stb.toString(), channel);
				}
		} catch (final Exception e)
		{
			sendMessage(e.getMessage(), channel);
		}
		else
			sendMessage("Unrecoginized argument " + args[1], channel.getName());

	}

	@Override
	public int requiredArguments()
	{
		return 0;
	}

}
