package com.unacceptableuse.unacceptablebot.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.command.Command;
import com.unacceptableuse.unacceptablebot.variable.Level;

public class CommandHandler
{
	public ArrayList<Command> _commands = new ArrayList<Command>();

	/**
	 * Registers Command
	 *
	 * @author UnacceptableUse
	 * @param c
	 *            Command to add.
	 */
	public void addCommand(final Command c)
	{
		_commands.add(c);
	}

	/**
	 * Get the <b>first</b> command that has the alias <code>c</code>
	 *
	 * @param c
	 *            The command (without the "!" prefix).
	 * @author UnacceptableUse
	 * @return The command object with the alias <code>c</code>
	 */
	public Command getCommand(final String c)
	{
		for (final Command command : getCommands())
			for (final String s : command.getAliases())
				if (s.toLowerCase().equals(c))
					return command;
		return null;
	}

	/**
	 * Returns the command ArrayList
	 *
	 * @author UnacceptableUse
	 * @return ArrayList
	 */
	public ArrayList<Command> getCommands()
	{
		return _commands;
	}

	/**
	 * Register Commands here!
	 *
	 * @author UnacceptableUse
	 */
	public void init()
	{
		System.out.println("Starting command registration");
		try {
			for(ClassInfo ci : ClassPath.from(ClassLoader.getSystemClassLoader()).getTopLevelClasses("com.unacceptableuse.unacceptablebot.command")) {
				if(!ci.getName().equals("com.unacceptableuse.unacceptablebot.command.Command")
						&& !ci.getName().contains("Dynamic")
						&& !ci.getName().contains("RPS")) {
					addCommand(((Command) ci.load().newInstance()));
				}
			}
		} catch(InstantiationException | IllegalAccessException | IOException e) {
			e.printStackTrace();
		}
		
		UnacceptableBot.log("DEBUG", "CMDREG", "Registered " + getCommands().size() + " commands successfully!");
		
		String table = "<html>"
				+ "<head>"
				+ "<title>Stevie Help Page</title>"
				+ "<link rel="+'"'+"stylesheet"+'"'+" type="+'"'+"text/css"+'"'+" href="+'"'+"https://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css"+'"'+" />"
				+ "<link rel="+'"'+"stylesheet"+'"'+" type="+'"'+"text/css"+'"'+" href="+'"'+"https://boywanders.us/stevie.css"+'"'+"/>"
						+ "</head>\n";
		
		table += "<table border=\"1\" width=\"50%\">\n" +
							"\t<tr>\n" +
								"\t\t<td>Command</td>\n" +
								"\t\t<td>Description</td>\n" +
								"\t\t<td>Access Level</td>\n" +
							"\t</tr>\n";
		
		for(Command c : _commands) {
			table += "\t<tr>\n" +
						"\t\t<td>!" + c.getAliases()[0] + "</td>\n" +
						"\t\t<td>" + c.getHelp().replace("<", "&lt;").replace(">", "&gt;") + "</td>\n" +
						"\t\t<td>" + c.getAccessLevel() + "</td>\n" +
					"\t</tr>\n";
		}
		
		table += "</table>\n";
		
		table += "</body></html>";
		
		try {
			PrintWriter pw = new PrintWriter("/home/peter/mp3/help.html");
			
			for(String line : table.split("\n")) {
				pw.println(line);
			}
			
			pw.close();
		} catch(IOException e) {
			
		}
	}

	/**
	 * Pass in onMessage for running commands
	 *
	 * @author UnacceptableUse, teknogeek, dvd604
	 * @param event
	 *            - The MessageEvent containing user, channel etc
	 */
	@SuppressWarnings("rawtypes")
	public void processMessage(final MessageEvent event)
	{

		final String message = event.getMessage();
		final Channel channel = event.getChannel();
		final User sender = event.getUser();

		final Command chosenCommand = getCommand(message.replaceFirst("!", "").split(" ")[0]);

		if (chosenCommand == null)
			return; // These arn't the commands you are looking for...
		else if ((chosenCommand.getAccessLevel() == Level.BANNED) || (UnacceptableBot.getConfigHandler().getUserLevel(sender) < Level.levelToInt(chosenCommand.getAccessLevel())))
		{
			event.getBot().sendIRC().message(event.getChannel().getName(), "You do not have permission to perform this command");
			event.getBot().sendIRC().message(event.getChannel().getName(), "Needed Level: " + Level.levelToInt(chosenCommand.getAccessLevel()) + " | Your Level: " + UnacceptableBot.getConfigHandler().getUserLevel(sender));
		} else if (chosenCommand.requiredArguments() > (event.getMessage().split(" ").length - 1))
			event.getBot().sendIRC().message(event.getChannel().getName(), "Insufficent Arguments. " + chosenCommand.getHelp());
		else
		{
			UnacceptableBot.getConfigHandler().increment("stat:commandsPerformed");
			UnacceptableBot.log("INFO", "CMDPRF", "Command " + chosenCommand.getAliases()[0] + " performed by " + sender.getNick());
			try
			{
				chosenCommand.performCommand(sender, channel, message, message.split(" "));
			} catch (final Exception e)
			{
				UnacceptableBot.log("ERROR", "CMDPRC", "Exception performing " + chosenCommand.getAliases()[0] + ": " + e.toString());
				event.getBot().sendIRC().message(event.getChannel().getName(), "An error occurred. (" + e.toString() + ")");
			}
		}
	}

	/**
	 * Unregisters Command
	 *
	 * @author UnacceptableUse
	 * @param c
	 *            Command to remove.
	 */
	public void removeCommand(final Command c)
	{
		_commands.remove(c);
	}

	/**
	 * Remove the <b>first</b> command that has the alias <code>c</code>
	 *
	 * @param c
	 *            The command (without the "!" prefix).
	 * @author UnacceptableUse
	 * @return True if the command was found and removed successfully.
	 */
	public boolean removeCommand(final String c)
	{
		for (final Command command : getCommands())
			for (final String s : command.getAliases())
				if (s.equalsIgnoreCase(c))
				{
					removeCommand(command);
					return true;
				}
		return false;
	}

	/**
	 * Sets the command arraylist
	 *
	 * @author UnacceptableUse
	 * @param c
	 *            - ArrayList to set
	 */
	public void setCommands(final ArrayList<Command> c)
	{
		_commands = c;
	}
}
