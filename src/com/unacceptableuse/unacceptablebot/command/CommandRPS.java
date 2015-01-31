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
@Deprecated
public class CommandRPS extends Command
{

	private String getAction(final int num)
	{
		switch (num)
		{
		case 1:
			return "rock";
		case 2:
			return "paper";
		case 3:
			return "scissors";
		default:
			return "error (" + num + ")";
		}
	}

	@Override
	public String[] getAliases()
	{

		return new String[] { "rps" };
	}

	@Override
	public String getHelp()
	{
		return "Usage: rps <rock|paper|scissors> | Result: Play a game of rock paper scissors!";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{

		final long lastFaucet = UnacceptableBot.getConfigHandler().getLong("faucetAt:" + sender.getUserId().toString());
		final int faucetTimeout = UnacceptableBot.getConfigHandler().getInteger("faucetTimeout");
		if (lastFaucet != 0L)
			if ((new Date().getTime() - lastFaucet) < faucetTimeout)
			{
				sendMessage("You must wait another " + (faucetTimeout - (new Date().getTime() - lastFaucet)) + " seconds until you can receive doge!", channel);
				return;
			}

		// 1 = ROCK 2 = PAPER 3 = SCISSORS

		final int playerChoice = args[1].equalsIgnoreCase("rock") ? 1 : args[1].equalsIgnoreCase("paper") ? 2 : args[1].equalsIgnoreCase("scissors") ? 3 : 0;
		if (playerChoice == 0)
		{
			sendMessage("Unknown choice. Try rock, paper or scissors.", channel);
			return;
		} else
		{
			final int steveChoice = (UnacceptableBot.rand.nextInt(300) / 100) + 1;
			final boolean playerWon = playerChoice == steveChoice ? false : (playerChoice == 1) && (steveChoice == 2) ? false : (playerChoice == 3) && (steveChoice == 1) ? false : (playerChoice == 2) && (steveChoice == 3) ? false : true;
			sendMessage((playerWon ? "You WIN! " : steveChoice == playerChoice ? "You TIED! " : "You LOSE! ") + UnacceptableBot.getConfigHandler().getString("botName") + " chose " + getAction(steveChoice) + " and you chose " + getAction(playerChoice), channel);
			if (playerWon && !UnacceptableBot.getConfigHandler().getBoolean("rpsBounty"))
				sendMessage(".tip " + sender + " " + UnacceptableBot.rand.nextInt((UnacceptableBot.getConfigHandler().getInteger("faucetLimit")) + 1), channel);

			UnacceptableBot.getConfigHandler().setLong("faucetAt:" + sender.getUserId().toString(), new Date().getTime());
		}

	}

}
