package com.unacceptableuse.unacceptablebot;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
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

public class UnacceptableBot extends ListenerAdapter {
	

	private static CommandHandler handler = new CommandHandler();
	private static ConfigHandler config = new ConfigHandler();
	public static Random rand = new Random();

	public UnacceptableBot() {
		handler.init();
		config.init();
		
		config.increment("stat:startups");
		
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
	
	@Override
	public void onInvite(final InviteEvent event)
	{
		System.out.println(event.getUser()+" invited bot to "+event.getChannel());
		event.getBot().sendIRC().joinChannel(event.getChannel());
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
	
	public static InputStream getUrlContents(String surl)
	{
		URL url;
		 
		try {
			url = new URL(surl);
			URLConnection conn = url.openConnection();
			InputStream is =  conn.getInputStream();
 
			return is;
		} catch (Exception e) {
			System.err.println("Could not connect: "+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	public static InputStream getHTTPSUrlContents(String surl)
	{
		URL url;
		 
		try {
			url = new URL(surl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			InputStream is =  conn.getInputStream();
 
			return is;
 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static CommandHandler getCommandHandler()
	{
		return handler;
	}

	public static ConfigHandler getConfigHandler()
	{
		return config;
	}
}
