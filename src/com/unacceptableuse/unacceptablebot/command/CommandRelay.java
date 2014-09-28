package com.unacceptableuse.unacceptablebot.command;

import java.util.ArrayList;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.variable.Level;

public class CommandRelay extends Command {
	@Override
	public void performCommand(User sender, Channel channel, String message, String[] args) {
		String from = args[1];
		String to = args[2];
		
		if(to.equalsIgnoreCase(from)) {
			return;
		}
		
		if(args[0].equalsIgnoreCase("!startrelay")) {
			if(UnacceptableBot.relay.containsKey(from) && !UnacceptableBot.relay.get(from).contains(to)) {
				UnacceptableBot.relay.get(from).add(to);
			} else {
				UnacceptableBot.relay.put(from, new ArrayList<String>());
				UnacceptableBot.relay.get(from).add(to);
			}
		} else if(args[0].equalsIgnoreCase("!stoprelay")) {
			if(UnacceptableBot.relay.containsKey(from)) {
				UnacceptableBot.relay.get(from).remove(to);
			}
		}
	}

	@Override
	public String[] getAliases() {
		return new String[]{ "startrelay", "stoprelay" };
	}
	
	@Override
	public Level getAccessLevel() {
		return Level.ADMIN;
	}

	@Override
	public String getHelp() {
		return "Usage: relay <from> <to> | Will relay all messages from the first channel to the second.";
	}
	
	@Override
	public int requiredArguments() {
		return 2;
	}
}
