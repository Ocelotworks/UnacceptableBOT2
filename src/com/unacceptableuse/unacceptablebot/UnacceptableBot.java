package com.unacceptableuse.unacceptablebot;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import com.unacceptableuse.unacceptablebot.command.CommandSql;
import com.unacceptableuse.unacceptablebot.handler.CommandHandler;
import com.unacceptableuse.unacceptablebot.handler.ConfigHandler;

public class UnacceptableBot extends ListenerAdapter {
	CommandHandler handler = new CommandHandler();
	ConfigHandler config = new ConfigHandler();

	public UnacceptableBot() {
		handler.init();
		config.init();
	}

	@Override
	public void onMessage(final MessageEvent event) throws Exception {
		recordMessage(event);
		if (event.getMessage().startsWith("!")) {
			String channel = event.getChannel().getName();
			handler.processMessage(event);
		}
	}

	@Override
	public void onPrivateMessage(final PrivateMessageEvent event)
			throws Exception {
		// if(event.getMessage().startsWith("!"))
		// {
		// String user = event.getUser().getNick();
		// handler.processMessage(event);
		// }
	}

	private void recordMessage(final MessageEvent event) {
		String message = event.getMessage();
		Channel channel = event.getChannel();
		User sender = event.getUser();
		PircBotX bot = event.getBot();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new java.util.Date());
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		int minutes = calendar.get(Calendar.MINUTE);
		int date = calendar.get(Calendar.DATE);
		int month = calendar.get(Calendar.MONTH);
		String[] months = new String[] { "Jan", "Feb", "Mar", "Apr", "May",
				"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
		String stringMonth = months[month];
		String dateTime = date + " " + stringMonth + ", " + hours + ":"
				+ minutes;

		try {
			PrintWriter out = new PrintWriter(new FileWriter(channel.getName()
					+ ".ub2log", true));
			out.write("[" + dateTime + "] <" + sender.getNick() + "> " + message + "\n");
			out.close();
			//System.out.println("Writing log");
		} catch (IOException e) {
			bot.sendIRC().message(channel.getName(),
					"Something just fucked up....");
			e.printStackTrace();
		}

	}
}
