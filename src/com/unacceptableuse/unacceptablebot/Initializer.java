package com.unacceptableuse.unacceptablebot;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;

public class Initializer
{
	public static void main(final String[] args) throws Exception
	{

		if (System.getProperty("os.name").contains("Windows"))
			System.err.println("WARNING: This is designed to run on Linux and certain features may not work on Windows!");

		new Initializer("StevieBOT");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Initializer(final String username)
	{
		System.out.println("Loading config...");
		try
		{
			final InputStream is = new FileInputStream("static.properties");
			final Properties props = new Properties();
			props.load(is);

			final Configuration config = 
					new Configuration.Builder()
					.setName(username).setLogin(username)
					.setAutoNickChange(true)
					.addListener(new UnacceptableBot())
					.setServer(props.getProperty("server"), Integer.parseInt(props.getProperty("port")), props.getProperty("password"))
					.addAutoJoinChannel(props.getProperty("default_channel"))
					.buildConfiguration();

			UnacceptableBot.channels.add(props.getProperty("default_channel"));
			// UnacceptableBot.nickAuth(username, props.getProperty("nickserv_password"));
			props.clear();
			is.close();

			final PircBotX bot = new PircBotX(config);
			UnacceptableBot.setBot(bot);
			bot.startBot();

		} catch (final Exception e)
		{
			e.printStackTrace();
		}

	}
}
