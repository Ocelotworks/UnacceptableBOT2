package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.handler.WebSocketHandler;
import com.unacceptableuse.unacceptablebot.variable.Level;

public class CommandDebug extends Command
{

	@Override
	public void performCommand(User sender, Channel channel, String message, String[] args)
	{
		if (args[1].equalsIgnoreCase("sock"))
		{
			if (args.length < 3)
			{
				sendMessage("Usage: !debug sock status|send ", channel);
				return;
			} else
			{
				final WebSocketHandler w = UnacceptableBot.getWebSocketHandler();
				if (args[2].equalsIgnoreCase("status"))
				{
					sendMessage("WebSocket on " + w.getAddress().getHostString() + ":" + w.getPort() + " connected to " + w.getConnectedClients() + " clients.", channel);
				} else if (args[2].equalsIgnoreCase("send"))
				{
					sendMessage("Sending " + args[3] + " to all connected clients.", channel);
					w.logMessage(args[3]);
				}
			}

		} else if (args[1].equalsIgnoreCase("commands"))
		{
			if (args.length < 3)
			{
				sendMessage("Usage: !debug commands reload| ", channel);
				return;
			} else
			{
				if (args[2].equalsIgnoreCase("reload"))
				{
					UnacceptableBot.getCommandHandler()._commands.clear();
					UnacceptableBot.getCommandHandler().init();
					sendMessage("Commands reloaded!", channel);
				}
			}

		} else if (args[1].equalsIgnoreCase("irc"))
		{
			if (args.length < 3)
			{
				sendMessage("Usage: !debug irc status|nick|action|mode|notice|invite|message", channel);
				return;
			} else
			{
				switch (args[2].toLowerCase())
				{
				case "nick":
					UnacceptableBot.getBot().sendIRC().changeNick(args[3]);
					break;
				case "mode":
					UnacceptableBot.getBot().sendIRC().mode(args[3], args[4]);
					break;
				case "action":
					UnacceptableBot.getBot().sendIRC().action(args[3], args[4]);
					break;
				case "notice":
					UnacceptableBot.getBot().sendIRC().notice(args[3], args[4]);
					break;
				case "invite":
					UnacceptableBot.getBot().sendIRC().invite(args[3], args[4]);
					break;
				case "message":
					UnacceptableBot.getBot().sendIRC().message(args[3], args[4]);
					break;
				case "status":
					sendMessage(UnacceptableBot.getBot().getState().name(), channel);
					break;
				default:
					sendMessage("Unknown option. See !debug irc", channel);
				}
			}
		} else if (args[1].equalsIgnoreCase("bot"))
		{
			// if(args.length < 3)
			// {
			// sendMessage("Usage: !debug bot ", channel);
			// return;
			// }else
			// {
			// switch(args[2].toLowerCase())
			// {
			// case "nick": UnacceptableBot.getBot().getUserBot(). break;
			// case "mode": UnacceptableBot.getBot().sendIRC().mode(args[3], args[4]); break;
			// case "action": UnacceptableBot.getBot().sendIRC().action(args[3], args[4]); break;
			// case "notice": UnacceptableBot.getBot().sendIRC().notice(args[3], args[4]); break;
			// case "invite": UnacceptableBot.getBot().sendIRC().invite(args[3], args[4]); break;
			// case "message": UnacceptableBot.getBot().sendIRC().message(args[3], args[4]); break;
			// default: sendMessage("Unknown option. See !debug irc", channel);
			// }
			// }
		}
	}

	@Override
	public String[] getAliases()
	{
		return new String[] { "debug" };
	}

	@Override
	public String getHelp()
	{
		return "sock|commands|irc";
	}

	@Override
	public Level getAccessLevel()
	{
		return Level.SUPERADMIN;
	}

}
