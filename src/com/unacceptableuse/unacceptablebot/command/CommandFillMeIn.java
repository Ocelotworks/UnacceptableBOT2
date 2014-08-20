package com.unacceptableuse.unacceptablebot.command;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.handler.ConfigHandler;

/**
 * 
 * @author Neil
 *
 */
public class CommandFillMeIn extends Command {

	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args, PircBotX bot) {
		String id = "";
		try {
			ConfigHandler config = UnacceptableBot.getConfigHandler();
			ResultSet rs = config.logQuery(channel.getName());
			rs.next();
			id = rs.getString(1);
			System.out.println("id");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error from CommandFillMeIn.class. Ranging from line 20 to line 24");
		}
		try{
		for(int i = 0; i < Integer.parseInt(args[1]);i++){
			ResultSet rs = UnacceptableBot.getConfigHandler().getLog(channel.getName(), Integer.parseInt(id)-i);
			rs.next();
			bot.sendIRC().notice(sender.getNick(), "["+rs.getString(1)+"] <"+rs.getString(2)+"> "+rs.getString(3));
		}
		}
		catch (SQLException e){
			e.printStackTrace();
			System.out.println("Error from CommandFillMeIn.class. Ranging from line 29 to line 38");
		}
	}

	@Override
	public String[] getAliases() {
		return new String[] { "fillmein", "fmi" };
	}

	@Override
	public int requiredArguments() {
		return 1;
	}

}
