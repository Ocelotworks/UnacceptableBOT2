package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.variable.Level;

public class CommandSetup extends Command
{

	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args, PircBotX bot)
	{
		sendMessage(bot, "Performing First time setup.", channel);
		sendMessage(bot, "This WILL get spammy...!", channel);
		sendMessage(bot, "Setting "+channel.getName()+" as home channel.", channel);
		UnacceptableBot.getConfigHandler().setString("homeChannel", channel.getName());
		sendMessage(bot, "Setting tipbot formats (#doge-coin:.tip %s$1 %s$2, #dogefest:!tip %s$1 %s$2)", channel);
		UnacceptableBot.getConfigHandler().setString("tipbot:#doge-coin", ".tip %s$1 %s$2");
		UnacceptableBot.getConfigHandler().setString("tipbot:#dogefest", "!tip %s$1 %s$2");
		sendMessage(bot, "Setting dogecoin faucet limits/timeouts.", channel);
		UnacceptableBot.getConfigHandler().setInteger("faucetTimeout", 1200);
		UnacceptableBot.getConfigHandler().setInteger("faucetLimit", 100);
		sendMessage(bot, "Setting Access levels...", channel);
		UnacceptableBot.getConfigHandler().setUserLevel(sender.getUserId().toString(), Level.SUPERADMIN);
		sendMessage(bot, "And last but not least. Setting the build number.", channel);
		UnacceptableBot.getConfigHandler().setInteger("build", 1);
		sendMessage(bot, "Congratulations. Youre shiney new BOT is now ready!.", channel);
		sendMessage(bot, "This command will delete itsself forever :(.", channel);
		UnacceptableBot.getCommandHandler().removeCommand(this);
		
	}

	@Override
	public String[] getAliases()
	{	
		return new String[]{"setup"};
	}

}
