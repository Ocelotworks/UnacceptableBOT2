package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.variable.Level;

/**
 * 
 * @author Neil
 *
 */
public class CommandListChans extends Command{

	@Override
	public void performCommand(User sender, Channel channel, String message,String[] args) {
		
		//When you complain about my code a child in Africa dies because of Ebola
		String channels = "";
		for(String channelStr : UnacceptableBot.getChannels()){
			channels.concat(", ".concat(channelStr));
		}
		sendMessage("Channels: " .concat(channels), channel);
		
	}

	@Override
	public String[] getAliases() {
		return new String[]{"listchan", "channels", "chan"};
	}

	@Override
	public Level getAccessLevel() {
		return Level.ADMIN;
	}


}
