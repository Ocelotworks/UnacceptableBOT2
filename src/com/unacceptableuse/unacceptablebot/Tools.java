package com.unacceptableuse.unacceptablebot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.pircbotx.Channel;

public class Tools
{
	/**
	 * Gets a currently connected {@link Channel} from supplied channel name
	 * @param channel name
	 * @return {@link Channel} instance of currently connected channel, null if not found
	 */
	public static Channel getChannel(String channel)
	{
		for(Channel chan : UnacceptableBot.getBot().getUserBot().getChannels())
			if(chan.getName().equalsIgnoreCase(channel))
				return chan;
		return null;
	}
	
	
	public static String getBotUsername()
	{
		return UnacceptableBot.getBot().getNick();
	}
	
	
	public static String capitaliseFirstLetter(String input)
	{
		return input.substring(0, 1).toUpperCase() + input.substring(1);
	}
	
	public static String compareDate(long milliseconds1, long milliseconds2){
	    long diff = milliseconds2 - milliseconds1;
	    long diffSeconds = diff / 1000;
	    long diffMinutes = diff / (60 * 1000);
	    long diffHours = diff / (60 * 60 * 1000);
	    long diffDays = diff / (24 * 60 * 60 * 1000);
	    StringBuilder stb = new StringBuilder();
	    if(diffDays > 0)stb.append(diffDays+" days,");
	    if(diffHours > 0)stb.append(diffHours+" hours,");
	    if(diffMinutes > 0)stb.append(diffMinutes+" minutes,");
	    if(diffSeconds > 0)stb.append(diffSeconds+" seconds.");
	    return stb.toString();
	}
	
}
