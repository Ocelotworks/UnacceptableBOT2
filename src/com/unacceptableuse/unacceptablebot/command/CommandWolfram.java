package com.unacceptableuse.unacceptablebot.command;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAPlainText;
import com.wolfram.alpha.WAPod;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;
import com.wolfram.alpha.WASubpod;

public class CommandWolfram extends Command
{

	@Override
	public String[] getAliases()
	{
		return new String[] { "wolfram" };
	}

	@Override
	public String getHelp()
	{
		return "Usage: wolfram <querey> | Performs wolfram alpha query.";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{

		sendMessage("Working...", channel);

		final WAEngine wolfram = new WAEngine();

		wolfram.setAppID("V4GHYA-AJTVGY4XJQ");
		final WAQuery query = wolfram.createQuery();
		query.setInput(message.replace(args[0] + " ", ""));
		final int max = 2;
		int count = 0;

		try {
			final WAQueryResult result = wolfram.performQuery(query);

			if (result.isError()) {
				sendMessage("Query error: " + result.getErrorCode() + ": " + result.getErrorMessage(), channel);
			} else if (!result.isSuccess()) {
				sendMessage("Query was not understood.", channel);
			} else {
				for (final WAPod pod : result.getPods()) {
					if (!pod.isError()) {
						for (final WASubpod subpod : pod.getSubpods()) {
							for (final Object obj : subpod.getContents()) {
								if ((obj instanceof WAPlainText) && (((WAPlainText) obj).getText().length() > 1)) {
									sendMessage(" " + (pod.getTitle().length() > 1 ? "&BOLD" + pod.getTitle() + "&RESET: " : "") + ((WAPlainText) obj).getText(), channel);
								}
							}
						}
					}
						
					count++;
					if (count > max) {
						sendMessage(" More info: http://www.wolframalpha.com/input/?i=" + message.replace(args[0] + " ", "").replace(" ", "+"), channel);
						return;
					}
				}
			}
		} catch (final WAException e) {
			sendMessage("An error ocurred performing this query: " + e.getMessage(), channel);
		}
	}

	@Override
	public int requiredArguments()
	{
		return 1;
	}
}
