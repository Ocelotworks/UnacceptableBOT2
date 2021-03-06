package com.unacceptableuse.unacceptablebot.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

@SuppressWarnings("unused")
public class LotteryHandler
{

	private HashMap<String, Long> participants = new HashMap<String, Long>();
	private long currentTotal = 0L, currentJackpot = 0L;
	private boolean lotteryDone = false;

	public void init()
	{
			//Um
	}

	public void addParticipant(String user, long amount)
	{
		if (participants.containsKey(user) && !getConfig().getBoolean("lott:canEnterMultiples"))// Check if player has entered already, and if the gamemode allows it
		{
			log(user + " tried to enter lottery, but was already participating. (" + participants.get(user) + " DOGE)");
			sendMessage(user + ": Returning coins. Lottery is running in single entry mode!");
			sendCoins(user, amount);
			return;

		} else
		{
			final long min = getConfig().getLong("lott:minimumEntry");
			if (amount < min)// Prevent players from entering with small amounts of DOGE
			{
				log(user + " tried to enter lottery with " + amount + " DOGE but it is less than the minimum of " + min + " DOGE.");
				sendMessage(user + ": Please enter an amount higher than " + min);
				return;
			} else
			{
				
				if (getConfig().getBoolean("lott:canEnterMultiples") && participants.containsKey(user))// If the lottery allows multiples and this person has entered before
				{
					participants.put(user, participants.get(user) + amount);
					currentTotal = participants.get(user);
				}else
				{
					participants.put(user, amount);
					currentTotal+=amount;
				}
					currentJackpot = currentTotal - (currentTotal * (getConfig().getInteger("lott:profitPercentage") /100));
				
				log(user + " successfully entered the lottery with " + amount + " DOGE. Lottery jackpot is now " + currentJackpot);

			}
		}

	}
	
	
	public void pickWinner()
	{
		/* Dead code warnings are ugly.
		UnacceptableBot.getBot().sendIRC().message("##Ocelotworks", "updateDogecoinBalance()");																//First of all... We want to do a sanity check here. We can't go around promising dogecoin that doesn't exist now can we?
		UnacceptableBot.updateDogecoinBalance(); 							//So we update the Dogecoin balance
		UnacceptableBot.getBot().sendIRC().message("##Ocelotworks", "if(getConfig().getLong(dogeWalletBalance) < currentJackpot){");	
		if(false){		//Then we check if it's lower than the jackpot
			error("Sanity check failed. Jackpot too big!");					//If it is, something has seriously gone wrong here...
			UnacceptableBot.getBot().sendIRC().message("##Ocelotworks", "currentTotal = 	getConfig().getLong(dogeWalletBalance);");	
			currentTotal = 	getConfig().getLong("dogeWalletBalance");		//So we correct the total and complain
			currentJackpot = currentTotal - (currentTotal * (getConfig().getInteger("lott:profitPercentage") /100));
		}else{	
			UnacceptableBot.getBot().sendIRC().message("##Ocelotworks", "Sanity check 1 pass");	//Otherwise the jackpot is fine
			if(false){//If the jackpot is too small there's no point in picking a winner
				UnacceptableBot.getBot().sendIRC().message("##Ocelotworks", "Sanity check 2 fail");
				UnacceptableBot.getBot().sendIRC().message("##Ocelotworks", "Line ");
				getConfig().setBoolean("lott:isRollover", true); 			//So we set it as a rollover
				sendMessage("The lottery jackpot is too small! Rollover triggered!");
				return;														//Pretty self explanatory I think....
			}else{															//Sanity check complete, it's time to pick the winners
				log("Sanity check passed.");								//Just to make sure we know this
				try{														//We grab a random number from random.org
					debug("Grabbing random number from random.org");
					InputStream is = UnacceptableBot.getUrlContents("http://www.random.org/integers/?num=1&min=0&max="+participants.size()+"&col=1&base=10&format=plain&rnd=new");
					InputStreamReader isr = new InputStreamReader(is);		//All this for a number. I could just use Math.random but where's the fun in that?
					BufferedReader br = new BufferedReader(isr);			//Reading URLs in Java is such a bitch, amirite?
					int winner = Integer.parseInt(br.readLine())-112;			//We get the number and parse it as an integer, this is the winner
					debug("Random number is "+winner);
					if(false)
					{														//We do another quick sanity check to make sure we arn't picking a winner that sneaked past!
						error("Invalid winner #"+winner+". Under minimum entry.");
						pickWinner();										//We pick another winner.
						return;												//We all know what return means, right? (I'm looking at you Neil)
					}else
					{
						log("Valid winner entrant #"+winner);				//Get the winner from winner ID
						String winnerName = participants.entrySet().toArray()[winner].toString();
						log("Winner is "+winnerName);						//and we find the winner's name
						for(Channel c : UnacceptableBot.getBot().getUserBot().getChannels())
						{													//We get all the channels we're connected to, so we can get the #doge-coin channel
							if(c.getName().equals("#doge-coin"))			//When we get the channel, we can get the users
							{
								for(User u : c.getUsers())
								{											//We need to make sure the user is legit and is the correct winner
									if(u.isVerified() && u.getNick().equals(winnerName))
									{										//The user is online, so we can give them the coins
										sendMessage(Colors.BOLD+"Dogecoin lottery: A winner has been picked!");
										try{Thread.sleep(2000);}			//Sleep for 2 seconds to build some tension
										catch(Exception e){}				//Oh boy this is tense....
										sendMessage(Colors.BOLD+winnerName+" has won a jackpot of "+currentJackpot+" DOGE!");
										sendCoins(winnerName, currentJackpot);
										sendCoins("Peter", currentTotal-currentJackpot);
										resetLottery();						//Reset the lottery so it can be played again
										break;
									}
								}
								break;
							}
						}
						pickWinner();									 	//Picked winner was not online... unlucky
					}
				} catch (NumberFormatException e)							//If random.org doesn't return a number correctly
				{															//Something has obviously fucked up. We need a rollover
					error("Random.org did not return a number. "+e.toString());
					e.printStackTrace();									//Stack trace is always nice
					getConfig().setBoolean("lott:isRollover", true); 		//Set it to rollover
					sendMessage("There was an error picking the user. Rollover triggered!");
					return;
					
				} catch (IOException e)
				{
					error("IOException connecting to random.org. "+e.toString());
					e.printStackTrace();									//Stack trace is always nice
					getConfig().setBoolean("lott:isRollover", true); 		//Set it to rollover
					sendMessage("There was an error picking the user. Rollover triggered!");
					return;
				}
			}
		}*/
	}
	
	public void resetLottery()
	{
		log("Resetting lottery...");
		participants.clear();
		currentTotal = 0;
		currentJackpot = 0;
		getConfig().setBoolean("lott:isRollover", false); 
		UnacceptableBot.updateDogecoinBalance();
	}
	
	public void doTick()
	{
		if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == 12 && !lotteryDone)
		{
			log("Lottery commencing");
			pickWinner();
			lotteryDone = true;
		}else if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == 13)
		{
			lotteryDone = false;
		}
	}
	
	public void forceLottery()
	{
		log("Lottery forced");
		pickWinner();
	}
	
	public void forceReset()
	{
		log("Reset forced");
		resetLottery();
	}
	
	public void forceParticipant(String name, long amt)
	{
		log("Participant force added");
		participants.put(name, amt);
	}
	
	public void forceRefund()
	{
		log("Refund forced");
		sendMessage("A refund has been forced. Lottery will be reset.");
		for(String user : participants.keySet())
			sendCoins(user, participants.get(user));
		resetLottery();
		
	}
	
	public void forceRefund(String user)
	{
		log("Refund forced for "+user);
		sendCoins(user, participants.get(user));
		resetLottery();
	}
	
	public String getParticipants()
	{
		return participants.toString();
	}

	public void sendCoins(String user, long amount)
	{
		UnacceptableBot.getBot().sendIRC().message("DogeWallet", ".tip " + user + " " + amount);
	}

	private static void log(String message)
	{
		UnacceptableBot.log("INFO", "LOTTERY", message);
	}
	
	private static void error(String message)
	{
		UnacceptableBot.log("WARN", "LOTTERY", message);
	}
	
	private static void debug(String message)
	{
		UnacceptableBot.log("DEBUG", "LOTTERY", message);
	}

	private static void sendMessage(String message)
	{
		UnacceptableBot.getBot().sendIRC().message("#doge-coin", message);
	}

	private static ConfigHandler getConfig()
	{
		return getConfig();
	}

}
