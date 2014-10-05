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

public class CommandTwitter extends Command {

	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args) {
		try {
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setDebugEnabled(true).setOAuthConsumerKey("")
					.setOAuthConsumerSecret("").setOAuthAccessToken("")
					.setOAuthAccessTokenSecret("");
			Twitter unauthenticatedTwitter = new TwitterFactory(cb.build()).getInstance();
			// First param of Paging() is the page number, second is the number
			// per page (this is capped around 200 I think.
			Paging paging = new Paging(1, 100);
			List<Status> statuses = unauthenticatedTwitter.getUserTimeline(
					args[0], paging);
			sendMessage(statuses.get(0).getText(), channel);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String[] getAliases() {
		return new String[] { "twitter" };
	}

	@Override
	public String getHelp() {
		return "Twitter API hook";
	}

}
