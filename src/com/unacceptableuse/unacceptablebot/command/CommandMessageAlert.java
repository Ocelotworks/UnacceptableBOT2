package com.unacceptableuse.unacceptablebot.command;

import java.util.ArrayList;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class CommandMessageAlert extends Command {
	
	@Override
	public String[] getAliases()
	{
		return new String[] { "msgalert", "alert" };
	}

	@Override
	public String getHelp()
	{
		return "Usage: msgalert [channel] | Result: Alerts you the next time something is said in [channel]. If [channel] is not defined, defaults to the channel the command was used in.";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{
		String target = channel.getName();
		if(args.length == 2) {
			target = args[1].startsWith("#") ? args[1] : "#" + args[1];
		}
		target = target.toLowerCase();
		
		if(!UnacceptableBot.msgalert.containsKey(target)) {
			UnacceptableBot.msgalert.put(target, new ArrayList<User>());
		}
		
		if(!UnacceptableBot.msgalert.get(target).contains(sender)) {
			UnacceptableBot.msgalert.get(target).add(sender);
			sender.send().notice("You will be notified the next time there is activity in " + target + ".");
		} else {
			sender.send().notice("You have already requested to be notified the next time there is activity in " + target + ".");
		}
	}
}
