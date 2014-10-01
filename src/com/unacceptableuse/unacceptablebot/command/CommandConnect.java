package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.variable.Level;

/**
 * 
 * @author Peter
 *
 */
public class CommandConnect extends Command
{

	@Override
	public void performCommand(User sender, Channel channel, String message,String[] args)
	{
		UnacceptableBot.getBot().sendIRC().joinChannel(args[1]);
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

}
