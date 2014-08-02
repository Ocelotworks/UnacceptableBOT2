package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.PircBotX;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.variable.Level;

public abstract class Command
{

	public Command()
	{
		
	}
	
	public void performCommand(User u, String[] args, String message, PircBotX ub)
	{
		
	}
	
	
	public void sendMessage(PircBotX ub, String message)
	{
		ub.sendIRC().message(target, message);
	}
	
	public abstract String[] getAliases();
	
	public Level getAccessLevel()
	{
		return Level.USER;
	}
	
	public int requiredArguments()
	{
		return 0;
	}
	
}
