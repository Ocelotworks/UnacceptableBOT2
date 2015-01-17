package com.unacceptableuse.unacceptablebot;

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
		return UnacceptableBot.getBot().isConnected() ? UnacceptableBot.getBot().getNick() : "NOTCONNECTED";
	}
	
	
	public static String capitaliseFirstLetter(String input)
	{
		return input.substring(0, 1).toUpperCase() + input.substring(1);
	}
	
	public static String compareDate(long milliseconds1, long milliseconds2) {
		StringBuilder stb = new StringBuilder();
		
		long diff = milliseconds2 - milliseconds1;
		long diffDays = diff / (24 * 60 * 60 * 1000);
		if(diffDays > 0) {
			stb.append(diffDays + " days, ");
			diff = diff - diffDays * (24 * 60 * 60 * 1000);
		}
		
		long diffHours = diff / (60 * 60 * 1000);
		if(diffHours > 0) {
			stb.append(diffHours + " hours, ");
			diff = diff - diffHours * (60 * 60 * 1000);
		}
		
		long diffMinutes = diff / (60 * 1000);
		if(diffMinutes > 0) {
			stb.append(diffMinutes + " minutes, ");
			diff = diff - diffMinutes * (60 * 1000);
		}
		
		long diffSeconds = diff / 1000;
		if(diffSeconds > 0) {
			stb.append(diffSeconds + " seconds, ");
			diff = diff - diffSeconds * 1000;
		}
		
		if(diff > 0) {
			stb.append(diff + " milliseconds.");
		}

		if (stb.charAt(stb.length() - 2) == ',') {
			stb.setCharAt(stb.length() - 2, '.');
			stb.deleteCharAt(stb.length() - 1);
		}
		
		return stb.toString();
	}
}
