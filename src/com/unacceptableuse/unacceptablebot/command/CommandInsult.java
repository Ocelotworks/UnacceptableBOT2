package com.unacceptableuse.unacceptablebot.command;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.pircbotx.Channel;
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
	public String[] getAliases()
	{

		return new String[] { "insult" };
	}

	@Override
	public String getHelp()
	{
		return "Usage: insult [user] | Result: Generates an insult";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{
		final InputStream is = UnacceptableBot.getUrlContents("http://www.insultme.co/scripts/insult.php");
		try
		{
			final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			final Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();

			final Node nNode = doc.getElementsByTagName("result").item(0);
			sendMessage(args.length > 1 ? nNode.getAttributes().getNamedItem("title").getNodeValue().replace("You're", args[1] + " is") : nNode.getAttributes().getNamedItem("title").getNodeValue().replace("UnacceptableBOT", "I'm"), channel);

		} catch (final Exception e)
		{
			sendMessage("ERROR: You're a bot crashing " + e.getMessage(), channel);
			e.printStackTrace();
		}
	}

}
