package com.unacceptableuse.unacceptablebot.threading;

import java.util.TimerTask;

import org.pircbotx.PircBotX;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class SnapchatThread extends TimerTask
{

	public PircBotX bot = UnacceptableBot.getBot();

	public void run()
	{
		UnacceptableBot.getSnapchat().doOneInQueue();
		// UnacceptableBot.log("INFO", "SNAPCHAT", "Trying queue!"); Sorry Peter:)
	}

}
