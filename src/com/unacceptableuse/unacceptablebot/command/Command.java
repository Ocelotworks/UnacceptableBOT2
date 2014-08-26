package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

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
	public abstract void performCommand(User sender, Channel channel, String message, String[] args, PircBotX bot);
	
	
	public void sendMessage(PircBotX ub, String message, String target)
	{
		ub.sendIRC().message(target, message);
	}
	
	public void sendMessage(PircBotX ub, String message, Channel channel)
	{
		sendMessage(ub, message, channel.getName());
	}
	
	public void sendMessage(PircBotX ub, String message, User user)
	{
		sendMessage(ub, message, user.getNick());
	}
	
	public abstract String[] getAliases();
	
	public abstract Level getAccessLevel();
	
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