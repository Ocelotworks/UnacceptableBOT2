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
		try{
			PreparedStatement ps = UnacceptableBot.getConfigHandler().sql.getPreparedStatement("SELECT Message FROM `teknogeek_unacceptablebot`.`?` WHERE Username = '?' ORDER BY RAND() LIMIT 1");
			ps.setString(1, args.length == 3 ? args[2].startsWith("#") ? args[2] : "#" + args[2] : channel.getName());
			ps.setString(2, args[1]);
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
	
	@Override
	public int requiredArguments()
	{
		return 1;
	}

}
