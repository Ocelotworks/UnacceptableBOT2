package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.User;

public class CommandCanIDate extends Command
{

	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args)
	{
		int dateAge = Integer.parseInt(args[2]), ourAge = Integer.parseInt(args[1]);
		sendMessage((dateAge/2)+7 < ourAge ? "She's too young bro. That's a bit weird" : (ourAge > 15 ? "Bang" : "Finger")+" away my friend", channel);
		
	}

	@Override
	public String[] getAliases()
	{
		return new String[]{"canidate"};
	}

	@Override
	public String getHelp()
	{
		return "<your age> <their age> - Calculate if you should date them or not";
	}
	
	@Override
	public int requiredArguments()
	{
		return 2;
	}

}
