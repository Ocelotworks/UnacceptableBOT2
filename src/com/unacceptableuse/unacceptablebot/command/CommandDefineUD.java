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
	public String[] getAliases()
	{
		return new String[] { "defineud" };
	}

	@Override
	public String getHelp()
	{
		return "Usage: defineud <word> | Result: Returns the Urban Dictionary definition of the word.";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{
		InputStream is = null;
		final String word = message.replace(args[0], "").trim();
		try
		{
			final URL url = new URL("http://api.urbandictionary.com/v0/define?term=" + word.replace(" ", "%20"));

			final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			is = conn.getInputStream();

			final JsonParser json = new JsonParser();
			final String definition = json.parse(new InputStreamReader(is)).getAsJsonObject().get("list").getAsJsonArray().get(0).getAsJsonObject().get("definition").getAsString().replace(" \r", "").replace("\n", "");

			sendMessage(word + ": " + definition, channel);
		} catch (final Exception e2)
		{
			sendMessage("An unknown exception occurred.", channel); // Probably needs to be more descriptive.
		}
	}

	@Override
	public int requiredArguments()
	{
		return 1;
	}
}
