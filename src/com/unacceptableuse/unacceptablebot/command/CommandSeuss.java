/**
 * 
 */
package com.unacceptableuse.unacceptablebot.command;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.variable.Level;

/**
 * @author Neil
 * 
 */
public class CommandSeuss extends Command {

	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args, PircBotX bot) {
		switch (args[1]) {
		default: {
			bot.sendIRC().message(channel.getName(), getLine(args[1]));
			break;
		}
		case ("list"): {
			File folder = new File("books/");
			File[] listOfFiles = folder.listFiles();
			bot.sendIRC().message(channel.getName(), "Books:");
			for(int i = 0; i < listOfFiles.length; i++){
				bot.sendIRC().message(channel.getName(), (i+1) + ". " + listOfFiles[i] );
			}
			break;
		}
		}

	}

	@Override
	public String[] getAliases() {
		return new String[] { "seuss" };
	}

	@Override
	public Level getAccessLevel() {
		return Level.USER;
	}

	@Override
	public int requiredArguments() {
		return 1;
	}

	public static String getLine(String args) {
		File file = new File("books/", args);
		try {
			int maxLines = countLines("books/" + args);
			int rand = new Random().nextInt(maxLines);
			if (rand == maxLines) {
				rand = rand + 1;
			}
			Scanner s = new Scanner(file);
			ArrayList<String> list = new ArrayList<String>();
			while (s.hasNext()) {
				list.add(s.next());
			}
			s.close();
			return list.get(rand);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return args;
	}

	public static int countLines(String filename) throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream(filename));
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n') {
						++count;
					}
				}
			}
			return (count == 0 && !empty) ? 1 : count;
		} finally {
			is.close();
		}
	}

}
