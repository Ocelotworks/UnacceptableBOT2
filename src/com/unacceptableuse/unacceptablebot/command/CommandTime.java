package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.User;

public class CommandTime extends Command
{

	@Override
	public String[] getAliases()
	{
		return new String[] { "time" };
	}

	@Override
	public String getHelp()
	{
		return "Usage: time | Result: Returns the current system time";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{
		sendMessage("The time is now " + new java.util.Date().toString(), channel);
	}

	@Override
	public int requiredArguments()
	{
		return 0;
	}
}
