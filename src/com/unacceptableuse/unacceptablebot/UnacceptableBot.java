package com.unacceptableuse.unacceptablebot;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.InviteEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import com.unacceptableuse.unacceptablebot.handler.CommandHandler;
import com.unacceptableuse.unacceptablebot.handler.ConfigHandler;

@SuppressWarnings("rawtypes")
public class UnacceptableBot extends ListenerAdapter {

	private static CommandHandler handler = new CommandHandler();
	private static ConfigHandler config = new ConfigHandler();
	public static Random rand = new Random();

	/**
	 * Starts the init process of everything
	 * @author UnacceptableUse
	 */
	public UnacceptableBot() {
		handler.init();
		config.init();

		config.increment("stat:startups");

	}

	@Override
	public void onMessage(final MessageEvent event) throws Exception {
		recordMessage(event);
		if (event.getMessage().startsWith("!")) {
			//String channel = event.getChannel().getName();
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

	@Override
	public void onInvite(final InviteEvent event) {
		System.out.println(event.getUser() + " invited bot to "
				+ event.getChannel());
		event.getBot().sendIRC().joinChannel(event.getChannel());
	}

	private void recordMessage(final MessageEvent event) throws SQLException {
		String message = event.getMessage();
		Channel channel = event.getChannel();
		User sender = event.getUser();
		PircBotX bot = event.getBot();
		if(bot.getNick() == ""){} //Here to tidy up the error console. Please ignore 
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new java.util.Date());
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		int minutes = calendar.get(Calendar.MINUTE);
		int date = calendar.get(Calendar.DATE);
		int month = calendar.get(Calendar.MONTH);
		final String[] months = new String[] { "Jan", "Feb", "Mar", "Apr", "May",
				"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
				"WHAT ARE YOU, A FUCKING WIZARD?!" };
		String stringMonth = months[month];
		String dateTime = date + " " + stringMonth + ", " + hours + ":"
				+ minutes;
		config.createChannelTable(channel.getName());
		config.setLog(dateTime, sender.getNick(), message, channel.getName());
	}

	public static InputStream getUrlContents(String surl) {
		URL url;

		try {
			url = new URL(surl);
			URLConnection conn = url.openConnection();
			InputStream is = conn.getInputStream();

			return is;
		} catch (Exception e) {
			System.err.println("Could not connect: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public static InputStream getHTTPSUrlContents(String surl) {
		URL url;

		try {
			url = new URL(surl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			InputStream is = conn.getInputStream();

			return is;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static CommandHandler getCommandHandler() {
		return handler;
	}

	public static ConfigHandler getConfigHandler() {
		return config;
	}
}
