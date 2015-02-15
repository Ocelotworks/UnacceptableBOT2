package com.unacceptableuse.unacceptablebot.command;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.variable.Level;

public class CommandTopic extends Command
{

	@Override
	public Level getAccessLevel()
	{
		return Level.TRUSTED;
	}

	@Override
	public String[] getAliases()
	{
		return new String[] { "topic" };
	}

	@Override
	public String getHelp()
	{
		return "Adds the previous quote to the random topic list";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{
		try
		{
			final ResultSet rs = UnacceptableBot.getConfigHandler().sql.query("SELECT Username,Message FROM `stevie`.`" + channel.getName() + "` WHERE ID=(SELECT MAX(ID)"+(args.length > 1 ? "-"+Integer.parseInt(args[1]): "")+" FROM `stevie`.`" + channel.getName() + "`)");
			rs.next();
			final String newTopic = "<" + rs.getString(1) + "> " + rs.getString(2);
			
			if(newTopic.contains("!topic"))
			{
				sendMessage("Ignoring Duplicate request!", channel);
			}else
			{
				final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("sexquotes.txt", true)));
				out.println(newTopic);
				out.close();

				UnacceptableBot.sexQuotes.add(newTopic);

				sendMessage("Added topic " + newTopic, channel);
			}

		}catch(SQLException e)
		{
			sendMessage("Unable to add quote:"+e.toString(), channel);		
		}catch(NumberFormatException e)
		{
			sendMessage("Unable to add quote: Invalid number", channel);
		}catch (final Exception e)
		{
			sendMessage("Unable to add quote: " + e.toString(), channel);
			e.printStackTrace();
		}

	}

}
