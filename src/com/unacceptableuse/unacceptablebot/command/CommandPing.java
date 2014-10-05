package com.unacceptableuse.unacceptablebot.command;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.pircbotx.Channel;
import org.pircbotx.User;

/**
 * 
 * @author Edward
 *
 */
public class CommandPing extends Command {

	@Override
	public void performCommand(User sender, Channel channel, String message, String[] args) {
		String address = message.replace(args[0] + " ", "").replace("http://", "");
		sendMessage("Pinging " + address + "...", channel);
		
		int ping = 0;
		try {
			final URL url = new URL("http://" + address);
			final HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setConnectTimeout(1000 * 10); 
			final long startTime = System.currentTimeMillis();
			urlConn.connect();
			final long endTime = System.currentTimeMillis();
			if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				ping = (int) (endTime - startTime) ; 
				sendMessage(address + " responded in " + ping + "ms.", channel);
			} else {
				sendMessage("HTTP Error " + urlConn.getResponseCode(), channel);
			}
		} catch (IOException e) {
			sendMessage("Unable to connect to " + address + ".", channel);
		}
	}

	@Override
	public String[] getAliases() {
		return new String[]{"ping"};
	}

	@Override
	public String getHelp() {
		return "Usage: ping <url> | Pings the given URL.";
	}

}
