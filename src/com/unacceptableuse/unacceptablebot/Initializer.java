package com.unacceptableuse.unacceptablebot;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;

public class Initializer
{
	@SuppressWarnings({ "unchecked", "rawtypes" })
	
	public Initializer(String username)
	{	
		System.out.println("Loading config...");
		try
		{
			InputStream is = new FileInputStream("static.properties");
			Properties props = new Properties();
			props.load(is);
			
			Configuration config = new Configuration.Builder()
			.setName(username)
			.setLogin(username)
			.setAutoNickChange(true)
			.addListener(new UnacceptableBot())
			.setServerHostname(props.getProperty("server"))
			.setServerPort(Integer.parseInt(props.getProperty("port")))
			.setServerPassword(props.getProperty("password"))
			.addAutoJoinChannel(props.getProperty("default_channel"))
			.buildConfiguration();
			
			UnacceptableBot.channels.add(props.getProperty("default_channel"));
			UnacceptableBot.nickAuth(username, props.getProperty("nickserv_password"));
			props.clear();
			is.close();
			 
			PircBotX bot = new PircBotX(config);
			UnacceptableBot.setBot(bot);
			bot.startBot();
			
			
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args) throws Exception
	{
		
		if(System.getProperty("os.name").contains("Windows"))
			System.err.println("WARNING: This is designed to run on Linux and certain features may not work on Windows!");

		new Initializer("StevieBOT");
	}
}
