package com.unacceptableuse.unacceptablebot;

import java.util.ArrayList;

import org.pircbotx.hooks.types.GenericMessageEvent;

import com.unacceptableuse.unacceptablebot.command.Command;

public class CommandHandler
{

	
	private ArrayList<Command> _commands = new ArrayList<Command>();
	
	public CommandHandler()
	{
		
	}
	
	
	public void init()
	{
		
	}
	
	
	public void processMessage(GenericMessageEvent event)
	{
		
	}
	
	
	
	public ArrayList<Command> getCommands()
	{
		return _commands;
	}
	
	public void setCommands(ArrayList<Command> c)
	{
		_commands = c;
	}
	
	public void addCommand(Command c)
	{
		_commands.add(c);
	}
	
	
	public void removeCommand(Command c)
	{
		_commands.remove(c);
	}
	
	public boolean removeCommand(String c)
	{
		for(Command command : getCommands())
		{
			for(String s  : command.getAliases())
			{
				if(s.equalsIgnoreCase(c))
				{
					removeCommand(command);
					return true;
				}
			}
		}
		return false;
	}
	
	public Command getCommand(String c)
	{
		for(Command command : getCommands())
		{
			for(String s : command.getAliases())
			{
				if(s.equalsIgnoreCase(c))
					return command;
			}
		}
		return null;
	}
	
	
	
	
	
}
