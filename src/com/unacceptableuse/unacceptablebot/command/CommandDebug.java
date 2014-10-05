package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.handler.WebSocketHandler;
import com.unacceptableuse.unacceptablebot.variable.Level;

public class CommandDebug extends Command
{

	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args)
	{
		if(args[1].equalsIgnoreCase("sock"))
		{
			if(args.length < 3)
			{
				sendMessage("debug sock status|send", channel);
				return;
			}else
			{
				final WebSocketHandler w = UnacceptableBot.getWebSocketHandler();
				if(args[2].equalsIgnoreCase("status"))
				{
					sendMessage("WebSocket on "+w.getAddress().getHostString()+":"+w.getPort()+" connected to "+w.getConnectedClients()+" clients.", channel);
				}else if(args[2].equalsIgnoreCase("send"))
				{
					sendMessage("Sending "+args[3]+" to all connected clients.", channel);
					w.logMessage(args[3]);
				}
			}
			
		}
		
	}

	@Override
	public String[] getAliases()
	{
		return new String[]{"debug"};
	}

	@Override
	public String getHelp()
	{
		return "sock|";
	}
	
	@Override
	public Level getAccessLevel()
	{
		return Level.SUPERADMIN;
	}

}
