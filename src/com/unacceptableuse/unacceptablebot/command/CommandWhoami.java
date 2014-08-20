package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

/**
 * 
 * @author Peter
 *
 */
public class CommandWhoami extends Command
{

	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args, PircBotX bot)
	{
		sendMessage(bot, sender.getNick()+" has UUID "+sender.getUserId()+" Level "+UnacceptableBot.getConfigHandler().getUserLevel(sender), channel);
	}

	@Override
	public String[] getAliases()
	{
		
		return new String[]{"whoami"};
	}

}
