package com.unacceptableuse.unacceptablebot;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import com.unacceptableuse.unacceptablebot.command.CommandSql;
import com.unacceptableuse.unacceptablebot.handler.CommandHandler;
import com.unacceptableuse.unacceptablebot.handler.ConfigHandler;

public class UnacceptableBot extends ListenerAdapter
{	
	CommandHandler handler = new CommandHandler();
	ConfigHandler config = new ConfigHandler();
	
	public UnacceptableBot()
	{
		handler.init();
		config.init();
	}
	
	
	@Override
	public void onMessage(final MessageEvent event)throws Exception
	{
		if(event.getMessage().startsWith("!"))
		{
			String channel = event.getChannel().getName();
			handler.processMessage(event);
		}
	}
	
	@Override
	public void onPrivateMessage(final PrivateMessageEvent event)throws Exception
	{
//		if(event.getMessage().startsWith("!"))
//		{
//			String user = event.getUser().getNick();
//			handler.processMessage(event);
//		}
	}
}
