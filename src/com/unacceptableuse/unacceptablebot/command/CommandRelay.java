package com.unacceptableuse.unacceptablebot.command;

import java.util.ArrayList;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.variable.Level;

/**
 * 
 * @author Edward
 *
 */
public class CommandRelay extends Command {
	@Override
	public void performCommand(User sender, Channel channel, String message, String[] args) {
		String from = args[1].toLowerCase();
		String to = args[2].toLowerCase();
		
		if(to.equals(from)) {
			return;
		}
		
		if(args[0].equalsIgnoreCase("!startrelay")) {
			if(!UnacceptableBot.relay.containsKey(from)) {
				UnacceptableBot.relay.put(from, new ArrayList<String>());
			}

			if(!UnacceptableBot.relay.get(from).contains(to)) {
				UnacceptableBot.relay.get(from).add(to);
				sendMessage("Now relaying from " + from + ".", to);
				sendMessage("Now relaying to " + to + ".", to);
			}
		} else if(args[0].equalsIgnoreCase("!stoprelay")) {
			if(UnacceptableBot.relay.containsKey(from)) {
				UnacceptableBot.relay.get(from).remove(to);
				sendMessage("No longer relaying from " + from + ".", to);
				sendMessage("No longer relaying to " + to + ".", to);
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
