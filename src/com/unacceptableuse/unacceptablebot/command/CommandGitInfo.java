package com.unacceptableuse.unacceptablebot.command;

import java.io.InputStreamReader;

import org.pircbotx.Channel;
import org.pircbotx.User;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class CommandGitInfo extends Command
{

	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args)
	{
		String user = args[1];
		JsonParser parser = new JsonParser();
		
		JsonObject jo = parser.parse(new InputStreamReader(UnacceptableBot.getUrlContents("https://osrc.dfm.io/"+user+".json"))).getAsJsonObject();
		
		try{
			//ACTIVE REPOS
			JsonArray repos = jo.get("repositories").getAsJsonArray();
			StringBuilder stb = new StringBuilder();
			stb.append(user+" is most actively contributing to ");
			for(int i = 0; i < repos.size(); i++)
			{
				stb.append(repos.get(i).getAsJsonObject().get("repo").getAsString()+(i < repos.size() ? i == repos.size()-1 ? " and " : ", " : "."));
			}
			sendMessage(stb.toString(), channel);
			
			
			//CONNECTED USERS
			JsonArray friends = jo.get("connected_users").getAsJsonArray();
			stb = new StringBuilder();
			stb.append(user+" is probably friends with ");
			for(int i = 0; i < friends.size(); i++)
			{
				stb.append(friends.get(i).getAsJsonObject().get("name").getAsString()+(i < friends.size() ? i == friends.size()-1 ? " and " : ", " : "."));
			}
			sendMessage(stb.toString(), channel);
			
			
			//ACTIVITY
			JsonArray activity = jo.get("usage").getAsJsonObject().get("events").getAsJsonArray();
			stb = new StringBuilder();
			stb.append(user+" has ");
			for(int i = 0; i < activity.size(); i++)
			{
				JsonObject event = activity.get(i).getAsJsonObject();
				switch(event.get("type").getAsString())
				{
				case "PushEvent":
					stb.append("pushed "+event.get("total").getAsInt()+" commits");
					break;
				case "IssuesEvent":
					stb.append("dealt with "+event.get("total").getAsInt()+" issues");
					break;
				case "IssueCommentEvent":
					stb.append("commented on "+event.get("total").getAsInt()+" issues");
					break;
				case "CreateEvent":
					stb.append("created "+event.get("total").getAsInt()+" repos");
					break;
				}
				stb.append(i < friends.size() ? i == friends.size()-1 ? " and " : ", " : ".");	
			}
			sendMessage(stb.toString(), channel);
			
		}catch(Exception e)
		{
			e.printStackTrace();
			sendMessage(e.toString()+": "+jo.get("message").getAsString(), channel);
		}

	}

	@Override
	public String[] getAliases()
	{
		return new String[]{"gitinf"};
	}

	@Override
	public String getHelp()
	{
		return "<username> - Shows infomation on the git user";
	}
	
	@Override
	public int requiredArguments()
	{
		return 1;
	}

}
