package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.variable.Level;

/**
 * 
 * @author Neil
 *
 */
public class CommandHelp extends Command {

	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args, PircBotX bot) {
		sendMessage("Help:", channel);

		for (int i = 0; i < UnacceptableBot.getCommandHandler().getCommands()
				.size(); i++) {

			// Message: command : command help

			if (!UnacceptableBot.getCommandHandler().getCommands().get(i)
					.getHelp().toLowerCase().equals("system command")
					|| !UnacceptableBot.getCommandHandler().getCommands()
							.get(i).getHelp().toLowerCase()
							.equals("unfinished")
					|| !UnacceptableBot.getCommandHandler().getCommands()
							.get(i).getHelp().toLowerCase().equals("")) {

				sendMessage("&GREEN"
						+ UnacceptableBot.getCommandHandler().getCommands()
								.get(i).getAliases()[0]
						+ " &RESET - &RED"
						+ UnacceptableBot.getCommandHandler().getCommands()
								.get(i).getHelp(), channel);
			}
		}

	}

	@Override
	public String[] getAliases() {
		return new String[] { "help", "?" };
	}

	@Override
	public Level getAccessLevel() {
		return Level.USER;
	}

	@Override
	public String getHelp() {
		return "";
	}

}
