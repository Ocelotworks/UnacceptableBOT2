package com.unacceptableuse.unacceptablebot.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.pircbotx.Channel;
import org.pircbotx.User;

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
			Process p = Runtime.getRuntime().exec(
					"youtube-dl --extract-audio --audio-format mp3 -l "
							+ args[0]);

			p.waitFor();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				if(line.contains("[download]")){
					line.replace("[download] ", "");
					line.replace("Destination: ", "");
					line.replace(".m4a", "");
					sendMessage("Youtube:" + line, channel);
				}
			}
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
		
		copy("*.mp3","/home/peter/mp3");
	}
	
	public void copy(String from, String to){
		try {
			Process p = Runtime.getRuntime().exec(
					"mv " + from + " " + to);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
