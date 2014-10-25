package com.unacceptableuse.unacceptablebot.command;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 *
 * @author Joel
 *
 */
public class CommandLive extends Command
{

	@Override
	public String[] getAliases()
	{
		return new String[] { "live" };
	}

	@Override
	public String getHelp()
	{
		return "Usage: live <twitch user>  | Result: Checks if the specified twitch streamer is live ";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{

		String result = null;

		sendMessage("Checking live status...", channel);
		final String liveUsername = message.substring(6);

		try
		{
			URL url = null;

			url = new URL("https://api.twitch.tv/kraken/streams/" + args[1]);

			HttpURLConnection conn = null;

			conn = (HttpURLConnection) url.openConnection();

			conn.setDoInput(true);
			conn.setDoOutput(false);
			conn.setUseCaches(false);
			conn.setRequestMethod("GET");
			// conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/vnd.twitchtv.v3+json");

			// System.out.println(conn.getResponseCode());
			// System.out.println(conn.getResponseMessage());

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
				JsonObject JObject = JElement.getAsJsonObject();
				JObject = JObject.getAsJsonObject("stream");
				result = JObject.toString();
			} catch (final ClassCastException e)
			{
				sendMessage(liveUsername + " is offline.", channel);
			}
			if (result != null)
				sendMessage(liveUsername + " is live! http://twitch.tv/" + liveUsername, channel);
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
