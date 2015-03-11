package com.unacceptableuse.unacceptablebot.command;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class CommandPersonality extends Command
{

	@Override
	public String[] getAliases()
	{
		return new String[]{"personality"};
	}

	@Override
	public String getHelp()
	{
		return "<username> - Returns an analysis of their personality from their last 1000 messages.";
	}

	@Override
	public void performCommand(User sender, Channel channel, String message, String[] args)
	{
		
		StringBuilder payload = new StringBuilder();
		
		
		try{
			
			//This bit grabs the last 1000 messages and puts them into a string for sending off
			PreparedStatement ps = UnacceptableBot.getConfigHandler().sql.getPreparedStatement("SELECT `Message` FROM `stevie`.`"+channel.getName()+"` WHERE `Username` = '"+args[1]+"' ORDER BY `ID` DESC;");
			
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				payload.append(rs.getString(1)+"(\n");
			}
			
			//This bit connects to the API and retrieves the analysis
			URL url = new URL("http://watson-um-demo.mybluemix.net/v2/profile");
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
	        connection.setDoOutput(true); 
	        connection.setDoInput(true);
	        connection.setInstanceFollowRedirects(false); 
	        connection.setRequestMethod("POST"); 
	        connection.setRequestProperty("Content-Type", "text/plain"); 

	        OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
	        osw.write(payload.toString());
	        osw.flush(); 
	        
	        //This bit reads the returned data and parses it as JSON
	        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        JsonObject mainObject = UnacceptableBot.getParser().parse(br).getAsJsonObject();
	        br.close();
	        
	        connection.disconnect(); 
	        
	        //This is where we actually handle the JSON
	        StringBuilder output = new StringBuilder();
	        JsonArray tree = mainObject.get("tree").getAsJsonObject().get("children").getAsJsonArray();
	        
	        for(JsonElement child : tree){
	        	JsonObject childObject = child.getAsJsonObject();
	        	
	        	output.append(childObject.get("name").getAsString()+": ");
	        	
	        	JsonObject biggestTrait = childObject.get("children").getAsJsonArray().get(0).getAsJsonObject();
	        	output.append(biggestTrait.get("name").getAsString()+" "+Colors.BOLD+(biggestTrait.get("percentage").getAsFloat()*100)+"%"+Colors.NORMAL+" / ");
	        }

	        
	        sendMessage(output.toString(), channel);
	        
		}catch(Exception e){
				sendMessage(e.toString(), channel);
		}
	}
	
	@Override
	public int requiredArguments(){
		return 1;
	}

}
