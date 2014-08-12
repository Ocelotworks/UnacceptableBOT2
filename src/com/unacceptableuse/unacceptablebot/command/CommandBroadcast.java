package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class CommandBroadcast extends Command{

	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args, PircBotX bot) {
		
		for(String s : UnacceptableBot.getChannels())
			bot.sendIRC().message(s, message.replace("!broadcast ", "BROADCAST FROM "+sender+": "));
		
	}

	@Override
	public String[] getAliases() {
		return new String[]{"broadcast"};
	}

	@Override
	public int requiredArguments()
	{
		return 1;
	}
}
