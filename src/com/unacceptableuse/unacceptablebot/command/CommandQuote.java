package com.unacceptableuse.unacceptablebot.command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class CommandQuote extends Command {

	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args, PircBotX bot) {
		String user = args[1];
		try{
			PreparedStatement ps = UnacceptableBot.getConfigHandler().sql.getPreparedStatement("SELECT Message FROM `teknogeek_unacceptablebot`.`" + args.length == 3 ? (args[2].startsWith("#") ? args[2] : "#" + args[2]) : channel.getName() + "` WHERE Username = '"+user+"' ORDER BY RAND() LIMIT 1");
			ResultSet rs = ps.executeQuery();
			bot.sendIRC().message(channel.getName(), rs.getString(1));
		} catch(SQLException e){
			e.printStackTrace();
		}
		
	}

	@Override
	public String[] getAliases() {
		return new String[]{"quote"};
	}

}
