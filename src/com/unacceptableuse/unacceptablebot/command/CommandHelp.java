package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

/**
 * 
 * @author Neil
 *
 */
public class CommandHelp extends Command {

	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args) {
		//I hate you all <3
		sendMessage("Peter has shit to fix",channel);
	}

	@Override
	public String[] getAliases() {
		return new String[] { "help", "?" };
	}


}
