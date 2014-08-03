package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.PircBotX;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.variable.Level;

public abstract class Command
{
	
	public Command()
	{
		
	}
	
	public abstract void performCommand(String target, String[] args, String message, PircBotX bot);
	
	public void sendMessage(PircBotX ub, String message, String target)
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