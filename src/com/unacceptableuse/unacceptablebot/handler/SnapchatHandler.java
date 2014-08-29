package com.unacceptableuse.unacceptablebot.handler;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.pircbotx.PircBotX;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class SnapchatHandler {

	private String user;
	private String pass;
	String fileName;
	private boolean _loggedIn = false;
	StringBuilder stb;
	public ArrayList<String> add = new ArrayList<String>();
	public ArrayList<String> targ = new ArrayList<String>();
	public ArrayList<Boolean> complete = new ArrayList<Boolean>();

	public void init() throws Exception {
		user = "Stevie-BOT";
		this.pass = UnacceptableBot.getConfigHandler().getString("sc_password");

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

	public void getImage(String strURL, String target) {
		try {
			Random rn = new Random();
			int number = rn.nextInt(9999 - 1 + 1) + 1;
			fileName = "temp" + number;
			if (strURL.substring(strURL.lastIndexOf(".")).equals(".png")) {
				convertToJPG(strURL);
			} else {
				URL url = new URL(strURL);
				InputStream in = new BufferedInputStream(url.openStream());
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buf = new byte[8192];
				int n = 0;
				while (-1 != (n = in.read(buf))) {
					out.write(buf, 0, n);
				}
				out.close();
				in.close();
				byte[] response = out.toByteArray();
				FileOutputStream fos = new FileOutputStream(fileName + ".jpg");
				fos.write(response);
				fos.close();
			}
			upload(fileName + ".jpg", target, user, pass);
			if (new File(fileName + ".png").exists()) {
				new File(fileName + ".png").delete();
			}
			new File(fileName + ".jpg").delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void convertToJPG(String pngURL) {
		BufferedImage bufferedImage;
		try {
			URL url = new URL(pngURL);
			InputStream in = new BufferedInputStream(url.openStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[8192];
			int n = 0;
			while (-1 != (n = in.read(buf))) {
				out.write(buf, 0, n);
			}
			out.close();
			in.close();
			byte[] response = out.toByteArray();
			FileOutputStream fos = new FileOutputStream(fileName + ".png");
			fos.write(response);
			fos.close();
			// read image file
			bufferedImage = ImageIO.read(new File(fileName + ".png"));

			// create a blank, RGB, same width and height, and a white
			// background
			BufferedImage newBufferedImage = new BufferedImage(
					bufferedImage.getWidth(), bufferedImage.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0,
					Color.WHITE, null);

			// write to jpeg file
			ImageIO.write(newBufferedImage, "jpg", new File(fileName + ".jpg"));
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
			// String auth_token = obj.get("auth_token").toString();
			_loggedIn = true;
		} else {
			_loggedIn = false;
		}

	}

	public static void post(String imageName, String target, int time,
			String user, String pass) throws Exception {

		String s = "http://boywanders.us:6969/snapapi.php?username=" + user
				+ "&password=" + pass + "&method=sendsnap&variables=target:"
				+ target + ";;time:" + time + ";;image:" + imageName;
		URL url = new URL(s);

		// read from the URL
		Scanner scan = new Scanner(url.openStream());
		@SuppressWarnings("unused")
		String str = new String();
		while (scan.hasNext()) {
			str += scan.nextLine();
		}
		scan.close();

		// GET SNAP COMPLETEION. LATER
	}

	public static void upload(String fileName, String target, String scUser,
			String pass) throws FileNotFoundException {
		String hostname = "boywanders.us";
		int port = 22;
		String user = "api";
		String keyFilePath = "api.pem";
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

			post(fileName, target, 10, scUser, pass);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		session.disconnect();
		channel.disconnect();
	}
	
	public void doOneInQueue(PircBotX bot, String channel) {
		for (int i = 0; i < add.size(); i++) {
			if (UnacceptableBot.getSnapchat().complete.get(i) == false) {
				UnacceptableBot.getSnapchat().getImage(
						UnacceptableBot.getSnapchat().add.get(i),
						UnacceptableBot.getSnapchat().targ.get(i));
				bot.sendIRC().message(channel, "Snap sent.");
				UnacceptableBot.getSnapchat().complete.set(i, true);
				break;
			}
		}
	}

	public byte[] getSnaps() throws IOException {
		// build a URL
		String s = " http://boywanders.us:6969/snapapi.php?username=" + user
				+ "&password=" + pass + "&method=getsnaps";
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
		if (obj.get("received_snaps") != null) {
			for (int i = 0; i < obj.get("received_snaps").getAsJsonArray()
					.size(); i++) {
				JsonArray objArray = obj.get("received_snaps").getAsJsonArray();
				JsonObject snapObj = (JsonObject) objArray.get(i);
				return Base64.decodeBase64(snapObj.toString());
			}
		}
		return new byte[]{0};
	}

	public boolean logged() {
		return _loggedIn;
	}

	public String getUser() {
		return user;
	}

	public String getPass() {
		String pass = UnacceptableBot.getConfigHandler().getString(
				"sc_password");
		return pass;
	}
}
