package com.unacceptableuse.unacceptablebot.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.google.common.io.Files;
import com.unacceptableuse.unacceptablebot.variable.Level;

public class CommandYoutube extends Command {

	@Override
	public String[] getAliases() {
		return new String[] { "youtube" };
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Command to download youtube videos : !youtube <video link>";
	}

	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args) {
		try {
			Runtime rt = Runtime.getRuntime();
			Process p = rt
					.exec("youtube-dl --extract-audio --audio-format mp3 -l "
							+ args[1]);

			p.waitFor();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			String line = "";
			String song = "";
			String temp = "";
			while ((line = reader.readLine()) != null) {
				if (line.contains("[download]")) {
					if (line.contains("in")) {
						line = line.replace("[download] ", "");
						line = line.replace("Destination: ", "");
						line = line.replace(".m4a", "");
						line = temp += line
								+ System.getProperty("line.separator");
						song = line;
					}
				}
			}
			
			File file = finder("/home/peter/Stevie/")[0];
			new File("/home/peter/mp3" + file.getName()).createNewFile();
			Files.copy(file, new File("/home/peter/mp3/" + file.getName()));
			file.delete();
			sendMessage("&GREENThe requested video can be found at: http://files.unacceptableuse.com",channel);

		} catch (IOException | InterruptedException e) {
			sendMessage("Fuck." + e.getMessage(), channel);
		}
	}

	public File[] finder(String dirName) {
		File dir = new File(dirName);

		return dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".mp3");
			}
		});

	}

	@Override
	public Level getAccessLevel() {
		return Level.SUPERADMIN;
	}

}
