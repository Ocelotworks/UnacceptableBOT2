package com.unacceptableuse.unacceptablebot.command;

import java.util.Random;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.variable.Level;

/**
 * 
 * @author Peter
 *
 */
public class CommandFucksGiven extends Command {

	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args, PircBotX bot) {
		bot.sendIRC()
				.message(
						channel.getName(),
						"Fucks given: &BOLD"
								+ (((int) Math.signum(new Random().nextInt())) - (sender
										.getNick().length() * channel.getName()
										.length())));

	}

	@Override
	public String[] getAliases() {
		return new String[] { "fucksgiven" };
	}

	@Override
	public Level getAccessLevel() {
		return Level.USER;
	}

}
