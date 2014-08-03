package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

public class CommandFillMeIn extends Command {

	@Override
	public void performCommand(User sender, Channel channel, String message, String[] args, PircBotX bot) {
		
		
	}

	@Override
	public String[] getAliases() {
		return new String[]{"fillmein","fmi"};
	}

}
