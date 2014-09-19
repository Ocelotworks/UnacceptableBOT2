package com.unacceptableuse.unacceptablebot.threading;

import java.util.TimerTask;

import org.pircbotx.PircBotX;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class SnapchatThread extends TimerTask {

	public PircBotX bot =  UnacceptableBot.getBot();

	public void run() {
		UnacceptableBot.getSnapchat().doOneInQueue(bot, UnacceptableBot.getChannels().get(0));
	}

}
