package com.unacceptableuse.unacceptablebot.command;

import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class CommandSoundcloud extends Command
{

	@Override
	public String[] getAliases()
	{
		return new String[] { "soundcloud", "sc" };
	}

	@Override
	public String getHelp()
	{
		return "todo";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{
		try{
			final int index = message.contains("--index=") ? Integer.parseInt(message.split("index=")[1].split(" ")[0]) : 0;
			final String query = message.replace(args[0] + " ", "");
	
			final JsonArray je = UnacceptableBot.getParser().parse(new InputStreamReader(UnacceptableBot.getUrlContents("https://api.soundcloud.com/tracks.json?q=" + URLEncoder.encode(query, "UTF-8") + "&client_id=" + UnacceptableBot.getConfigHandler().getPassword("soundcloud_client")))).getAsJsonArray();
			final JsonObject jo = je.get(index).getAsJsonObject();
	
			sendMessage(jo.get("title").getAsString() + ": " + jo.get("permalink_url").getAsString(), channel);
		}catch(Exception e)
		{
			sendMessage("An error occurred proccessing your search: "+e.toString(), channel);
		}

	}

}
