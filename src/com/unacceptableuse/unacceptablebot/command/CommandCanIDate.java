package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.User;

public class CommandCanIDate extends Command
{

	@Override
	public String[] getAliases()
	{
		return new String[] { "canidate" };
	}

	@Override
	public String getHelp()
	{
		return "<your age> <their age> - Calculate if you should date them or not";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{
		final int dateAge = Integer.parseInt(args[2]), ourAge = Integer.parseInt(args[1]);
		// sendMessage((ourAge/2)+7 >= dateAge ? (dateAge/2)+7 >= ourAge ? (dateAge >= 16 && ourAge < 16) ? "You're too young... This has statutory rape written all over it!" : "You're a bit young dude" : (dateAge >= 16 && ourAge >= 16 ? "Bang" : "Finger")+" away my friend" : "She's a bit young bro... That's weird...", channel);
		if (((ourAge / 2) + 7) >= dateAge)
		{
			if (((dateAge / 2) + 7) >= ourAge)
			{
				if ((dateAge >= 16) && (ourAge < 16))
					sendMessage("You're too young, and this has statutory rape written all over it!", channel);
				else
					sendMessage("You're too young... That's a bit weird", channel);
			} else
				sendMessage(((dateAge >= 16) && (ourAge >= 16) ? "Bang" : "Finger") + " away my friend", channel);
		} else
			sendMessage("She's too young bro. That's " + (dateAge < 12 ? "very" : "a little") + " weird", channel);

	}

	@Override
	public int requiredArguments()
	{
		return 2;
	}

}
