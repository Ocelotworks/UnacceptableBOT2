package com.unacceptableuse.unacceptablebot.command;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.variable.Level;

public class CommandTopic extends Command
{

	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args)
	{
		try
		{
			ResultSet rs = UnacceptableBot.getConfigHandler().sql.query("SELECT MAX(ID)-1 FROM `teknogeek_unacceptablebot`.`"+channel.getName()+"`");
			rs.next();
			String newTopic = "<"+rs.getString(3)+"> "+rs.getString(4);
			
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("myfile.txt", true)));
			out.println(newTopic);
			out.close();
			
			
		} catch (Exception e)
		{
			sendMessage("Unable to add quote: "+e.toString(), channel);
			e.printStackTrace();
		}
		
	}

	@Override
	public String[] getAliases()
	{
		return new String[]{"topic"};
	}

	@Override
	public Level getAccessLevel()
	{
		return Level.TRUSTED;
	}

	@Override
	public String getHelp()
	{
		return "Adds the previous quote to the random topic list";
	}

}
