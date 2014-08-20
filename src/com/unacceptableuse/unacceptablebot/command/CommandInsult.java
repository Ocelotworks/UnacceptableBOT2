package com.unacceptableuse.unacceptablebot.command;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

/**
 * 
 * @author Peter
 *
 */
public class CommandInsult extends Command
{

	@Override
	public void performCommand(User sender, Channel channel, String message,
			String[] args, PircBotX bot)
	{
		InputStream is = UnacceptableBot.getUrlContents("http://www.insultme.co/scripts/insult.php");
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();
			
			Node nNode = doc.getElementsByTagName("result").item(0);
			sendMessage(bot, args.length > 1 ? nNode.getAttributes().getNamedItem("title").getNodeValue().replace("You're", args[1]+" is") : nNode.getAttributes().getNamedItem("title").getNodeValue().replace("UnacceptableBOT", "I'm"), channel);
		
		}catch(Exception e)
		{
			sendMessage(bot, "ERROR: You're a bot crashing "+e.getMessage(), channel);
			e.printStackTrace();
		}
	}

	@Override
	public String[] getAliases()
	{
		
		return new String[]{"insult"};
	}

}
