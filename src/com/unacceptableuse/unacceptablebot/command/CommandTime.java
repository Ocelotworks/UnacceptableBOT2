package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.variable.Level;

public class CommandTime extends Command{

	@Override
	public void performCommand(User sender, Channel channel, String message, String[] args, PircBotX bot)
	{
		bot.sendIRC().message(channel.getName(), "The time is now " + new java.util.Date().toString());
	}

	@Override
	public String[] getAliases() {
		return new String[]{"time"};
	}

	@Override
	public int requiredArguments()
	{
		return 1;
	}

	@Override
	public Level getAccessLevel() {
		return Level.USER;
	}
}
