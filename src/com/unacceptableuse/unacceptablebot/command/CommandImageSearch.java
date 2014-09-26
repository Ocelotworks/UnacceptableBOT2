package com.unacceptableuse.unacceptablebot.command;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

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
		int nth = 0;
		Boolean rehost = false;
		
		for(int i = 0; i < args.length; i++) {
			if(args[i].toLowerCase().startsWith("--n")) {
				try {
					nth = Integer.parseInt(args[i + 1]) - 1;
					if(nth < 0 || nth > 3) {
						sendMessage("Number must be between 1 and 4.", channel);
						return;
					}
				} catch (NumberFormatException nfe) {
					sendMessage(args[i + 1] + "is not a number.", channel);
					return;
				}
			} else if(args[i].toLowerCase().startsWith("--i")) {
				rehost = true;
			}
		}
		
		String query = message.substring(message.indexOf(" ") + 1, message.indexOf("--") == -1 ? message.length() - 1 : message.indexOf(" --"));
				
		try {
			InputStream is = new URL("http://ajax.googleapis.com/ajax/services/search/images?v=13.0&q=" + query.replace(" ", "%20").replace("&", "and")).openConnection().getInputStream();
			String url = new JsonParser().parse(new InputStreamReader(is)).getAsJsonObject().get("responseData").getAsJsonObject().get("results").getAsJsonArray().get(nth).getAsJsonObject().get("url").getAsString();
		
			if (rehost) {
				URL api;
				api = new URL("https://api.imgur.com/3/image");
				HttpURLConnection conn = (HttpURLConnection) api.openConnection();

				String data = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(url, "UTF-8");

				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Authorization", "Client-ID " + "0b47321417f7173");
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");

				conn.connect();
				OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
				wr.write(data);
				wr.close();

				url = new JsonParser().parse(new InputStreamReader(conn.getInputStream())).getAsJsonObject().get("data").getAsJsonObject().get("link").getAsString();
				conn.disconnect();
			}
			
			sendMessage(url, channel);
		} catch (Exception e) {
			if(e.getMessage().equals("Index: 0, Size: 0")) {
				sendMessage("No results found.", channel);
			} else {
				sendMessage(e.getMessage(), channel);
			}
			return;
		}
	}

	@Override
	public String[] getAliases() {
		return new String[]{ "gimage", "imagesearch" };
	}

	@Override
	public String getHelp() {
		return "Usage: gimage <search term> [--number <1-4>] [--imgur] | Result: A link to the first result on google images, if --n is included, returns the nth result, will rehost the image to imgur if --imgur is included.";
	}
	
	@Override
	public int requiredArguments() {
		return 1;
	}
}
