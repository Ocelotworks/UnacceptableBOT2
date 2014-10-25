package com.unacceptableuse.unacceptablebot.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author Edward
 *
 */
public class CommandImageSearch extends Command
{

	@Override
	public String[] getAliases()
	{
		return new String[] { "gimage", "imagesearch" };
	}

	@Override
	public String getHelp()
	{
		return "Usage: gimage <search term> [--number <1-4>] [--rehost] | Result: A link to the first result on google images, if --n is included, returns the nth result, will rehost the image to imgur or gfycat if --rehost is included.";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{
		int nth = 0;
		Boolean rehost = false;

		for (int i = 0; i < args.length; i++)
			if (args[i].toLowerCase().startsWith("--n"))
				try
		{
					nth = Integer.parseInt(args[i + 1]) - 1;
					if ((nth < 0) || (nth > 7))
					{
						sendMessage("Number must be between 1 and 8.", channel);
						return;
					}
		} catch (final NumberFormatException nfe)
		{
			sendMessage(args[i + 1] + "is not a number.", channel);
			return;
		}
			else if (args[i].toLowerCase().startsWith("--r"))
				rehost = true;

		final String query = message.substring(message.indexOf(" ") + 1, message.indexOf("--") == -1 ? message.length() - 1 : message.indexOf(" --"));

		final JsonParser json = new JsonParser();

		String url = "";
		try
		{
			final InputStream is = new URL("http://ajax.googleapis.com/ajax/services/search/images?v=13.0&rsz=8&q=" + query.replace(" ", "%20").replace("&", "and")).openStream();
			url = json.parse(new InputStreamReader(is)).getAsJsonObject().get("responseData").getAsJsonObject().get("results").getAsJsonArray().get(nth).getAsJsonObject().get("url").getAsString();
		} catch (final Exception e)
		{
			if (e.getMessage().equals("Index: 0, Size: 0"))
				sendMessage("No results found.", channel);
			else
				sendMessage(e.getMessage(), channel);
			return;
		}

		try
		{
			if (rehost)
				try
			{
					final HttpURLConnection httpcon = (HttpURLConnection) new URL("http://upload.gfycat.com/transcode?fetchUrl=" + url).openConnection();
					httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");
					final String gfy = json.parse(new InputStreamReader(httpcon.getInputStream())).getAsJsonObject().get("gfyName").getAsString();
					url = "http://gfycat.com/" + gfy;
			} catch (final NullPointerException notagif)
			{
				URL api;
				api = new URL("https://api.imgur.com/3/image");
				final HttpURLConnection conn = (HttpURLConnection) api.openConnection();

				final String data = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(url, "UTF-8");

				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Authorization", "Client-ID " + "0b47321417f7173");
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				conn.connect();

				final OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
				wr.write(data);
				wr.close();

				final JsonObject imgurJson = json.parse(new InputStreamReader(conn.getInputStream())).getAsJsonObject();

				if (imgurJson.get("success").getAsBoolean())
					url = imgurJson.get("data").getAsJsonObject().get("link").getAsString();
				else
					sendMessage("Rehost unsuccessful, original image: " + url, channel);

				conn.disconnect();
			}

			if (url.equals(""))
				sendMessage("No results found.", channel);
			else
				sendMessage(url, channel);
		} catch (final IOException timeout)
		{
			sendMessage("Rehost unsuccessful, original image: " + url, channel);
		}
	}

	@Override
	public int requiredArguments()
	{
		return 1;
	}
}
