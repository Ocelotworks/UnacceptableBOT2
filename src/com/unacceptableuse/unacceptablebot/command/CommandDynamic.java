package com.unacceptableuse.unacceptablebot.command;

import java.io.StringReader;
import java.util.Map.Entry;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class CommandDynamic extends Command
{
	
	private String alias, format;
	
	public CommandDynamic(String alias, String format)
	{
		this.alias = alias;
		this.format = format;
	}

	

	@SuppressWarnings("rawtypes")
	@Override
	public void performCommand(User sender, Channel channel, String message,String[] args)
	{
		JsonParser parser = new JsonParser();
		
		JsonReader jr = new JsonReader(new StringReader(format));
		jr.setLenient(true);
		JsonArray ja  = parser.parse(jr).getAsJsonArray();
		
		
		
		//[{text:"This is an example of a text thing"}, {text:"You can add two together too", performCommand:"!image this could probably be done better"}]
		
		for(JsonElement je : ja)
		{
			JsonObject jo = je.getAsJsonObject();
			for(Entry e : jo.entrySet())
			{
				switch(e.getKey().toString())
				{
					case "text":
					{
						sendMessage(e.getValue().toString(), channel); break;
					}
					
					case "performCommand":
					{
						UnacceptableBot.getCommandHandler().getCommand(e.getValue().toString().split(" ")[0].replaceFirst("!", "")).performCommand(sender, channel, e.getValue().toString(), e.getValue().toString().split(" ")); break;
					}
					
					case "args":
					{
						sendMessage(args[Integer.parseInt(e.getValue().toString())] , channel); break;
					}
					
					case "choose":
					{
						JsonArray cja = parser.parse(e.getValue().toString()).getAsJsonArray(); cja.get(UnacceptableBot.rand.nextInt(cja.size()));
					}
					
					default:
					{
						sendMessage("&REDUnknown value " + e.getKey().toString(), channel); break;
					}
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
	public String getHelp() {
		return "System command";
	}

}
