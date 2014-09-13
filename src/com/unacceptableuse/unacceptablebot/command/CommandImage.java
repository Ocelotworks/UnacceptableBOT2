package com.unacceptableuse.unacceptablebot.command;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.variable.Level;

public class CommandImage extends Command
{

	@Override
	public void performCommand(User sender, Channel channel, String message,String[] args)
	{
		sendMessage(getRandomImage(args[1]), channel);
	}
	
	private String getRandomImage(String subreddit)
	{
		
		if(UnacceptableBot.getConfigHandler().getBoolean("srd:"+subreddit))return "This subreddit has been disabled";
		try{

		
			InputStream is = UnacceptableBot.getUrlContents("http://api.reddit.com/r/"+subreddit.replace(",","").replace(".",""));
			com.google.gson.JsonParser parser = new com.google.gson.JsonParser(); 

			String imageURL = "Error";
			int timeout = 0;
			boolean isNSFW = false;
			while(!imageURL.contains("imgur") && timeout < 40)
			{
				com.google.gson.JsonObject object = 
						parser.parse(new InputStreamReader(is)).getAsJsonObject()
						.get("data").getAsJsonObject()
						.get("children").getAsJsonArray()
						.get(UnacceptableBot.rand.nextInt(20)).getAsJsonObject()
						.get("data").getAsJsonObject();
				isNSFW = object.get("over_18").getAsBoolean();
				imageURL = object.get("title").getAsString()+": "+object.get("url").getAsString();
				timeout++;
			}
			
			is.close();
			

		return imageURL.contains("imgur") ?  imageURL + (isNSFW ? " &RED&BOLD[NSFW]&RESET" : ""): "Could not find image in that sub!" ;
		}catch(IllegalStateException e)
		{
			return "Reddit returned nothing, try again later.";
		}
		catch(IndexOutOfBoundsException e)
		{
			return "That subreddit does not exist or does not contain enough images.";
		}
		catch(NullPointerException e)
		{
			return "That subreddit is banned, private or invalid.";
		}
		catch(Exception e)
		{
			UnacceptableBot.log("ERROR", "!image", "Unhandled Exception: "+e.toString());
			e.printStackTrace();
			return "You crashed the bot, so have a gif of two kittens crashing into eachother: http://stream1.gifsoup.com/view6/2612041/kitty-crash-test-no-3-o.gif  &RED"+e.toString();
		}
	}

	@Override
	public String[] getAliases()
	{
		return new String[]{"image","images"};
	}

	@Override
	public Level getAccessLevel()
	{
		return Level.USER;
	}

	@Override
	public String getHelp() {
		return "Usage: image <subreddit>  | Result: Returns a link of a picture from the desired subreddit";
	}

}
