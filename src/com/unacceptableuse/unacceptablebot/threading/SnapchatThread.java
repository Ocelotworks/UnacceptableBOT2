package com.unacceptableuse.unacceptablebot.threading;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimerTask;

import org.pircbotx.PircBotX;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class SnapchatThread extends TimerTask {

	PircBotX bot = null;

	public void run() {
		// brokenGetSnaps();
		UnacceptableBot.getSnapchat().doOneInQueue(bot, UnacceptableBot.getChannels().get(1));
	}

	private void brokenGetSnaps() {
		Calendar calendar = Calendar.getInstance();
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		int minutes = calendar.get(Calendar.MINUTE);
		int date = calendar.get(Calendar.DATE);
		int month = calendar.get(Calendar.MONTH);
		try {
			byte[] image = UnacceptableBot.getSnapchat().getSnaps();
			if (image[0] != 0) {
				FileOutputStream imageOut = new FileOutputStream(("snap-"
						+ month + date + hours + minutes));
				imageOut.write(image);
				imageOut.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
