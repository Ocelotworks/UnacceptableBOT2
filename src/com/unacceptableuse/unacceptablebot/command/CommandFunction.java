package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.variable.Level;

public class CommandFunction extends Command
{

	@Override
	public void performCommand(User sender, Channel channel, String message,String[] args)
	{
		UnacceptableBot.getCommandHandler().addCommand(new CommandDynamic(args[1], message.substring(message.indexOf(args[2]))));
		sendMessage("Command added succesfully.", channel);
	}

	@Override
	public String[] getAliases()
	{
		return new String[]{"function"};
	}

	@Override
	public Level getAccessLevel()
	{
		return Level.USER;
	}
	
	@Override
	public int requiredArguments()
	{
		return 2;
	}

	@Override
	public String getHelp() {
		return "System command";
	}

}
