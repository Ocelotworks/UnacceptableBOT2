package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.variable.Level;

public class CommandTwat extends Command {

	@Override
	public void performCommand(User sender, Channel channel, String message,String[] args) {
		UnacceptableBot.twatMode = !UnacceptableBot.twatMode;
		sendMessage("Twat mode: " + UnacceptableBot.twatMode, channel);
	}

	@Override
	public String[] getAliases() {
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
