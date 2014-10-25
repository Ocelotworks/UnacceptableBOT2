package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

/**
 *
 * @author Peter
 *
 */
public class CommandRand extends Command
{

	@Override
	public String[] getAliases()
	{

		return new String[] { "rand", "random" };
	}

	@Override
	public String getHelp()
	{
		return "Usage: rand <choice 1, choice 2, choice ...> | Result: Picks a supplied string at random";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{
		final String[] choice = message.replace("!rand", "").replace("!choice", "").split(",");
		final String chosen = choice[UnacceptableBot.rand.nextInt(choice.length)];
		sendMessage((chosen.charAt(0) == '!') || (chosen.charAt(0) == '.') ? "&REDYou can't use !rand to perform commands!" : chosen, channel);

	}

	@Override
	public int requiredArguments()
	{
		return 2;
	}

}
