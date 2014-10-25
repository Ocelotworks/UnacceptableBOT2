package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

/**
 *
 * @author Peter
 *
 */
public class CommandWhoami extends Command
{

	@Override
	public String[] getAliases()
	{

		return new String[] { "whoami" };
	}

	@Override
	public String getHelp()
	{
		return "Usage: whoami | Result: Returns user info";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{
		sendMessage(sender.getNick() + " has UUID " + sender.getUserId() + " Level " + UnacceptableBot.getConfigHandler().getUserLevel(sender), channel);
	}

}
