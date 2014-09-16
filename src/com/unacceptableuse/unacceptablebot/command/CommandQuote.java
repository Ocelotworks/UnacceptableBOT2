package com.unacceptableuse.unacceptablebot.command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.pircbotx.Channel;
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
	public void performCommand(User sender, Channel channel, String message,String[] args) {

		int count = 1;

		String quoteChannel = channel.getName();
		
		for (int i = 0; i < args.length; i++) {
			if (args[i].toLowerCase().contains("--count")
					|| args[i].toLowerCase().contains("-c")) {
				try {
					count = Integer.parseInt(args[i + 1]);
				} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
							sendMessage("Something went wrong here", channel);
							UnacceptableBot.log("ERROR", "!quote", e.toString());
					e.printStackTrace();
				}
			} else if (args[i].toLowerCase().contains("--channel")
					|| args[i].toLowerCase().contains("-ch")) {
				quoteChannel = args[i + 1].startsWith("#") ? args[i + 1] : "#" + args[i + 1];
				/*
				 * Or does channel.getName() not include the '#'?
				 * In which case, the line should be:
				 * quoteChannel = args[i + 1].replaceFirst(Pattern.quote("#"), "");
				 */
			}
		}

		try {
			for (int i = 0; i < count; i++) {
				PreparedStatement ps = UnacceptableBot.getConfigHandler().sql
						.getPreparedStatement("SELECT Message FROM `teknogeek_unacceptablebot`.`"
								+ quoteChannel
								+ "` WHERE Username = '"
								+ args[1].replace(" ", "") // Is the replace needed, since args = message.split(" ")? -eduardog3000
								+ "' ORDER BY RAND() LIMIT " + count);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					sendMessage("<" + args[1] + "> " + rs.getString(1), channel);
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
		return "Usage: quote <user> [-c <number> | --count <number>] [-ch <channel> | --channel <channel>]";
	}

}