package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

public class CommandGoogle extends Command {

	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args, PircBotX bot) {
		String add = "www.lmgtfy.com/?q=";
		for(int i = 1; i<args.length - 1; i++){
			add = add + args[i];
		}
		bot.sendIRC().message(channel.getName(), add);
		
	}

	@Override
	public String[] getAliases() {
		return new String[]{"google", "lmgtfy"};
	}
	
	@Override
	public int requiredArguments()
	{
		return 1;
	}

}
