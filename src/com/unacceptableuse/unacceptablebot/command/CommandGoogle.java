package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.variable.Level;

/**
 * 
 * @author Joel?
 *
 */
public class CommandGoogle extends Command
{

	@Override
	public void performCommand(User sender, Channel channel, String message, String[] args)
	{
		String add = "http://www.lmgtfy.com/?q=";
		for(int i = 1; i < args.length; i++)
		{
			add += args[i] + "+";
		}
		add = add.substring(0, add.length() - 1); //Wouldn't it be better to use StringBuilder here? - Peter
		sendMessage(add, channel);
	}

	@Override
	public String[] getAliases()
	{
		return new String[]{"google", "lmgtfy"};
	}
	
	@Override
	public int requiredArguments()
	{
		return 1;
	}

	@Override
	public Level getAccessLevel() {
		return Level.USER;
	}

	@Override
	public String getHelp() {
		return "Usage: google <terms> | Result: Produces a LMGTFY link using the specified terms";
	}

}
