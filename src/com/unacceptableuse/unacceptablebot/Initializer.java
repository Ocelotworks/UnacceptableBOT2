package com.unacceptableuse.unacceptablebot;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;

import com.unacceptableuse.unacceptablebot.handler.CommandHandler;

public class Initializer
{
	@SuppressWarnings({ "unchecked", "rawtypes" })
	
	public Initializer(String username)
	{	
		Configuration config = new Configuration.Builder()
						.setName(username)
						.setLogin(username)
						.setAutoNickChange(true)
						.addListener(new UnacceptableBot())
						.setServerHostname("peter.unacceptableuse.com")
						.setServerPort(6868)
						.setServerPassword("Stevie/Freenode:botbot")
						.addAutoJoinChannel("##UBTesting")
						.buildConfiguration();
		
		try
		{
			PircBotX bot = new PircBotX(config);
			bot.startBot();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args) throws Exception
	{
		new Initializer("UnacceptableBOT");
	}
}
