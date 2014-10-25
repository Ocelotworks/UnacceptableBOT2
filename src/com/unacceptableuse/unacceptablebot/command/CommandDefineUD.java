package com.unacceptableuse.unacceptablebot.command;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.google.gson.JsonParser;

/**
 * 
 * @author Edward
 * @author Joel
 *
 */
public class CommandDefineUD extends Command
{
	@Override
	public void performCommand(User sender, Channel channel, String message, String[] args)
	{
		InputStream is = null;
		String word = message.replace(args[0], "").trim();
		try
		{
			URL url = new URL("http://api.urbandictionary.com/v0/define?term=" + word.replace(" ", "%20"));

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			is = conn.getInputStream();

			JsonParser json = new JsonParser();
			String definition = json.parse(new InputStreamReader(is)).getAsJsonObject().get("list").getAsJsonArray().get(0).getAsJsonObject().get("definition").getAsString().replace(" \r", "").replace("\n", "");

			sendMessage(word + ": " + definition, channel);
		} catch (Exception e2)
		{
			sendMessage("An unknown exception occurred.", channel); // Probably needs to be more descriptive.
		}
	}

	@Override
	public String[] getAliases()
	{
		return new String[] { "defineud" };
	}

	@Override
	public int requiredArguments()
	{
		return 1;
	}

	@Override
	public String getHelp()
	{
		return "Usage: defineud <word> | Result: Returns the Urban Dictionary definition of the word.";
	}
}
