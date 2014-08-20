package com.unacceptableuse.unacceptablebot.command;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.handler.ConfigHandler;
import com.unacceptableuse.unacceptablebot.variable.Level;

public class CommandImport extends Command {

	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args, PircBotX bot) {
		// 0 1 2 3 4 5 6 split.length()-1
		// format: [Wed Jan 29 21:28:27 GMT 2014] <teknogeek> i cant ssh in
		// args: logFile, table
		int lastPercent = 0;
		int currentPercent = 0;
		try {
			bot.sendIRC().message(channel.getName(), "Starting import!");
			String[] log = readLog(args[1]);
			ConfigHandler config = UnacceptableBot.getConfigHandler();
			for (int i = 0; i < log.length; i++) {
				currentPercent = (i / log.length) * 100;
				if (currentPercent != lastPercent) {
					bot.sendIRC().message(channel.getName(), "Import " + currentPercent + "% complete");
				}
				String[] split = log[i].split(" ");
				String time = split[1] + " " + split[2] + ", " + split[3];
				String user = split[6];
				String logMessage = "";
				for (int k = 0; k < split.length; k++) {
					if (i > 6) {
						logMessage = logMessage + " " + split[i];
					}
				}
				config.createChannelTable(args[2]);
				config.setLog(time, user, logMessage, args[2]);
				lastPercent = currentPercent;
			}
		} catch (IOException | SQLException e) {
			e.printStackTrace();
			bot.sendIRC().message(channel.getName(), "Import failed!");
			return;
		}

		bot.sendIRC().message(channel.getName(), "Import complete!");

	}

	@Override
	public String[] getAliases() {
		return new String[] { "import" };
	}

	@Override
	public Level getAccessLevel() {
		return Level.SUPERADMIN;
	}

	@Override
	public int requiredArguments() {
		return 2;
	}

	public String[] readLog(String filename) throws IOException {
		FileReader fileReader = new FileReader(filename);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		List<String> lines = new ArrayList<String>();
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			lines.add(line);
		}
		bufferedReader.close();
		return lines.toArray(new String[lines.size()]);
	}
}
