package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.variable.Level;

/**
 *
 * @author Peter
 *
 */
public class CommandBroadcast extends Command
{

	@Override
	public Level getAccessLevel()
	{
		return Level.ADMIN;
	}

	@Override
	public String[] getAliases()
	{
		return new String[] { "broadcast", "bc" };
	}

	@Override
	public String getHelp()
	{
		return "Usage: broadcast <message> | Result: Sends a message to every channel Stevie is connected to";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{

		for (int i = 0; i < UnacceptableBot.getChannels().size(); i++)
			sendMessage("BROADCAST FROM " + sender.getNick() + ": " + message.replace("!broadcast ", ""), UnacceptableBot.getChannels().get(i));

	}

	@Override
	public int requiredArguments()
	{
		return 1;
	}
}
