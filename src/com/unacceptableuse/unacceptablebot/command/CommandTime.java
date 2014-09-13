package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.variable.Level;

public class CommandTime extends Command{

	@Override
	public void performCommand(User sender, Channel channel, String message, String[] args)
	{
		sendMessage("The time is now " + new java.util.Date().toString(), channel);
	}

	@Override
	public String[] getAliases() {
		return new String[]{"time"};
	}

	@Override
	public int requiredArguments()
	{
		return 0;
	}

	@Override
	public Level getAccessLevel() {
		return Level.USER;
	}

	@Override
	public String getHelp() {
		return "Usage: time | Result: Returns the current system time";
	}
}
