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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.variable.Snap;

public class SnapchatHandler
{

	public static void post(final String imageName, final String target, final int time, final String user, final String pass) throws Exception
	{

		final String s = "http://boywanders.us:6969/snapapi.php?username=" + user + "&password=" + pass + "&method=sendsnap&variables=target:" + target + ";;time:" + time + ";;image:" + imageName;
		final URL url = new URL(s);

		// read from the URL
		final Scanner scan = new Scanner(url.openStream());
		new String();
		while (scan.hasNext())
			scan.nextLine();
		scan.close();

		// GET SNAP COMPLETEION. LATER
	}

	public static void upload(final String fileName, final String target, final String scUser, final String pass) throws FileNotFoundException
	{
		final String hostname = "boywanders.us";
		final int port = 22;
		final String user = "api";
		final String keyFilePath = "api.pem";
		final String workingDir = "/home/api/nginxhtml/upload/";

		Session session = null;
		Channel channel = null;
		ChannelSftp channelSFTP = null;

		try
		{
			final JSch jsch = new JSch();
			jsch.addIdentity(keyFilePath);
			session = jsch.getSession(user, hostname, port);
			final java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			channelSFTP = (ChannelSftp) channel;
			channelSFTP.cd(workingDir);
			final File f = new File(fileName);
			channelSFTP.put(new FileInputStream(f), f.getName());

			post(fileName, target, 10, scUser, pass);
		} catch (final Exception ex)
		{
			ex.printStackTrace();
		}
		session.disconnect();
		channel.disconnect();
	}

	private boolean _loggedIn = false;
	String fileName;
	private String pass;
	ArrayList<Snap> snaps = new ArrayList<Snap>();

	StringBuilder stb;

	private String user;

	public void addSnap(final Snap snap)
	{
		snaps.add(snap);
	}

	public void convertToJPG(final String pngURL)
	{
		BufferedImage bufferedImage;
		try
		{
			final URL url = new URL(pngURL);
			final InputStream in = new BufferedInputStream(url.openStream());
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			final byte[] buf = new byte[8192];
			int n = 0;
			while (-1 != (n = in.read(buf)))
				out.write(buf, 0, n);
			out.close();
			in.close();
			final byte[] response = out.toByteArray();
			final FileOutputStream fos = new FileOutputStream(fileName + ".png");
			fos.write(response);
			fos.close();
			// read image file
			bufferedImage = ImageIO.read(new File(fileName + ".png"));

			// create a blank, RGB, same width and height, and a white
			// background
			final BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
			newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);

			// write to jpeg file
			ImageIO.write(newBufferedImage, "jpg", new File(fileName + ".jpg"));
		} catch (final IOException e)
		{
			e.printStackTrace();
		}
	}

	public void doOneInQueue()
	{
		for (final Snap snap : snaps)
			if (!snap.sent)
			{
				snap.send();
				snap.sent = true;
				break;
			}
	}

	public void getImage(final String strURL, final String target)
	{
		try
		{
			final Random rn = new Random();
			final int number = rn.nextInt(10000);
			fileName = "temp" + number;
			if (strURL.substring(strURL.lastIndexOf(".")).equals(".png"))
				convertToJPG(strURL);
			else
			{
				final URL url = new URL(strURL);
				final InputStream in = new BufferedInputStream(url.openStream());
				final ByteArrayOutputStream out = new ByteArrayOutputStream();
				final byte[] buf = new byte[8192];
				int n = 0;
				while (-1 != (n = in.read(buf)))
					out.write(buf, 0, n);
				out.close();
				in.close();
				final byte[] response = out.toByteArray();
				final FileOutputStream fos = new FileOutputStream(fileName + ".jpg");
				fos.write(response);
				fos.close();
			}
			upload(fileName + ".jpg", target, user, pass);
			final boolean pngSuccess = new File(fileName + ".png").delete();
			final boolean jpgSuccess = new File(fileName + ".jpg").delete();
			if (!jpgSuccess && !pngSuccess)
				System.out.print("Failed to delete sent snap files:" + " File.delete() provides no insight into why, so that's all I got, sorry");

		} catch (final IOException e)
		{
			e.printStackTrace();
		}
	}

	public String getPass()
	{
		final String pass = UnacceptableBot.getConfigHandler().getString("sc_password");
		return pass;
	}

	@SuppressWarnings("unused")
	public byte[] getSnaps() throws IOException
	{
		// build a URL
		final String s = " http://boywanders.us:6969/snapapi.php?username=" + user + "&password=" + pass + "&method=getsnaps";
		final URL url = new URL(s);

		// read from the URL
		final Scanner scan = new Scanner(url.openStream());
		String str = new String();
		while (scan.hasNext())
			str += scan.nextLine();
		scan.close();

		// build a JSON object
		final JsonParser parser = new JsonParser();
		final JsonObject obj = (JsonObject) parser.parse(str);
		if (obj.get("received_snaps") != null)
			for (int i = 0; i < obj.get("received_snaps").getAsJsonArray().size(); i++)
			{
				final JsonArray objArray = obj.get("received_snaps").getAsJsonArray();
				final JsonObject snapObj = (JsonObject) objArray.get(i);
				return Base64.decodeBase64(snapObj.toString());
			}
		return new byte[] { 0 };
	}

	public String getUser()
	{
		return user;
	}

	public void init() throws Exception
	{
		user = "Stevie-BOT";
		pass = UnacceptableBot.getConfigHandler().getString("sc_password");

		try
		{
			login(user, pass);
		} catch (final IOException e)
		{
			e.printStackTrace();
		}

		if (logged())
			System.out.println("Snapchat logged in as " + user);
		else
			System.out.println("Snapchat login failed!");
	}

	public boolean logged()
	{
		return _loggedIn;
	}

	public void login(final String user, final String pass) throws IOException
	{

		// build a URL
		final String s = " http://boywanders.us:6969/snapapi.php?username=" + user + "&password=" + pass + "&method=userinfo";
		final URL url = new URL(s);

		// read from the URL
		final Scanner scan = new Scanner(url.openStream());
		String str = new String();
		while (scan.hasNext())
			str += scan.nextLine();
		scan.close();

		// build a JSON object
		final JsonParser parser = new JsonParser();
		final JsonObject obj = (JsonObject) parser.parse(str);
		if (obj.get("auth_token") != null)
			// String auth_token = obj.get("auth_token").toString();
			_loggedIn = true;
		else
			_loggedIn = false;

	}
}
