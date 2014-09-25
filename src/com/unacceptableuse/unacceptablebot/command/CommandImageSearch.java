package com.unacceptableuse.unacceptablebot.command;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.google.gson.JsonParser;

/**
 * 
 * @author Edward
 *
 */
public class CommandImageSearch extends Command {

	@Override
	public void performCommand(User sender, Channel channel, String message, String[] args) {
		String querey = message.replace(args[0] + " ", "");
				
		InputStream is = null;
		try {
			is = new URL("http://ajax.googleapis.com/ajax/services/search/images?v=13.0&q=" + querey.replace(" ", "%20").replace("&", "and")).openConnection().getInputStream();
			String url = new JsonParser().parse(new InputStreamReader(is)).getAsJsonObject().get("responseData").getAsJsonObject().get("results").getAsJsonArray().get(0).getAsJsonObject().get("url").getAsString();
			sendMessage(url, channel);
		} catch (Exception e) {
			sendMessage(e.getMessage(), channel);
		}
	}

	@Override
	public String[] getAliases() {
		return new String[]{ "gimage", "imagesearch" };
	}

	@Override
	public String getHelp() {
		return "Usage: gimage <search term> | Result: A link to the first result on google images.";
	}
	
	@Override
	public int requiredArguments() {
		return 1;
	}
	
}
