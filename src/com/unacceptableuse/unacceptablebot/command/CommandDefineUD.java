package com.unacceptableuse.unacceptablebot.command;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import com.google.gson.JsonParser;
import com.unacceptableuse.unacceptablebot.variable.Level;

/**
 * 
 * @author Joel
 *
 */
public class CommandDefineUD extends Command
{

	@SuppressWarnings("unused")
	@Override
	public void performCommand(User sender, Channel channel, String message, String[] args, PircBotX bot) 
	{
		InputStream is;
		String word = args[1];
		try
		{
			URL url = new URL("http://api.urbandictionary.com/v0/define?term=" + args[1]);
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			is =  conn.getInputStream();
		}
		catch(Exception e2)
		{
			e2.printStackTrace();
		}
		
		JsonParser json = new JsonParser();
		//JsonObject udObject = json.parse(new InputStreamReader(is)).getAsJsonObject();
	/*	try
		{
//			bot.sendIRC().message(channel.getName(), channel,
//						String.format("%s: %s",
//						multiString.replace("+", " "),
//						udObject.get("list").getAsJsonArray().get(0).getAsJsonObject().get("definition").toString().replace("\"","")));
		} catch(IndexOutOfBoundsException e3)
		{
			//bot.sendIRC().message(channel.getName(),  word + ": " + definition);
		}*/
	}

	@Override
	public String[] getAliases()
	{
		return new String[]{"define"};
	}

	@Override
	public int requiredArguments()
	{
		return 1;
	}

	@Override
	public Level getAccessLevel() {
		// TODO Auto-generated method stub
		return Level.USER;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "See define";
	}
}
