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
		
		sendMessage("Channels:", channel);
		for(int i = 0; i < UnacceptableBot.getChannels().size(); i++){
			sendMessage(UnacceptableBot.getChannels().get(i), channel);
		}
		
	}

	@Override
	public String[] getAliases() {
		return new String[]{"listchan", "channels", "chan"};
	}

	@Override
	public Level getAccessLevel() {
		return Level.ADMIN;
	}

	@Override
	public String getHelp() {
		return "System command";
	}

}
