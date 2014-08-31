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
		UnacceptableBot.twatMode = !UnacceptableBot.twatMode;
		bot.sendIRC().message(channel.getName(), "Twat mode: " + UnacceptableBot.twatMode);
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
