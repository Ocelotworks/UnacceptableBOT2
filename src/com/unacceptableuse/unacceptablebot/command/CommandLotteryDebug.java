package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class CommandLotteryDebug extends Command
{

	@Override
	public String[] getAliases()
	{
		return new String[]{"ldebug","lottdebug","lotterydebug","debuglottery"};
	}

	@Override
	public String getHelp()
	{
		return "Admin command";
	}

	@Override
	public void performCommand(User sender, Channel channel, String message, String[] args)
	{
		if(args[1].equalsIgnoreCase("participants"))
		{
			sendMessage(UnacceptableBot.getLotteryHandler().getParticipants(), channel);
		}else if(args[1].equalsIgnoreCase("addparticipant")){
			UnacceptableBot.getLotteryHandler().forceParticipant(args[2], Long.parseLong(args[3]));
			sendMessage("It is done.", channel);
		}else if(args[1].equalsIgnoreCase("forcelottery")){
			UnacceptableBot.getLotteryHandler().forceLottery();
			sendMessage("It has been done.", channel);
		}else if(args[1].equalsIgnoreCase("forcereset")){
			UnacceptableBot.getLotteryHandler().forceReset();
			sendMessage("Your wishes have been fulfilled.", channel);
		}else if(args[1].equalsIgnoreCase("forcerefund")){
			UnacceptableBot.getLotteryHandler().forceRefund();
			sendMessage("The task has been completed.", channel);
		}else if(args[1].equalsIgnoreCase("refund")){
			UnacceptableBot.getLotteryHandler().forceRefund(args[2]);
			sendMessage("I have handled it, sir.", channel);
		}
		
	}
	
	public int requiredArguments()
	{
		return 1;
	}

}
