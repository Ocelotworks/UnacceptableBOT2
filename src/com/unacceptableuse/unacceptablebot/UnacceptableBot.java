package com.unacceptableuse.unacceptablebot;

import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

public class UnacceptableBot extends ListenerAdapter
{

	
	CommandHandler handler;

	public void init()
	{
		handler = new CommandHandler();
		handler.init();
	}
	
	
	@Override
	public void onGenericMessage(final GenericMessageEvent event)throws Exception
	{
		if(event.getMessage().startsWith("!"))
		{
			handler.processMessage(event);
		}
	}

	

}
