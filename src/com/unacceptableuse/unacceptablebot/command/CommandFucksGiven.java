package com.unacceptableuse.unacceptablebot.command;

import java.util.Random;

import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;

/**
 *
 * @author Peter
 *
 */
public class CommandFucksGiven extends Command
{

	@Override
	public String[] getAliases()
	{
		return new String[] { "fucksgiven" };
	}

	@Override
	public String getHelp()
	{
		return "Usage: fucksgiven | Result: Shows how many fucks you give";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{
		sendMessage("Fucks given: " + Colors.BOLD + ((((int) Math.signum(new Random().nextInt())) - (sender.getNick().length() * channel.getName().length()))), channel);

	}

}
