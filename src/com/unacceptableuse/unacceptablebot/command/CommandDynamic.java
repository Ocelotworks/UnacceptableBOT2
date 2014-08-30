package com.unacceptableuse.unacceptablebot.command;

import java.util.Map.Entry;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.variable.Level;

public class CommandDynamic extends Command
{
	
	private String alias, format;
	
	public CommandDynamic(String alias, String format)
	{
		this.alias = alias;
		this.format = format;
	}

	

	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args, PircBotX bot)
	{
		JsonParser parser = new JsonParser();
		
		JsonArray ja = parser.parse(format).getAsJsonArray();
		
		//[{text:"This is an example of a text thing"}, {text:"You can add two together too", performCommand:"!image this could probably be done better"}]
		
		for(JsonElement je : ja)
		{
			JsonObject jo = je.getAsJsonObject();
			for(Entry e : jo.entrySet())
			{
				switch(e.getKey().toString())
				{
					case "text": sendMessage(bot, e.getValue().toString(), channel); break;
					case "performCommand": UnacceptableBot.getCommandHandler().getCommand(e.getValue().toString().split(" ")[0].replaceFirst("!","")).performCommand(sender, channel,e.getValue().toString(),e.getValue().toString().split(" "), bot); break;
					case "args": sendMessage(bot, args[Integer.parseInt(e.getValue().toString())] , channel); break;
					case "choose": JsonArray cja = parser.parse(e.getValue().toString()).getAsJsonArray(); cja.get(UnacceptableBot.rand.nextInt(cja.size()));
					default: sendMessage(bot, "&REDUnknown value "+e.getKey().toString(), channel); break;
				};
			}
		}
		
		
	}

	@Override
	public String[] getAliases()
	{
		return new String[]{alias};
	}

	@Override
	public Level getAccessLevel()
	{
		return Level.USER;
	}

}
