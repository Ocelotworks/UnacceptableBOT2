package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.variable.Level;

public class CommandTwat extends Command
{

	@Override
	public Level getAccessLevel()
	{
		return Level.ADMIN;
	}

	@Override
	public String[] getAliases()
	{
		return new String[] { "twat" };
	}

	@Override
	public String getHelp()
	{
		return "System command";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{
		UnacceptableBot.twatMode = !UnacceptableBot.twatMode;
		sendMessage("Twat mode: " + UnacceptableBot.twatMode, channel);
	}

}
