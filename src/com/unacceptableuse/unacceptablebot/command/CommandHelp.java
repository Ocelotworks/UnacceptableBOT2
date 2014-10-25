package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

/**
 *
 * @author Neil
 *
 */
public class CommandHelp extends Command
{

	@Override
	public String[] getAliases()
	{
		return new String[] { "help", "?" };
	}

	@Override
	public String getHelp()
	{
		return "Usage help <command> | Returns: information about the specified command.";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{
		final Command command = UnacceptableBot.getCommandHandler().getCommand(args[1]);

		if (command != null)
			sendMessage(command.getHelp(), channel);
		else
			sendMessage("Command, " + args[1] + ", not found.", channel);
	}

	@Override
	public int requiredArguments()
	{
		return 1;
	}
}
