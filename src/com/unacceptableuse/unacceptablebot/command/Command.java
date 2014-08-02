package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.variable.Level;

public abstract class Command
{

	public Command()
	{
		
	}
	
	public void performCommand(User u, String[] args, String message)
	{
		
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
