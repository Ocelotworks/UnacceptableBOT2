package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.handler.CommandHandler;
import com.unacceptableuse.unacceptablebot.variable.Level;

public abstract class Command
{
	
	/**
	 * This is called whenever the command is performed
	 * @param sender The {@link User} object of whoever triggered the command
	 * @param channel The {@link Channel} object of the channel it performed in.
	 * @param message The whole message
	 * @param args The message, split by spaces where args[0] is the command performed
	 * @param bot The {@link PircBotX} instance that performed the command
	 * @author UnacceptableUse
	 */
	public abstract void performCommand(User sender, Channel channel, String message, String[] args);
	
	
	public void sendMessage(String message, String target)
	{
		UnacceptableBot.getBot().sendIRC().message(target, message
				.replace("&BOLD", Colors.BOLD)
				.replace("&UNDERLINE", Colors.UNDERLINE)
				.replace("&REVERSE", Colors.REVERSE)
				.replace("&PURPLE", Colors.PURPLE)
				.replace("&RED", Colors.RED)
				.replace("&GREEN", Colors.GREEN)
				.replace("&BLUE", Colors.BLUE)
				.replace("&YELLOW", Colors.YELLOW)
				.replace("&BLACK", Colors.BLACK)
				.replace("&CYAN", Colors.CYAN)
				.replace("&WHITE", Colors.WHITE)				
				.replace("&RESET", Colors.NORMAL));
	}
	
	public void sendMessage(String message, Channel channel)
	{
		sendMessage(message, channel.getName());
	}
	
	public void sendMessage(String message, User user)
	{
		sendMessage(message, user.getNick());
	}
	
	
	public abstract String[] getAliases();
	
	public abstract Level getAccessLevel();
	
	public abstract String getHelp();
	
	/**
	 * Override this if a command has any arguments, forcing the {@link CommandHandler} to throw an error if there are no arguments
	 * @author UnacceptableUse
	 * @return
	 */
	public int requiredArguments()
	{
		return 0;
	}
	
}