package com.unacceptableuse.unacceptablebot.command;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 *
 * @author Joel
 *
 */
public class CommandDefine extends Command
{

	@Override
	public String[] getAliases()
	{
		return new String[] { "define" };
	}

	@Override
	public String getHelp()
	{
		return "Usage: define <word> | Result: Returns the lookup for the word ";
	}

	// http://api.wordnik.com:80/v4/word.json/hello/definitions?limit=1&includeRelated=false&sourceDictionaries=webster&useCanonical=false&includeTags=false&api_key=a2a73e7b926c924fad7001ca3111acd55af2ffabf50eb4ae5
	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{
		String definition = null;
		String word = null;
		try
		{
			URL url = null;

			url = new URL("http://api.wordnik.com:80/v4/word.json/" + args[1] + "/definitions?limit=1&includeRelated=false&sourceDictionaries=webster&useCanonical=false&includeTags=false&api_key=a2a73e7b926c924fad7001ca3111acd55af2ffabf50eb4ae5");

			HttpURLConnection conn = null;

			conn = (HttpURLConnection) url.openConnection();

			conn.setDoInput(true);
			conn.setDoOutput(false);
			conn.setUseCaches(false);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

			// System.out.println(conn.getResponseCode());
			System.out.println(conn.getResponseMessage());

			// open the stream and put it into BufferedReader
			BufferedReader br = null;
			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			final StringBuilder stb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null)
				stb.append(line);

			try
			{
				final JsonElement JElement = new com.google.gson.JsonParser().parse(stb.toString());
				final JsonArray array = JElement.getAsJsonArray();
				final JsonObject obj = array.get(0).getAsJsonObject();
				definition = obj.get("text").getAsString();
				word = obj.get("word").getAsString();
			} catch (final ClassCastException e)
			{
				sendMessage(word + " not found.", channel);
			}

			if (definition != null)
				sendMessage(word + ": " + definition, channel);
		} catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public int requiredArguments()
	{
		return 1;
	}
}
