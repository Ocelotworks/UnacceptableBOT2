package com.unacceptableuse.unacceptablebot.command;

import java.util.Date;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.variable.Level;

/**
 * 
 * @author Peter
 *
 */
public class CommandRPS extends Command
{

	@Override
	public void performCommand(User sender, Channel channel, String message,String[] args)
	{
		
		long lastFaucet = UnacceptableBot.getConfigHandler().getLong("faucetAt:"+sender.getUserId().toString());
		int faucetTimeout = UnacceptableBot.getConfigHandler().getInteger("faucetTimeout");
		if(lastFaucet != 0L)
		{
			if(new Date().getTime()-lastFaucet < faucetTimeout)
			{
				sendMessage("You must wait another "+(faucetTimeout-(new Date().getTime()-lastFaucet))+" seconds until you can receive doge!", channel);
				return;
			}
		}	
			
		//1 = ROCK 2 = PAPER 3 = SCISSORS
			
		int playerChoice = args[1].equalsIgnoreCase("rock") ? 1 : args[1].equalsIgnoreCase("paper") ? 2 : args[1].equalsIgnoreCase("scissors") ? 3 : 0;
		if(playerChoice == 0)
		{
			sendMessage("Unknown choice. Try rock, paper or scissors.", channel);
			return;
		}else
		{
			int steveChoice = (UnacceptableBot.rand.nextInt(300)/100)+1;
			boolean playerWon = playerChoice == steveChoice ? false : playerChoice == 1 && steveChoice == 2 ? false : playerChoice == 3 && steveChoice == 1 ? false : playerChoice == 2 && steveChoice == 3 ? false :  true; 
			sendMessage((playerWon ? "You WIN! " : steveChoice == playerChoice ? "You TIED! " : "You LOSE! ")+UnacceptableBot.getConfigHandler().getString("botName")+" chose "+getAction(steveChoice)+" and you chose "+getAction(playerChoice), channel);
			if(playerWon && !UnacceptableBot.getConfigHandler().getBoolean("rpsBounty"))
				sendMessage(".tip "+sender+" "+UnacceptableBot.rand.nextInt((UnacceptableBot.getConfigHandler().getInteger("faucetLimit"))+1), channel);
			
			UnacceptableBot.getConfigHandler().setLong("faucetAt:"+sender.getUserId().toString(), new Date().getTime());
		}
				
		
	}
	
	private String getAction(int num)
	{
		switch(num)
		{
		case 1:
			return "rock";
		case 2:
			return "paper";
		case 3:
			return "scissors";
		default:
			return "error ("+num+")";
		}
	}

	@Override
	public String[] getAliases()
	{
		
		return null;
	}

	@Override
	public Level getAccessLevel() {
		return Level.USER;
	}

	@Override
	public String getHelp() {
		return "Usage: rps <rock|paper|scissors> | Result: Play a game of rock paper scissors!";
	}

}
