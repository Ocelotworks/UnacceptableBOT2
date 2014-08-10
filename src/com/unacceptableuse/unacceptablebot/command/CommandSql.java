package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class CommandSql extends Command
{
	public CommandSql()
	{
		
	}
	
	@Override
	public void performCommand(User sender, Channel channel, String message, String[] args, PircBotX bot)
	{
		if(args[1].equals("set"))
		{
			UnacceptableBot.getConfigHandler().setString(args[2], args[3]);
		}else if(args[1].equals("get")) 
		{
			sendMessage(bot, UnacceptableBot.getConfigHandler().getString(args[2]), channel.getName());
		}else
		{
			sendMessage(bot, "Unrecoginized argument "+args[1], channel.getName());
		}
		
	}
	
	
	
	@Override
	public String[] getAliases()
	{
		return new String[]{"sql"};
	}
	
	
	public int requiredArguments()
	{
		return 0;
	}
	
}