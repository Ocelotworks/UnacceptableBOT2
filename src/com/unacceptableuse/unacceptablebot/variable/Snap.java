package com.unacceptableuse.unacceptablebot.variable;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class Snap
{

	String toUser = "";
	String imageAddress = "";
	String imageName = "";
	String channelRequested = "";
	String userSentBy = "";
	public boolean sent = false;

	public Snap(String user, String address, String name, String channel, String sender)
	{
		toUser = user;
		imageAddress = address;
		imageName = name;
		channelRequested = channel;
		userSentBy = sender;
	}

	public void send()
	{
		UnacceptableBot.getSnapchat().getImage(imageAddress, toUser);
		UnacceptableBot.getBot().sendIRC().message(channelRequested, userSentBy.concat(" : A snap has been sent!"));
	}

}
