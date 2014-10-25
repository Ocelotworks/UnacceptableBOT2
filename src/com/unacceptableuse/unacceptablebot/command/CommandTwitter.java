package com.unacceptableuse.unacceptablebot.command;

import java.util.List;

import org.pircbotx.Channel;
import org.pircbotx.User;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class CommandTwitter extends Command
{

	@Override
	public String[] getAliases()
	{
		return new String[] { "twitter" };
	}

	@Override
	public String getHelp()
	{
		return "Twitter API hook";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{
		try
		{
			final ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setDebugEnabled(true).setOAuthConsumerKey("qkjJsZ6OfDaOGN0HHuqXUitsb").setOAuthConsumerSecret(UnacceptableBot.getConfigHandler().getString("twitPrivKey")).setOAuthAccessToken("https://api.twitter.com/oauth/access_token").setOAuthAccessTokenSecret("https://api.twitter.com/oauth/authorize");
			final Twitter unauthenticatedTwitter = new TwitterFactory(cb.build()).getInstance();
			// First param of Paging() is the page number, second is the number
			// per page (this is capped around 200 I think.
			final Paging paging = new Paging(1, 100);
			final List<Status> statuses = unauthenticatedTwitter.getUserTimeline(args[0], paging);
			sendMessage(statuses.get(0).getText(), channel);
		} catch (final TwitterException e)
		{
			e.printStackTrace();
		}
	}

}
