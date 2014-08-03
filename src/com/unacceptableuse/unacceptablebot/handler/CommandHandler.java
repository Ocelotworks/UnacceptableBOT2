package com.unacceptableuse.unacceptablebot.handler;

import java.util.ArrayList;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.MessageEvent;

import com.unacceptableuse.unacceptablebot.command.Command;
import com.unacceptableuse.unacceptablebot.command.CommandFillMeIn;
import com.unacceptableuse.unacceptablebot.command.CommandSql;

public class CommandHandler
{	
	public ArrayList<Command> _commands = new ArrayList<Command>();
	
	/**Register Commands here!**/
	public void init()
	{
		System.out.println("init m8");
		addCommand(new CommandSql());
		addCommand(new CommandFillMeIn());
	}
	
	
	public void processMessage(MessageEvent event)
	{
		
		String message = event.getMessage();
		Channel channel = event.getChannel();
		User sender = event.getUser();
		PircBotX bot = event.getBot();
		

		Command chosenCommand = getCommand(message.replaceFirst("!", "").split(" ")[0]);
		
		System.out.println("Chosen Command: " + chosenCommand);
		
		if(chosenCommand == null)
		{
			return; //These arn't the commands you are looking for...
		}
		else
		{
			chosenCommand.performCommand(sender, channel, message, message.split(" "), bot);
		}
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
		System.out.println("C: " + c);
		for(Command command : _commands)
		{
			System.out.println("Command: " + command);
			for(String s : command.getAliases())
			{
				System.out.println("S: " + s);
				if(s.toLowerCase().equals(c))
				{
					return command;
				}
			}
		}
		return null;
	}
	
	
	
	
	
}
