package com.unacceptableuse.unacceptablebot.handler;

import java.util.ArrayList;

import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.command.Command;
import com.unacceptableuse.unacceptablebot.command.CommandBroadcast;
import com.unacceptableuse.unacceptablebot.command.CommandCommand;
import com.unacceptableuse.unacceptablebot.command.CommandConnect;
import com.unacceptableuse.unacceptablebot.command.CommandCount;
import com.unacceptableuse.unacceptablebot.command.CommandDebug;
import com.unacceptableuse.unacceptablebot.command.CommandDefine;
import com.unacceptableuse.unacceptablebot.command.CommandDefineUD;
import com.unacceptableuse.unacceptablebot.command.CommandFaucet;
import com.unacceptableuse.unacceptablebot.command.CommandFillMeIn;
import com.unacceptableuse.unacceptablebot.command.CommandFucksGiven;
import com.unacceptableuse.unacceptablebot.command.CommandFunction;
import com.unacceptableuse.unacceptablebot.command.CommandGitInfo;
import com.unacceptableuse.unacceptablebot.command.CommandGoogle;
import com.unacceptableuse.unacceptablebot.command.CommandHelp;
import com.unacceptableuse.unacceptablebot.command.CommandImage;
import com.unacceptableuse.unacceptablebot.command.CommandImageSearch;
import com.unacceptableuse.unacceptablebot.command.CommandImport;
import com.unacceptableuse.unacceptablebot.command.CommandInsult;
import com.unacceptableuse.unacceptablebot.command.CommandListChans;
import com.unacceptableuse.unacceptablebot.command.CommandLive;
import com.unacceptableuse.unacceptablebot.command.CommandMessageStats;
import com.unacceptableuse.unacceptablebot.command.CommandPing;
import com.unacceptableuse.unacceptablebot.command.CommandQuote;
import com.unacceptableuse.unacceptablebot.command.CommandRand;
import com.unacceptableuse.unacceptablebot.command.CommandRelay;
import com.unacceptableuse.unacceptablebot.command.CommandSentence;
import com.unacceptableuse.unacceptablebot.command.CommandSetAccessLevel;
import com.unacceptableuse.unacceptablebot.command.CommandSeuss;
import com.unacceptableuse.unacceptablebot.command.CommandSnapChat;
import com.unacceptableuse.unacceptablebot.command.CommandSql;
import com.unacceptableuse.unacceptablebot.command.CommandStevie;
import com.unacceptableuse.unacceptablebot.command.CommandTime;
import com.unacceptableuse.unacceptablebot.command.CommandTopic;
import com.unacceptableuse.unacceptablebot.command.CommandTwat;
import com.unacceptableuse.unacceptablebot.command.CommandTwitter;
import com.unacceptableuse.unacceptablebot.command.CommandWhoami;
import com.unacceptableuse.unacceptablebot.command.CommandWolfram;
import com.unacceptableuse.unacceptablebot.variable.Level;

public class CommandHandler {
	public ArrayList<Command> _commands = new ArrayList<Command>();

	/**
	 * Register Commands here!
	 * 
	 * @author UnacceptableUse
	 */
	public void init() {
		System.out.println("Starting command registration");
		// Keep this in alphabetical order.
		addCommand(new CommandBroadcast());
		addCommand(new CommandCommand());
		addCommand(new CommandConnect());
		addCommand(new CommandCount());
		addCommand(new CommandDebug());
		addCommand(new CommandDefine());
		addCommand(new CommandDefineUD());
		addCommand(new CommandFaucet());
		addCommand(new CommandFillMeIn());
		addCommand(new CommandFucksGiven());
		addCommand(new CommandFunction());
		addCommand(new CommandGitInfo());
		addCommand(new CommandGoogle());
		addCommand(new CommandHelp());
		addCommand(new CommandImage());
		addCommand(new CommandImageSearch());
		addCommand(new CommandImport());
		addCommand(new CommandInsult());
		addCommand(new CommandListChans());
		addCommand(new CommandLive());
		addCommand(new CommandMessageStats());
		addCommand(new CommandPing());
		addCommand(new CommandQuote());
		addCommand(new CommandRand());
		addCommand(new CommandRelay());
		addCommand(new CommandSentence());
		addCommand(new CommandSetAccessLevel());
		addCommand(new CommandSeuss());
		addCommand(new CommandSnapChat());
		addCommand(new CommandSql());
		addCommand(new CommandStevie());
		addCommand(new CommandTime());
		addCommand(new CommandTopic());
		addCommand(new CommandTwat());
		addCommand(new CommandTwitter());
		addCommand(new CommandWhoami());
		addCommand(new CommandWolfram());
		UnacceptableBot.log("DEBUG", "CMDREG", "Registered " + getCommands().size() + " commands successfully!");
	}

	/**
	 * Pass in onMessage for running commands
	 * 
	 * @author UnacceptableUse, teknogeek, dvd604
	 * @param event
	 *            - The MessageEvent containing user, channel etc
	 */
	@SuppressWarnings("rawtypes")
	public void processMessage(MessageEvent event) {

		String message = event.getMessage();
		Channel channel = event.getChannel();
		User sender = event.getUser();

		Command chosenCommand = getCommand(message.replaceFirst("!", "").split(" ")[0]);

		if (chosenCommand == null) {
			return; // These arn't the commands you are looking for...
		} else {
			if (chosenCommand.getAccessLevel() == Level.BANNED || UnacceptableBot.getConfigHandler().getUserLevel(sender) < Level.levelToInt(chosenCommand.getAccessLevel())) {
				event.getBot().sendIRC().message(event.getChannel().getName(), "You do not have permission to perform this command");
				event.getBot().sendIRC().message(event.getChannel().getName(), "Needed Level: " + Level.levelToInt(chosenCommand.getAccessLevel()) + " | Your Level: " + UnacceptableBot.getConfigHandler().getUserLevel(sender));
			} else {
				if (chosenCommand.requiredArguments() > (event.getMessage().split(" ").length - 1)) {
					event.getBot().sendIRC().message(event.getChannel().getName(), "Insufficent Arguments. " + chosenCommand.getHelp());
				} else {
					UnacceptableBot.getConfigHandler().increment("stat:commandsPerformed");
					UnacceptableBot.log("INFO", "CMDPRF", "Command " + chosenCommand.getAliases()[0] + " performed by " + sender.getNick());
					try {
						chosenCommand.performCommand(sender, channel, message, message.split(" "));
					} catch (Exception e) {
						UnacceptableBot.log("ERROR", "CMDPRC", "Exception performing " + chosenCommand.getAliases()[0] + ": " + e.toString());
						event.getBot().sendIRC().message(event.getChannel().getName(), "An error occurred. (" + e.toString() + ")");
					}
				}
			}
		}
	}

	/**
	 * Returns the command ArrayList
	 * 
	 * @author UnacceptableUse
	 * @return ArrayList
	 */
	public ArrayList<Command> getCommands() {
		return _commands;
	}

	/**
	 * Sets the command arraylist
	 * 
	 * @author UnacceptableUse
	 * @param c
	 *            - ArrayList to set
	 */
	public void setCommands(ArrayList<Command> c) {
		_commands = c;
	}

	/**
	 * Registers Command
	 * 
	 * @author UnacceptableUse
	 * @param c Command to add.
	 */
	public void addCommand(Command c) {
		_commands.add(c);
	}
	
	/**
	 * Unregisters Command
	 * 
	 * @author UnacceptableUse
	 * @param c Command to remove.
	 */
	public void removeCommand(Command c) {
		_commands.remove(c);
	}

	/**
	 * Remove the <b>first</b> command that has the alias <code>c</code>
	 * 
	 * @param c The command (without the "!" prefix).
	 * @author UnacceptableUse
	 * @return True if the command was found and removed successfully.
	 */
	public boolean removeCommand(String c) {
		for (Command command : getCommands()) {
			for (String s : command.getAliases()) {
				if (s.equalsIgnoreCase(c)) {
					removeCommand(command);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Get the <b>first</b> command that has the alias <code>c</code>
	 * 
	 * @param c The command (without the "!" prefix).
	 * @author UnacceptableUse
	 * @return The command object with the alias <code>c</code>
	 */
	public Command getCommand(String c) {
		for (Command command : getCommands()) {
			for (String s : command.getAliases()) {
				if (s.toLowerCase().equals(c)) {
					return command;
				}
			}
		}
		return null;
	}
}
