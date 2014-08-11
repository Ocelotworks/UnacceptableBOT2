package com.unacceptableuse.unacceptablebot.command;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class CommandLive extends Command {

	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args, PircBotX bot) {
		
		String result = null;
		
		bot.sendIRC().message(channel.getName(), "Checking live status...");
		String liveUsername = message.substring(6);
		
		try
		{
			@SuppressWarnings("unused")
			int i = 0;
			
			URL url = null;
			
			url = new URL("https://api.twitch.tv/kraken/streams/" + args[1]);
			
			HttpURLConnection conn = null;
			
			conn = (HttpURLConnection) url.openConnection();
			
			conn.setDoInput(true);
			conn.setDoOutput(false);
			conn.setUseCaches(false);
			conn.setRequestMethod("GET");
			//conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/vnd.twitchtv.v3+json");
			
			//System.out.println(conn.getResponseCode());
			//System.out.println(conn.getResponseMessage());

			// open the stream and put it into BufferedReader
			BufferedReader br = null;
			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder stb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null)
			{
				stb.append(line);
			}
			
			try
			{
				JsonElement JElement = new com.google.gson.JsonParser().parse(stb.toString());
			    JsonObject  JObject = JElement.getAsJsonObject();
			    JObject = JObject.getAsJsonObject("stream");
			    result = JObject.toString();
			} catch (ClassCastException e)
			{
				bot.sendIRC().message(channel.getName(), liveUsername + " is offline.");
			}
			if(result != null)
			{
				bot.sendIRC().message(channel.getName(), liveUsername + " is live! http://twitch.tv/" + liveUsername);				
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		
	}

	@Override
	public String[] getAliases() {
		return new String[]{"live"};
	}
	
	@Override
	public int requiredArguments()
	{
		return 1;
	}

}
