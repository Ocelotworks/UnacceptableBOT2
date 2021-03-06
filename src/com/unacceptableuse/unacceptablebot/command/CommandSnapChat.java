package com.unacceptableuse.unacceptablebot.command;

import java.io.File;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.handler.SnapchatHandler;
import com.unacceptableuse.unacceptablebot.variable.Level;
import com.unacceptableuse.unacceptablebot.variable.Snap;

public class CommandSnapChat extends Command
{

	@Override
	public Level getAccessLevel()
	{
		return Level.SUPERADMIN;
	}

	@Override
	public String[] getAliases()
	{
		return new String[] { "sc", "snap", "snapchat" };
	}

	@Override
	public String getHelp()
	{
		return "System command";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{
		final String com = args[1];
		switch (com)
		{
		case ("test"):
		{
			final File f = new File("api.pem");
			if (!f.exists())
				sendMessage("Cannot send snaps. Missing " + f.getAbsolutePath(), channel);
			else if (!UnacceptableBot.getSnapchat().logged())
				sendMessage("Snapchat failed to login!", channel);
			else
				sendMessage("Snapchat is ok!", channel);
			break;
		}
		case ("send"):
		{
			try
			{
				final String url = args[2];
				final String target = args[3];
				final Snap snap = new Snap(target, url, null, channel.getName(), sender.getNick());
				UnacceptableBot.getSnapchat().addSnap(snap);
			} catch (final Exception e)
			{
				sendMessage("Did you enter the correct number of arguments?", channel);
				e.printStackTrace();
			}
			break;
		}
		case ("getuser"):
		{
			sendMessage("Snapchating as " + UnacceptableBot.getSnapchat().getUser(), channel);
			break;
		}
		case ("getpass"):
		{
			try
			{
				sendMessage("Password: " + UnacceptableBot.getSnapchat().getPass(), channel);
			} catch (final Exception e)
			{
				e.printStackTrace();
			}
			break;
		}
		case ("loggedin"):
		{
			sendMessage("Logged in: " + UnacceptableBot.getSnapchat().logged(), channel);
			break;
		}
		case ("forcequeue"):
		{
			UnacceptableBot.getSnapchat().doOneInQueue();
			break;
		}
		case ("fucked"):
		{
			UnacceptableBot.setSnapchat(null);
			sendMessage("Reseting handler..", channel);
			final SnapchatHandler replacement = new SnapchatHandler();
			sendMessage("Creating new handler..", channel);
			UnacceptableBot.setSnapchat(replacement);
			sendMessage("Setting handler..", channel);
			try
			{
				UnacceptableBot.getSnapchat().init();
				sendMessage("Handler Init..", channel);
			} catch (final Exception e)
			{
				sendMessage("Handler init failed: " + e.getCause(), channel);
				e.printStackTrace();
			}
			break;
		}
		default:
		{
			sendMessage("Unknown Snapchat API command!", channel);
		}
		}
	}

	@Override
	public int requiredArguments()
	{
		return 1;
	}
}
