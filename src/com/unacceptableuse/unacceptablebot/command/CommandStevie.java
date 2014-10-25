package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

/**
 * 
 * @author Peter
 *
 */
public class CommandStevie extends Command
{

	@Override
	public void performCommand(User sender, Channel channel, String message, String[] args)
	{
		if (args[1].equalsIgnoreCase("credits"))
		{
			sendMessage(UnacceptableBot.getConfigHandler().getString("botName") + " uses PircBotX. Made by Peter, Jool, Neil. (unacceptableuse.com)", channel);
			sendMessage("Special thanks to irc.matrixdevuk.pw for hosting the bot's website/database. Special thanks to Atmalik for hosting the bot!", channel);
		} else if (args[1].equalsIgnoreCase("source"))
		{
			sendMessage("Source can be found here: https://github.com/UnacceptableUse/UnacceptableBot2", channel);
		} else if (args[1].equalsIgnoreCase("about"))
		{
			sendMessage(UnacceptableBot.getConfigHandler().getString("botName") + " is a utility/logging bot originally designed for ##Ocelotworks", channel);
		} else if (args[1].equalsIgnoreCase("help"))
		{
			sendMessage("Help can be found here: http://unacceptableuse.com/bot/help.html", channel);
		} else
		{
			sendMessage("I literally can't even hold all these feels.", channel);
		}
	}

	@Override
	public int requiredArguments()
	{
		return 1;
	}

	@Override
	public String[] getAliases()
	{

		return new String[] { "stevie", "steviebot", "ub" };
	}

	@Override
	public String getHelp()
	{
		return "Usage: stevie <credits|source|about|help> | Result: Produces information on the selected topic";
	}

}
