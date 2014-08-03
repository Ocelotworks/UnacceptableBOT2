package com.unacceptableuse.unacceptablebot.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.variable.Level;

public class CommandFillMeIn extends Command {

	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args, PircBotX bot) {

		Process p;
		try {
			p = Runtime.getRuntime().exec("tail " + channel.getName() + ".ub2log lines=" + args[0]);

			p.waitFor();
			BufferedReader buf = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line = "";
			String output = "";

			while ((line = buf.readLine()) != null) {
				output += line + "\n";
			}

			bot.sendIRC().notice(sender.getNick(),output);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Override
	public String[] getAliases() {
		return new String[] { "fillmein", "fmi" };
	}

	@Override
	public int requiredArguments() {
		return 1;
	}

}
