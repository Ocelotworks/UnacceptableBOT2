package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.variable.Level;

/**
 * 
 * @author Peter
 *
 */
public class CommandConnect extends Command
{

	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args, PircBotX bot)
	{
		bot.sendIRC().joinChannel(args[1]);
		sendMessage("Connecting to channel "+args[1], channel);
	}

	@Override
	public String[] getAliases()
	{
		return new String[]{"connect","join"};
	}
	
	@Override
	public int requiredArguments()
	{
		return 1;
	}

	@Override
	public Level getAccessLevel() {
		return Level.ADMIN;
	}

	@Override
	public String getHelp() {
		return "Usage: join <channel> | Result: Joins the bot to the specified channel";
	}

}
