package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class CommandFunction extends Command
{

	@Override
	public String[] getAliases()
	{
		return new String[] { "function" };
	}

	@Override
	public String getHelp()
	{
		return "System command";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{
		UnacceptableBot.getCommandHandler().addCommand(new CommandDynamic(args[1], message.substring(message.indexOf(args[2]))));
		sendMessage("Command added succesfully.", channel);
	}

	@Override
	public int requiredArguments()
	{
		return 2;
	}

}
