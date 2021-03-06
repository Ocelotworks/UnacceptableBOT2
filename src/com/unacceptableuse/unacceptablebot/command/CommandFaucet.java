package com.unacceptableuse.unacceptablebot.command;

import java.util.Date;

import org.pircbotx.Channel;
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
	public String[] getAliases()
	{

		return new String[] { "faucet", "freedoge" };
	}

	@Override
	public String getHelp()
	{
		return "System command";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{
		final String tipbotFormat = UnacceptableBot.getConfigHandler().getString("tipbot:" + channel.getName());

		if (tipbotFormat == null)
			sendMessage("No tipbot defined for " + channel.getName() + ".", channel);
		else
		{
			final long lastFaucet = UnacceptableBot.getConfigHandler().getLong("faucetAt:" + sender.getUserId().toString());
			final int faucetTimeout = UnacceptableBot.getConfigHandler().getInteger("faucetTimeout");
			if (lastFaucet != 0L)
				if ((new Date().getTime() - lastFaucet) < faucetTimeout)
					sendMessage("You must wait another " + (faucetTimeout - (new Date().getTime() - lastFaucet)) + " seconds until you can receive doge!", channel);

			sendMessage(String.format(tipbotFormat, UnacceptableBot.rand.nextInt(UnacceptableBot.getConfigHandler().getInteger("faucetLimit")), sender.getNick()), channel);
			UnacceptableBot.getConfigHandler().setLong("faucetAt:" + sender.getUserId().toString(), new Date().getTime());
		}
	}

}
