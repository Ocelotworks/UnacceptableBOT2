package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.Tools;
import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.handler.WebSocketHandler;
import com.unacceptableuse.unacceptablebot.variable.Level;

public class CommandDebug extends Command
{

	@Override
	public Level getAccessLevel()
	{
		return Level.SUPERADMIN;
	}

	@Override
	public String[] getAliases()
	{
		return new String[] { "debug" };
	}
	
	@Override
	public int requiredArguments()
	{
		return 1;
	}

	@Override
	public String getHelp()
	{
		return "sock|commands|irc|reflection - Provides basic debug functions";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
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
					sendMessage("WebSocket on " + w.getAddress().getHostString() + ":" + w.getPort() + " connected to " + w.getConnectedClients() + " clients.", channel);
				else if (args[2].equalsIgnoreCase("send"))
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
			} else if (args[2].equalsIgnoreCase("reload"))
			{
				UnacceptableBot.getCommandHandler()._commands.clear();
				UnacceptableBot.getCommandHandler().init();
				sendMessage("Commands reloaded!", channel);
			}

		} else if (args[1].equalsIgnoreCase("irc"))
		{
			if (args.length < 3)
			{
				sendMessage("Usage: !debug irc status|nick|action|mode|notice|invite|message - Debug actions for IRC functions", channel);
				return;
			} else
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
		} else if (args[1].equalsIgnoreCase("topic"))
		{
			if (args.length < 3)
			{
				sendMessage("Usage: !debug topic removecurrent|removeindex|addcurrent|add|show|showall|up|down|set - Debug actions for topic setting", channel);
				return;
			} else
			{
				if (args[2].equalsIgnoreCase("removecurrent"))
				{
					Channel c = Tools.getChannel("##Ocelotworks");
					if(!c.getTopicSetter().equals(Tools.getBotUsername()))
					{
						sendMessage("Topic was not set by "+Tools.getBotUsername()+"!", channel);
						return;
					}else
					{
						int i = 0;
						for(String quote : UnacceptableBot.sexQuotes)
						{
							if(quote.equals(c.getTopic()))
							{
								UnacceptableBot.sexQuotes.remove(i);
								sendMessage("Successfully Removed quote.", channel);
								
								break;
							}
							i++;
						}
					}
					
				}else if(args[2].equalsIgnoreCase("removeindex"))
				{
					if(args.length < 4)
					{
						sendMessage("Usage: !debug topic removeindex <num>", channel);
						return;
					}else
					{
						int index = Integer.parseInt(args[3]);
						if(UnacceptableBot.sexQuotes.size() < index)
						{
							sendMessage("Index number higher than maximum ("+UnacceptableBot.sexQuotes.size()+")", channel);
							return;
						}else
						{
							
						}
					}
					
					
				}else if(args[2].equalsIgnoreCase("addcurrent"))
				{
					
				}else if(args[2].equalsIgnoreCase("add"))
				{
					
				}else if(args[2].equalsIgnoreCase("show"))
				{
					
				}else if(args[2].equalsIgnoreCase("showall"))
				{
					
				}else if(args[2].equalsIgnoreCase("up"))
				{
					UnacceptableBot.currentTopic++;
					UnacceptableBot.updateTopic();
				}else if(args[2].equalsIgnoreCase("down"))
				{
					UnacceptableBot.currentTopic--;
					UnacceptableBot.updateTopic();
				}else if(args[2].equalsIgnoreCase("set"))
				{
					if(args.length < 4)
					{
						sendMessage("Usage: !debug topic set <num>", channel);
						return;
					}else
					{
						int index = Integer.parseInt(args[3]);
						if(UnacceptableBot.sexQuotes.size() < index)
						{
							sendMessage("Index number higher than maximum ("+UnacceptableBot.sexQuotes.size()+")", channel);
							return;
						}else
						{
							UnacceptableBot.currentTopic = index;
							UnacceptableBot.updateTopic();
						}
					}
				}
			}
		} else if (args[1].equalsIgnoreCase("reflection"))
		{
			if(args.length < 4)
			{
				sendMessage("Usage: !debug reflection variable <Instance in UnacceptableBot.class> <varible> OR method <method> <parameters> - Retrieves a varible from a selected class", channel);
				return;
			}else
			{
				if(args[2].equals("variable"))
				{
					Class c = UnacceptableBot.class;
					Method m = c.getDeclaredMethod(args[3]);
					
					
				}else if(args[2].equals("variable"))
				{
					
				}else
				{
					sendMessage("Invalid", channel);
					return;
				}
					
			}
			
		}
	}

}
