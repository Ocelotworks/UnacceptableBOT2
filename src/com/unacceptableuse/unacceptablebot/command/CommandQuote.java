package com.unacceptableuse.unacceptablebot.command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
public class CommandQuote extends Command {

	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args, PircBotX bot) {

		int count = 1;

		for (int i = 0; i < args.length; i++) {
			if (args[i].toLowerCase().contains("--count")
					|| args[i].toLowerCase().contains("-c")) {
				try {
					count = Integer.parseInt(args[i + 1]);
				} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
					bot.sendIRC().message(channel.getName(),
							"Something went wrong here");
					e.printStackTrace();
				}
			}
		}

		try {
			PreparedStatement idPS = UnacceptableBot.getConfigHandler().sql
					.getPreparedStatement("SELECT ID FROM `teknogeek_unacceptablebot`.`"
							+ channel.getName() + "WHERE Username = '" + args[1].replace(" ", "") + " ORDER BY RAND() LIMIT 1");
		} catch (Exception e) {
		}

		try {
			for (int i = 0; i < count; i++) {
				PreparedStatement ps = UnacceptableBot.getConfigHandler().sql
						.getPreparedStatement("SELECT Message FROM `teknogeek_unacceptablebot`.`"
								+ channel.getName()
								+ "` WHERE Username = '"
								+ args[1].replace(" ", "")
								+ "' ORDER BY RAND() LIMIT 1");
				/*
				 * ps.setString(1, args.length == 3 ? args[2].startsWith("#") ?
				 * args[2] : "#" + args[2] : channel.getName()); Like, what the
				 * fuck are these even here for? ps.setString(2, args[1]);
				 */
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					bot.sendIRC().message(channel.getName(),
							"<" + args[1] + "> " + rs.getString(1));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String[] getAliases() {
		return new String[] { "quote" };
	}

	@Override
	public int requiredArguments() {
		return 1;
	}

	@Override
	public Level getAccessLevel() {
		return Level.USER;
	}

	@Override
	public String getHelp() {
		return "Usage: quote <user> [-c|--count <number>] | Result: Returns a message or number of messages said by the specifed user";
	}

}
