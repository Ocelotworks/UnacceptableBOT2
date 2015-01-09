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
		return UnacceptableBot.getBot().getNick();
	}
}
