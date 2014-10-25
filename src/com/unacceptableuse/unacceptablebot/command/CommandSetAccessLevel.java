package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.variable.Level;

public class CommandSetAccessLevel extends Command
{

	@Override
	public Level getAccessLevel()
	{
		return Level.ADMIN;
	}

	@Override
	public String[] getAliases()
	{

		return new String[] { "setaccesslevel" };
	}

	@Override
	public String getHelp()
	{
		return "System command";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{

		User theUser = null;
		int newLevel = 0;

		try
		{
			newLevel = Integer.parseInt(args[2]);
		} catch (final NumberFormatException e)
		{
			sendMessage("Invalid access level", channel);
		}

		for (final User u : channel.getUsers())
			if (u.getNick().equals(args[1]))
			{
				theUser = u;
				break;
			}

		if (theUser == null)
		{
			sendMessage("The user could not be found, are they in this channel?", channel);
			return;
		}
		if (!theUser.isVerified())
		{
			sendMessage("The user must be verified with nickserv!", channel);
			return;
		}
		// if(channel.getOps().contains(theUser))
		// {
		// sendMessage("You cannot change the access level of ops", channel); //Not Fully Implemented
		// return;
		// }
		if (UnacceptableBot.getConfigHandler().getUserLevel(sender) < newLevel)
		{
			sendMessage("You cannot set someones access level higher than your own!", channel);
			return;
		}

		UnacceptableBot.getConfigHandler().setUserLevel(theUser, newLevel);
		sendMessage(args[1] + "'s access level is now " + newLevel, channel);

	}

}
