package com.unacceptableuse.unacceptablebot.command;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.handler.ConfigHandler;
import com.unacceptableuse.unacceptablebot.variable.Level;

/**
 *
 * @author Neil
 *
 */
public class CommandImport extends Command
{

	@Override
	public Level getAccessLevel()
	{
		return Level.SUPERADMIN;
	}

	@Override
	public String[] getAliases()
	{
		return new String[] { "import" };
	}

	@Override
	public String getHelp()
	{
		return "System command";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{
		// 0 1 2 3 4 5 6 split.length()-1
		// format: [Wed Jan 29 21:28:27 GMT 2014] <teknogeek> i cant ssh in
		// args: logFile, table
		int lastPercent = 0;
		int currentPercent = 0;
		try
		{
			sendMessage("Starting import!", channel);
			final String[] log = readLog(args[1]);
			final ConfigHandler config = UnacceptableBot.getConfigHandler();
			for (int i = 0; i < log.length; i++)
			{
				currentPercent = (i / log.length) * 100;
				if (currentPercent != lastPercent)
					sendMessage("Import " + currentPercent + "% complete", channel);
				final String[] split = log[i].split(" ");
				final String time = split[1] + " " + split[2] + ", " + split[3];
				final String user = split[6];
				String logMessage = "";
				for (final String element : split)
					if (i > 6)
						logMessage = logMessage + " " + split[i];
				config.createChannelTable(args[2]);
				config.setLog(time, user, logMessage, args[2]);
				lastPercent = currentPercent;
			}
		} catch (IOException | SQLException e)
		{
			e.printStackTrace();
			sendMessage("Import failed!", channel);
			return;
		}

		sendMessage("Import complete!", channel);

	}

	public String[] readLog(final String filename) throws IOException
	{
		final FileReader fileReader = new FileReader(filename);
		final BufferedReader bufferedReader = new BufferedReader(fileReader);
		final List<String> lines = new ArrayList<String>();
		String line = null;
		while ((line = bufferedReader.readLine()) != null)
			lines.add(line);
		bufferedReader.close();
		return lines.toArray(new String[lines.size()]);
	}

	@Override
	public int requiredArguments()
	{
		return 2;
	}
}
