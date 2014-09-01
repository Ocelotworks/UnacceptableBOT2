package com.unacceptableuse.unacceptablebot.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.variable.Level;

public class CommandMessageStats extends Command {

	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args) {
		final String target = args.length > 1 ? args[1] : sender.getNick();

		try {

			int totalCount = 0;// , totalLines = 0;
			HashMap<String, Integer[]> count = new HashMap<String, Integer[]>();
			File[] files = new File("./").listFiles();
			ArrayList<File> logs = new ArrayList<File>();
			long oldestFileTime = System.currentTimeMillis();
			if (oldestFileTime == 0) {} //Here to stop the unused var pestering the error console
			for (File f : files) {
				if (f.isDirectory() || !f.canRead())
					continue;
				if (f.getName().charAt(0) == '#' && !f.getName().contains("copy")) {
					logs.add(f);
				}

			}

			sendMessage("Processing " + logs.size()
					+ " logs... This might take a while", channel);
			for (File log : logs) {
				int icount = 0;
				int totalLines = 0;
				BufferedReader br = new BufferedReader(new FileReader(channel
						+ ".ub2log"));
				String line = br.readLine();
				while ((line = br.readLine()) != null) {
					if (line.toLowerCase().contains(target.toLowerCase()))
						icount++;
					totalLines++;
				}
				count.put(log.getName(), new Integer[] { icount, totalLines });
				br.close();

			}

			for (String s : count.keySet()) {
				totalCount += count.get(s)[0];
				// totalLines+= count.get(s)[1];
			}

			sendMessage(
					"&BOLD"
							+ target
							+ "&RESET has sent &BOLD"
							+ count.get(channel + ".log")[0]
							+ "&RESET messages in &BOLD"
							+ channel
							+ "&RESET. &BOLD"
							+ (count.get(channel + ".log")[0] * 100 / count
									.get(channel + ".log")[1])
							+ "&RESET% of all messages.", channel);
			sendMessage(
					"&BOLD"
							+ target
							+ "&RESET has sent &BOLD"
							+ totalCount
							+ "&RESET messages over &BOLD"
							+ count.size()
							+ "&RESET channels. &BOLDFuck knows I CBA fixing this&RESET% of all messages.",
					channel);

		} catch (Exception e) {
			sendMessage(
					"&REDSHIT!&RESET: " + e.getMessage() + " "
							+ e.getStackTrace()[0], channel);
		}
	}

	@Override
	public String[] getAliases() {

		return new String[] { "messagestats" };
	}

	@Override
	public Level getAccessLevel() {
		return Level.USER;
	}

	@Override
	public String getHelp() {
		return "Usage: messagestats | Result: Grades the channel of several recorded stats";
	}

}
