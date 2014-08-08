package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

public class CommandConnect extends Command
{

	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args, PircBotX bot)
	{
		bot.sendIRC().joinChannel(args[1]);
		sendMessage(bot, "Connecting to channel "+args[1], channel);
	}

	@Override
	public String[] getAliases()
	{
		return new String[]{"connect","join"};
	}
	
	@Override
	public int requiredArguments()
	{
		return 1;
	}

}
