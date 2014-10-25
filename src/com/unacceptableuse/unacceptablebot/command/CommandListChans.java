package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.variable.Level;

/**
 *
 * @author Neil
 *
 */
public class CommandListChans extends Command
{

	@Override
	public Level getAccessLevel()
	{
		return Level.ADMIN;
	}

	@Override
	public String[] getAliases()
	{
		return new String[] { "listchan", "channels", "chan" };
	}

	@Override
	public String getHelp()
	{
		return "System command";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{

		// When you complain about my code a child in Africa dies because of Ebola
		final String channels = "";
		for (final String channelStr : UnacceptableBot.getChannels())
			channels.concat(", ".concat(channelStr));
		sendMessage("Channels: ".concat(channels), channel);

	}
}
