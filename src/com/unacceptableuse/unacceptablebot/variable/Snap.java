package com.unacceptableuse.unacceptablebot.variable;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class Snap
{

	String channelRequested = "";
	String imageAddress = "";
	String imageName = "";
	public boolean sent = false;
	String toUser = "";
	String userSentBy = "";

	public Snap(final String user, final String address, final String name, final String channel, final String sender)
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
