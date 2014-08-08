package com.unacceptableuse.unacceptablebot.handler;

import java.util.ArrayList;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.command.Command;
import com.unacceptableuse.unacceptablebot.command.CommandConnect;
import com.unacceptableuse.unacceptablebot.command.CommandFaucet;
import com.unacceptableuse.unacceptablebot.command.CommandFillMeIn;
import com.unacceptableuse.unacceptablebot.command.CommandMessageStats;
import com.unacceptableuse.unacceptablebot.command.CommandSetup;
import com.unacceptableuse.unacceptablebot.command.CommandSql;
import com.unacceptableuse.unacceptablebot.variable.Level;

public class CommandHandler
{	
	public ArrayList<Command> _commands = new ArrayList<Command>();
	
	/**Register Commands here!**/
	public void init()
	{
		System.out.println("init m8");
		addCommand(new CommandSql());
		addCommand(new CommandFillMeIn());
		addCommand(new CommandConnect());
		addCommand(new CommandFaucet());
		addCommand(new CommandSetup());
		addCommand(new CommandMessageStats());
	}
	
	
	public void processMessage(MessageEvent event)
	{
		
		String message = event.getMessage();
		Channel channel = event.getChannel();
		User sender = event.getUser();
		PircBotX bot = event.getBot();
		

		Command chosenCommand = getCommand(message.replaceFirst("!", "").split(" ")[0]);
		
		if(chosenCommand == null)
		{
			return; //These arn't the commands you are looking for...
		}
		else
		{
			if(chosenCommand.getAccessLevel() == Level.BANNED)
			{
				event.respond("You do not have permission to perform this command");
			}else
			{
				if(chosenCommand.requiredArguments() > event.getMessage().split(" ").length)
				{
					event.respond("Insufficent Arguments. There should be help here but I havn't gotten around to it.");
				}else
				{
					UnacceptableBot.getConfigHandler().increment("stat:commandsPerformed");
					chosenCommand.performCommand(sender, channel, message, message.split(" "), bot);
				}
				
			}
			
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
		for(Command command : getCommands())
		{
			for(String s : command.getAliases())
			{
				if(s.toLowerCase().equals(c))
				{
					return command;
				}
			}
		}
		return null;
	}
	
	
	
	
	
}
