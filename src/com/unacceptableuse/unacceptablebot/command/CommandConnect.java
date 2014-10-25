package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.variable.Level;

/**
 *
 * @author Peter
 *
 */
public class CommandConnect extends Command
{

	@Override
	public Level getAccessLevel()
	{
		return Level.ADMIN;
	}

	@Override
	public String[] getAliases()
	{
		return new String[] { "connect", "join" };
	}

	@Override
	public String getHelp()
	{
		return "Usage: join <channel> | Result: Joins the bot to the specified channel";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{
		UnacceptableBot.getBot().sendIRC().joinChannel(args[1]);
		sendMessage("Connecting to channel " + args[1], channel);
	}

	@Override
	public int requiredArguments()
	{
		return 1;
	}

}
