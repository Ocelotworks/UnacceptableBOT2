package com.unacceptableuse.unacceptablebot.variable;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class Snap {
	
	String toUser = "";
	String imageAddress = "";
	String imageName = "";
	public boolean sent = false;

	public Snap(String user, String address, String name) {
		toUser = user;
		imageAddress = address;
		imageName = name;
	}
	
	public void send(){
		UnacceptableBot.getSnapchat().getImage(imageAddress, toUser);
	}

}
