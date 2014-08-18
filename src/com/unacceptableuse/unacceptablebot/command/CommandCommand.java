package com.unacceptableuse.unacceptablebot.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

public class CommandCommand extends Command{

	@Override
	public void performCommand(User sender, Channel channel, String message, String[] args, PircBotX bot)
	{
		if(message.contains("rm"))
		{
			bot.sendIRC().message(channel.getName(), "Nice try, you are now banned from using " + bot.getNick() + " ever.");
			//ub.accessLevels.put(sender, -1);
			return;
		}
		
		/*
		if(ub.disabledCommands.contains("console:" + args[1]))
		{
			bot.sendIRC().message(channel.getName(), "That command has been disabled.");
			return;
		}
		*/
		
		try
		{
			Process p = Runtime.getRuntime().exec(message.split(args[0] + " ")[1]);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			System.out.println(p.toString());
			String line = "";
			int lineCount = 0;
		
			while((line = br.readLine()) != null)
			{
				lineCount++;
				bot.sendIRC().message(channel.getName(), line);
				
				/*
				if(ub.disabledCommands.contains("consoleOutput")) return;
				if(ub.disabledCommands.contains("consoleDisableAt:" + lineCount))
				{
					sendMessage(channel, "&REDLine exceeds maximum line count.");
					break;
				}
				*/
			}
			
			br.close();
		}
		catch(IOException e)
		{
			bot.sendIRC().message(channel.getName(), "The command " + args[1] + " was not recognized.");
		}
		
	}

	@Override
	public String[] getAliases()
	{
		return new String[]{"command"};
	}

	@Override
	public int requiredArguments()
	{
		return 1;
	}
}
