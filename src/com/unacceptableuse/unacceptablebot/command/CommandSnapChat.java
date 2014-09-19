package com.unacceptableuse.unacceptablebot.command;

import java.io.File;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.handler.SnapchatHandler;

public class CommandSnapChat extends Command {

	boolean canSnap = true;

	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args) {
		String com = args[1];
		switch (com) {
		case ("test"): {
			File f = new File("api.pem");
			if (!f.exists()) {
				sendMessage("Cannot send snaps. Missing " + f.getAbsolutePath(), channel);
			} else {
				sendMessage("Snapchat is ok!", channel);
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
				sendMessage("Did you enter the correct number of arguments?", channel);
				e.printStackTrace();
			}
			break;
		}
		case ("getuser"): {
			sendMessage("Snapchating as "+ UnacceptableBot.getSnapchat().getUser(), channel);
			break;
		}
		case ("getpass"): {
			try {
				sendMessage("Password: " + UnacceptableBot.getSnapchat().getPass(), channel);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		case ("loggedin"): {
			sendMessage("Logged in: " + UnacceptableBot.getSnapchat().logged(), channel);
			break;
		}
		case ("fucked"): {
			UnacceptableBot.setSnapchat(null);
			sendMessage("Reseting handler..", channel);
			SnapchatHandler replacement = new SnapchatHandler();
			sendMessage("Creating new handler..", channel);
			UnacceptableBot.setSnapchat(replacement);
			sendMessage("Setting handler..", channel);
			try {
				UnacceptableBot.getSnapchat().init();
				sendMessage("Handler Init..", channel);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		default: {
			sendMessage("Unknown Snapchat API command!", channel);
		}
		}
	}

	@Override
	public String[] getAliases() {
		return new String[] { "sc", "snap", "snapchat" };
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
