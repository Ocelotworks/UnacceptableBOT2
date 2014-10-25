package com.unacceptableuse.unacceptablebot.command;

import java.io.InputStreamReader;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class CommandSoundcloud extends Command
{

	@Override
	public void performCommand(User sender, Channel channel, String message, String[] args)
	{
		int index = message.contains("--index=") ? Integer.parseInt(message.split("index=")[1].split(" ")[0]) : 0;
		String query = message.replace(args[0] + " ", "");

		JsonArray je = UnacceptableBot.getParser().parse(new InputStreamReader(UnacceptableBot.getUrlContents("https://api.soundcloud.com/tracks.json?q=" + query + "&client_id=" + UnacceptableBot.getConfigHandler().getPassword("soundcloud_client")))).getAsJsonArray();
		JsonObject jo = je.get(index).getAsJsonObject();

		sendMessage(jo.get("title").getAsString() + ": " + jo.get("permalink_url").getAsString(), channel);

	}

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

}
