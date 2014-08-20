package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class CommandListChans extends Command{

	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args, PircBotX bot) {
		
		bot.sendIRC().message(channel.getName(), "Channels:");
		for(int i = 0; i < UnacceptableBot.getChannels().size(); i++){
			bot.sendIRC().message(channel.getName(), UnacceptableBot.getChannels().get(i));
		}
		
	}

	@Override
	public String[] getAliases() {
		return new String[]{"listchan", "channels", "chan"};
	}

}
