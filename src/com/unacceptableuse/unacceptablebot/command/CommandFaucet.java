package com.unacceptableuse.unacceptablebot.command;

import java.util.Date;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

/**
 * 
 * @author Peter
 *
 */
public class CommandFaucet extends Command
{

	@Override
	public void performCommand(User sender, Channel channel, String message,String[] args, PircBotX bot)
	{
		String tipbotFormat = UnacceptableBot.getConfigHandler().getString("tipbot:"+channel.getName());
		
		if(tipbotFormat == null)
		{
			sendMessage(bot, "No tipbot defined for "+channel.getName()+".", channel.getName());
		}else
		{
			long lastFaucet = UnacceptableBot.getConfigHandler().getLong("faucetAt:"+sender.getUserId().toString());
			int faucetTimeout = UnacceptableBot.getConfigHandler().getInteger("faucetTimeout");
			if(lastFaucet != 0L)
			{
				if(new Date().getTime()-lastFaucet < faucetTimeout)
				{
					sendMessage(bot, "You must wait another "+(faucetTimeout-(new Date().getTime()-lastFaucet))+" seconds until you can receive doge!", channel.getName());
				}
			}
			
			sendMessage(bot, String.format(tipbotFormat, UnacceptableBot.rand.nextInt(UnacceptableBot.getConfigHandler().getInteger("faucetLimit")), sender.getNick()), channel.getName());
			UnacceptableBot.getConfigHandler().setLong("faucetAt:"+sender.getUserId().toString(), new Date().getTime());
		}
	}

	@Override
	public String[] getAliases()
	{
		
		return new String[]{"faucet","freedoge"};
	}

}
