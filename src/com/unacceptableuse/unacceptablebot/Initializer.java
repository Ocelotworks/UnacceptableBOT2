package com.unacceptableuse.unacceptablebot;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;

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
						.setServerHostname("irc.freenode.net")
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
		new Initializer("UnacceptableBOT2");
	}
}
