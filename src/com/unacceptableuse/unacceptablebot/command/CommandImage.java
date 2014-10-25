package com.unacceptableuse.unacceptablebot.command;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class CommandImage extends Command
{

	@Override
	public String[] getAliases()
	{
		return new String[] { "image", "images" };
	}

	@Override
	public String getHelp()
	{
		return "Usage: image <subreddit>  | Result: Returns a link of a picture from the desired subreddit";
	}

	private String getRandomImage(final String subreddit)
	{
		final String bannedReddits = UnacceptableBot.getConfigHandler().getString("bannedReddits");
		final ArrayList<String> bannedRedditsAry = new ArrayList<String>(Arrays.asList(bannedReddits.split(",")));
		boolean canGetImage = true;
		for (final String s : bannedRedditsAry)
			if (s.equals(subreddit))
				canGetImage = false;
		if (!canGetImage)
			return "This subreddit has been disabled";
		try
		{

			final InputStream is = UnacceptableBot.getUrlContents("http://api.reddit.com/r/" + subreddit.replace(",", "").replace(".", ""));
			final com.google.gson.JsonParser parser = new com.google.gson.JsonParser();

			String imageURL = "Error";
			int timeout = 0;
			boolean isNSFW = false;
			while (!imageURL.contains("imgur") && (timeout < 40))
			{
				final com.google.gson.JsonObject object = parser.parse(new InputStreamReader(is)).getAsJsonObject().get("data").getAsJsonObject().get("children").getAsJsonArray().get(UnacceptableBot.rand.nextInt(20)).getAsJsonObject().get("data").getAsJsonObject();
				isNSFW = object.get("over_18").getAsBoolean();
				imageURL = object.get("title").getAsString() + ": " + object.get("url").getAsString();
				timeout++;
			}

			is.close();

			final boolean canGetNSFW = UnacceptableBot.getConfigHandler().getBoolean("allowNSFW");
			if (!canGetNSFW && isNSFW)
				return "NSFW Images are currently not allowed. Please try again later!";
			return imageURL.contains("imgur") ? imageURL + (isNSFW ? " &RED&BOLD[NSFW]&RESET" : "") : "Could not find image in that sub!";
		} catch (final IllegalStateException e)
		{
			return "Reddit returned nothing, try again later.";
		} catch (final IndexOutOfBoundsException e)
		{
			return "That subreddit does not exist or does not contain enough images.";
		} catch (final NullPointerException e)
		{
			return "That subreddit is banned, private or invalid.";
		} catch (final Exception e)
		{
			UnacceptableBot.log("ERROR", "!image", "Unhandled Exception: " + e.toString());
			e.printStackTrace();
			return "You crashed the bot, so have a gif of two kittens crashing into eachother: http://stream1.gifsoup.com/view6/2612041/kitty-crash-test-no-3-o.gif  &RED" + e.toString();
		}
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{
		sendMessage(getRandomImage(args[1]), channel);
	}

}
