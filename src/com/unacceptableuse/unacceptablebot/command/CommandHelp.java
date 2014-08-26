package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.variable.Level;

/**
 * 
 * @author Neil?
 *
 */
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

	@Override
	public Level getAccessLevel() {
		return Level.USER;
	}

}
