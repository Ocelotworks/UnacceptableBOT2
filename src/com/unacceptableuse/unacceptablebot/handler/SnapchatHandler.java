package com.unacceptableuse.unacceptablebot.handler;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import javax.imageio.ImageIO;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Random;
import com.jcraft.jsch.Session;
import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class SnapchatHandler {
	
	private String user;
	private String pass;
	private boolean _loggedIn = false;

	public void init() {
		String user = "StevieBOT";
		String loadpass = UnacceptableBot.getConfigHandler().getString(
				"sc_password");
		pass = loadpass;
		try {
			login(user, pass);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (logged()) {
			System.out.println("Snapchat logged in as " + user);
		} else {
			System.out.println("Snapchat login failed!");
		}
	}
	
	public void getImage(String strURL, String target){
		try {
		    URL url = new URL(strURL);
		    InputStream in = new BufferedInputStream(url.openStream());
		    ByteArrayOutputStream out = new ByteArrayOutputStream();
		    byte[] buf = new byte[1024];
		    int n = 0;
		    while (-1!=(n=in.read(buf)))
		    {
		       out.write(buf, 0, n);
		    }
		    out.close();
		    in.close();
		    byte[] response = out.toByteArray();
		    FileOutputStream fos = new FileOutputStream("temp.jpg");
		    fos.write(response);
		    fos.close();
		    this.upload(this, "temp.jgp", target);
		    new File("temp.jpg").delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void login(String user, String pass) throws IOException {

		// build a URL
		String s = " http://boywanders.us:6969/snapapi.php?username=" + user
				+ "&password=" + pass + "&method=userinfo";
		URL url = new URL(s);

		// read from the URL
		Scanner scan = new Scanner(url.openStream());
		String str = new String();
		while (scan.hasNext())
			str += scan.nextLine();
		scan.close();

		// build a JSON object
		JsonParser parser = new JsonParser();
		JsonObject obj = (JsonObject) parser.parse(str);
		if (obj.get("auth_token") != null) {
			//String auth_token = obj.get("auth_token").toString();
			_loggedIn = true;
		} else {
			_loggedIn = false;
		}

	}

	public void post(String imageName, String target) throws Exception {

		String s = "http://boywanders.us:6969/snapapi.php?username=" + getUser()
				+ "&password=" + pass + "&method=sendsnap&variables=target:"
				+ target + ";;time:" + 10 + ";;image:" + imageName;
		URL url = new URL(s);

		// read from the URL
		Scanner scan = new Scanner(url.openStream());
		String str = new String();
		while (scan.hasNext()) {
			str += scan.nextLine();
		}
		scan.close();

		// build a JSON object
		JsonParser parser = new JsonParser();
		JsonObject obj = (JsonObject) parser.parse(str);
		String status = obj.get("status").toString();
		System.out.println(status);
	}

	public void upload(SnapchatHandler sc, String fileName, String target) {
		String hostname = "boywanders.us";
		int port = 22;
		String user = "api";
		String keyFilePath = "/api.pem";
		String workingDir = "/home/api/nginxhtml/upload/";

		Session session = null;
		Channel channel = null;
		ChannelSftp channelSFTP = null;

		try {
			JSch jsch = new JSch();
			jsch.addIdentity(keyFilePath);
			session = jsch.getSession(user, hostname, port);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			channelSFTP = (ChannelSftp) channel;
			channelSFTP.cd(workingDir);
			File f = new File(fileName);
			channelSFTP.put(new FileInputStream(f), f.getName());

			sc.post(fileName, target);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		session.disconnect();
		channel.disconnect();
	}

	public boolean logged() {
		return _loggedIn;
	}

	public String getUser() {
		return user;
	}
}
