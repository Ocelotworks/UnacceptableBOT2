package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.User;

/**
 *
 * @author Joel?
 *
 */
public class CommandGoogle extends Command
{

	@Override
	public String[] getAliases()
	{
		return new String[] { "google", "lmgtfy" };
	}

	@Override
	public String getHelp()
	{
		return "Usage: google <terms> | Result: Produces a LMGTFY link using the specified terms";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{
		String add = "http://www.lmgtfy.com/?q=";
		for (int i = 1; i < args.length; i++)
			add += args[i] + "+";
		add = add.substring(0, add.length() - 1); // Wouldn't it be better to use StringBuilder here? - Peter
		sendMessage(add, channel);
	}

	@Override
	public int requiredArguments()
	{
		return 1;
	}

}
