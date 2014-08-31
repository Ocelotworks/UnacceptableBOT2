package com.unacceptableuse.unacceptablebot.command;

import java.io.File;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.handler.SnapchatHandler;
import com.unacceptableuse.unacceptablebot.variable.Level;

public class CommandSnapChat extends Command {

	boolean canSnap = true;

	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args, PircBotX bot) {
		String com = args[1];
		switch (com) {
		case ("test"): {
			File f = new File("api.pem");
			if (!f.exists()) {
				bot.sendIRC().message(channel.getName(),
						"Cannot send snaps. Missing " + f.getAbsolutePath());
			} else {
				bot.sendIRC().message(channel.getName(), "Snapchat is ok!");
			}
			break;
		}
		case ("send"): {
			try {
				String url = args[2];
				String target = args[3];
				UnacceptableBot.getSnapchat().add.add(url);
				UnacceptableBot.getSnapchat().targ.add(target);
				UnacceptableBot.getSnapchat().complete.add(false);
			} catch (Exception e) {
				bot.sendIRC().message(channel.getName(),
						"Did you enter the correct number of arguments?");
				e.printStackTrace();
			}
			break;
		}
		case ("getuser"): {
			bot.sendIRC()
					.message(
							channel.getName(),
							"Snapchating as "
									+ UnacceptableBot.getSnapchat().getUser());
			break;
		}
		case ("getpass"): {
			try {
				bot.sendIRC().message(channel.getName(),
						"Password: " + UnacceptableBot.getSnapchat().getPass());
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		case ("loggedin"): {
			bot.sendIRC().message(channel.getName(),
					"Logged in: " + UnacceptableBot.getSnapchat().logged());
			break;
		}
		case ("fucked"): {
			UnacceptableBot.setSnapchat(null);
			bot.sendIRC().message(channel.getName(), "Reseting handler..");
			SnapchatHandler replacement = new SnapchatHandler();
			bot.sendIRC().message(channel.getName(), "Creating new handler..");
			UnacceptableBot.setSnapchat(replacement);
			bot.sendIRC().message(channel.getName(), "Setting handler..");
			try {
				UnacceptableBot.getSnapchat().init();
				bot.sendIRC().message(channel.getName(), "Handler Init..");
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		default: {
			bot.sendIRC().message(channel.getName(),
					"Unknown Snapchat API command!");
		}
		}
	}

	@Override
	public String[] getAliases() {
		return new String[] { "sc", "snap", "snapchat" };
	}

	@Override
	public Level getAccessLevel() {
		return Level.USER;
	}

	@Override
	public int requiredArguments() {
		return 1;
	}

	@Override
	public String getHelp() {
		return "System command";
	}

}
