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

public class CommandWolfram extends Command {

	@Override
	public void performCommand(User sender, Channel channel, String message, String[] args) {
		
		sendMessage("Working...", channel);

		WAEngine wolfram = new WAEngine();

		wolfram.setAppID("V4GHYA-AJTVGY4XJQ");
		WAQuery query = wolfram.createQuery();
		query.setInput(message.replace(args[0] + " ", ""));
		int max = 10, count = 0;

		try {
			WAQueryResult result = wolfram.performQuery(query);

			if(result.isError()) {
				sendMessage("Query error: " + result.getErrorCode() + ": " + result.getErrorMessage(), channel);
			} else if(!result.isSuccess()) {
				sendMessage("Query was not understood.", channel);
			} else {
				for(WAPod pod : result.getPods()) {	
					if(!pod.isError()) {
						for(WASubpod subpod : pod.getSubpods()) {
							for(Object obj : subpod.getContents()) {
								if(obj instanceof WAPlainText && ((WAPlainText) obj).getText().length() > 1) {
									sendMessage(" " + (pod.getTitle().length() > 1 ? "&BOLD" + pod.getTitle() + "&RESET: " : "") + ((WAPlainText) obj).getText(), channel);
								}
							}
						}
					}
					
					count++;
					if(count > max) {
						return;
					}
				}
			}
		} catch(WAException e) {
			sendMessage("An error ocurred performing this query: " + e.getMessage(), channel);
		}
	}

	@Override
	public String[] getAliases() {
		return new String[]{"wolfram"};
	}

	@Override
	public String getHelp() {
		return "Usage: wolfram <querey> | Performs wolfram alpha query.";
	}
	
	@Override
	public int requiredArguments() {
		return 1;
	}
}
