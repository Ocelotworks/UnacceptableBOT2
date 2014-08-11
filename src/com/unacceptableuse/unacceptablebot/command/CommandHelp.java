package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

public class CommandHelp extends Command {

	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args, PircBotX bot) {
		bot.sendIRC().notice(sender.getNick(), "Extensive help documents can be found here: %TODO: INSERT LINK HERE%");
		
	}

	@Override
	public String[] getAliases() {
		return new String[]{"help", "?"};
	}

}
