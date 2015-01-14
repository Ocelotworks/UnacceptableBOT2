package com.unacceptableuse.unacceptablebot.command;

import java.util.Date;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.Tools;
import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class CommandUptime extends Command
{

	@Override
	public String[] getAliases()
	{
		return new String[]{"uptime"};
	}

	@Override
	public String getHelp()
	{
		return "Returns the time "+Tools.getBotUsername()+" has been up.";
	}

	@Override
	public void performCommand(User sender, Channel channel, String message, String[] args)
	{
		sendMessage(Tools.getBotUsername()+" has been up for "+Tools.compareDate(UnacceptableBot.getConfigHandler().getLong("startupTime"),new Date().getTime()), channel);
		
	}

}
