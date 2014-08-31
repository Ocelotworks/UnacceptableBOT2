package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.variable.Level;

public class CommandTwat extends Command {

	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args, PircBotX bot) {
		boolean toBool = false;
		try {
			toBool = Boolean.parseBoolean(args[1]);
		} catch (Exception e) {
			bot.sendIRC()
					.message(
							channel.getName(),
							sender.getNick()
									+ ": Trying to switch twat mode, and you being a twat. *Makes tutting sounds**");
		}
		
		if(toBool != UnacceptableBot.twatMode){
			UnacceptableBot.twatMode = toBool;
			bot.sendIRC().message(channel.getName(),"Twat mode: " + toBool);
		} else {
			bot.sendIRC().message(channel.getName(),"Twat mode: " + toBool);	
		}
	}

	@Override
	public String[] getAliases() {
		// TODO Auto-generated method stub
		return new String[] { "twat" };
	}

	@Override
	public Level getAccessLevel() {
		return Level.ADMIN;
	}

	@Override
	public String getHelp() {
		return "System command";
	}

}
