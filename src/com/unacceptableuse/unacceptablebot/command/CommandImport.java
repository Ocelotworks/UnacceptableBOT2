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
		//			0    1      2            3    4
		//format: [8 Aug, 21:49] <ThaHandyman> aye
		//args: logFile, table
		try {
			String[] log = readLog(args[1]);
			ConfigHandler config = UnacceptableBot.getConfigHandler();
			for(int i = 0; i < log.length; i++){
				String[] split = log[i].split(" ");
				String time = split[2].replace("[", "");
				time = time.replace("]", "");
				time = split[0].replace("[", "") + ", " + time;
				String user = split[3].replace("<", "");
				user = user.replace(">", "");
				String logMessage = split[4];
				config.createChannelTable(args[2]);
				config.setLog(time, user, logMessage, args[2]);
				
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
		return new String[]{"import"};
	}
	
	@Override
	public Level getAccessLevel()
	{
		return Level.SUPERADMIN;
	}
	
	@Override
	public int requiredArguments()
	{
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
