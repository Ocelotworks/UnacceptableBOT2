package com.unacceptableuse.unacceptablebot.command;

import java.io.InputStreamReader;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class CommandGitInfo extends Command
{

	@Override
	public String[] getAliases()
	{
		return new String[] { "gitinfo" };
	}

	@Override
	public String getHelp()
	{
		return "<username> - Shows infomation on the git user";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{
		final String user = args[1];
		final JsonObject jo = UnacceptableBot.getParser().parse(new InputStreamReader(UnacceptableBot.getUrlContents("https://osrc.dfm.io/" + user + ".json"))).getAsJsonObject();

		try
		{
			if (user.contains("/"))
			{
				final JsonArray contributors = jo.get("contributors").getAsJsonArray();
				StringBuilder stb = new StringBuilder();
				for (int i = 0; i < contributors.size(); i++)
				{
					final JsonObject contributor = contributors.get(i).getAsJsonObject();
					stb.append(contributor.get("name").getAsString() + " made " + contributor.get("count") + " changes" + (i < (contributors.size() - 1) ? i == (contributors.size() - 2) ? " and " : ", " : "."));
				}
				sendMessage(stb.toString(), channel);

				final JsonArray recommendations = jo.get("recommendations").getAsJsonArray();
				stb = new StringBuilder();
				stb.append("Similar to " + user + " are: ");
				for (int i = 0; i < recommendations.size(); i++)
				{
					final String recomendation = recommendations.get(i).getAsString();
					stb.append(recomendation + (i < (recommendations.size() - 1) ? i == (recommendations.size() - 2) ? " and " : ", " : "."));
				}
				sendMessage(stb.toString(), channel);
			} else
			{
				// ACTIVE REPOS
				final JsonArray repos = jo.get("repositories").getAsJsonArray();
				StringBuilder stb = new StringBuilder();
				stb.append(user + " is most actively contributing to ");
				for (int i = 0; i < repos.size(); i++)
					stb.append(repos.get(i).getAsJsonObject().get("repo").getAsString() + (i < (repos.size() - 1) ? i == (repos.size() - 2) ? " and " : ", " : "."));
				sendMessage(stb.toString(), channel);

				// CONNECTED USERS
				final JsonArray friends = jo.get("connected_users").getAsJsonArray();
				stb = new StringBuilder();
				stb.append(user + " is probably friends with ");
				for (int i = 0; i < friends.size(); i++)
					stb.append(friends.get(i).getAsJsonObject().get("name").getAsString() + (i < (friends.size() - 1) ? i == (friends.size() - 2) ? " and " : ", " : "."));
				sendMessage(stb.toString(), channel);

				// ACTIVITY
				final JsonArray activity = jo.get("usage").getAsJsonObject().get("events").getAsJsonArray();
				stb = new StringBuilder();
				stb.append(user + " has ");
				for (int i = 0; i < activity.size(); i++)
				{
					final JsonObject event = activity.get(i).getAsJsonObject();
					switch (event.get("type").getAsString())
					{
					case "PushEvent":
						stb.append("pushed " + event.get("total").getAsInt() + " commits");
						break;
					case "IssuesEvent":
						stb.append("dealt with " + event.get("total").getAsInt() + " issues");
						break;
					case "IssueCommentEvent":
						stb.append("commented on " + event.get("total").getAsInt() + " issues");
						break;
					case "CreateEvent":
						stb.append("created " + event.get("total").getAsInt() + " repos");
						break;
					default:
						stb.append(event.get("total").getAsInt() + " other things");
						break;
					}
					stb.append(i < (activity.size() - 1) ? i == (activity.size() - 2) ? " and " : ", " : ".");
				}
				sendMessage(stb.toString(), channel);
			}

		} catch (final Exception e)
		{
			e.printStackTrace();
			sendMessage(e.toString() + ": " + jo.get("message").getAsString(), channel);
		}

	}

	@Override
	public int requiredArguments()
	{
		return 1;
	}

}
