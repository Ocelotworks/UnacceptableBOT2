package com.unacceptableuse.unacceptablebot;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.InviteEvent;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.pircbotx.hooks.events.QuitEvent;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unacceptableuse.unacceptablebot.handler.CommandHandler;
import com.unacceptableuse.unacceptablebot.handler.ConfigHandler;
import com.unacceptableuse.unacceptablebot.handler.LotteryHandler;
import com.unacceptableuse.unacceptablebot.handler.SnapchatHandler;
import com.unacceptableuse.unacceptablebot.handler.SpellCheckHandler;
import com.unacceptableuse.unacceptablebot.handler.WebSocketHandler;
import com.unacceptableuse.unacceptablebot.threading.SnapchatThread;
import com.unacceptableuse.unacceptablebot.variable.HealthStatus;

@SuppressWarnings("rawtypes")
public class UnacceptableBot extends ListenerAdapter
{

	private static PircBotX bot = null;
	public static ArrayList<String> channels = new ArrayList<String>();
	private static ConfigHandler config = new ConfigHandler();
	private static CommandHandler handler = new CommandHandler();
	private static JsonParser parser;
	public static Random rand = new Random();
	public static HashMap<String, ArrayList<String>> relay = new HashMap<String, ArrayList<String>>();
	public static ArrayList<String> sexQuotes = new ArrayList<String>();
	private static SnapchatHandler snapchat = new SnapchatHandler();
	private static WebSocketHandler socks = new WebSocketHandler();
	private static LotteryHandler lottery = new LotteryHandler();
	private static Timer timer = null;
	public static boolean twatMode = false;
	private static HealthStatus ZNCStatus = new HealthStatus("ZNC", "Connected", "export");

	private static void doReddit(final String message, final String channel, final User sender)
	{
		// TODO: fancy regex for this lol still not done it
		if (message.contains("whoop there it is") || message.contains("whoop, there it is"))
		{
			log("INFO", "REDDIT", "Whoop there it is detected");
			bot.sendIRC().message(channel, Colors.BOLD + "WHO THE FUCK SAID THAT?");
		}
		try
		{

			if (message.contains("/r/") && (config.getUserLevel(sender) >= 0))
				if (message.contains("reddit.com") && message.contains("/comments/"))
				{
					log("INFO", "REDDIT", "Reddit link " + message);
					final String reddit = "http://api." + message.substring(message.indexOf("reddit.com"), message.lastIndexOf("/"));
					final InputStream is = getUrlContents(reddit);
					final JsonParser parser = new JsonParser();
					final JsonArray ja = parser.parse(new InputStreamReader(is)).getAsJsonArray();

					final JsonObject data = ja.get(0).getAsJsonObject().get("data").getAsJsonObject().get("children").getAsJsonArray().get(0).getAsJsonObject().get("data").getAsJsonObject(); // Holy JSON batman

					String title = data.get("title").getAsString();
					while (title.charAt(0) == '.' || title.charAt(0) == '!')
						if (title.charAt(0) == '.')
							title = title.replaceFirst(Pattern.quote("."), "");
						else if (title.charAt(0) == '!')
							title = title.replaceFirst(Pattern.quote("!"), "");

					bot.sendIRC().message(channel, Colors.BOLD + title + Colors.NORMAL + " (" + data.get("domain").getAsString() + (data.get("over_18").getAsBoolean() ? ") " + Colors.RED + "NSFW" : ")"));

				} else
				{
					log("INFO", "REDDIT", "Subreddit " + message);
					final String subreddit = message.split("/r/")[1].split(" ")[0];
					final InputStream is = getUrlContents("http://api.reddit.com/r/" + subreddit.replace(",", "").replace(".", "") + "/about");
					final JsonParser parser = new JsonParser();

					final String subredditDesc = parser.parse(new InputStreamReader(is)).getAsJsonObject().get("data").getAsJsonObject().get("public_description").getAsString();
					bot.sendIRC().message(channel, Colors.BOLD + "http://reddit.com/r/" + subreddit + " - " + subredditDesc);
				}

			if (message.contains("/u/") && !message.contains("reddit.com") && (config.getUserLevel(sender) >= 0))
			{
				log("INFO", "REDDIT", "Redditor " + message);
				final String user = message.split("/u/")[1].split(" ")[0];
				final InputStream is = getUrlContents("http://api.reddit.com/u/" + user.replace(",", "").replace(".", "") + "/about");
				final com.google.gson.JsonParser parser = new com.google.gson.JsonParser();

				final JsonObject jo = parser.parse(new InputStreamReader(is)).getAsJsonObject().get("data").getAsJsonObject();

				final int linkKarma = jo.get("link_karma").getAsInt();
				final int commentKarma = jo.get("comment_karma").getAsInt();
				bot.sendIRC().message(channel, Colors.NORMAL + Colors.BOLD + "http://reddit.com/u/" + user + " - " + linkKarma + " Link Karma. " + commentKarma + " Comment Karma.");
			}
		} catch (final Exception e){}
	}

	private static void doYoutube(final String message, final String channel)
	{
		final String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";

		final Pattern compiledPattern = Pattern.compile(pattern);
		final Matcher matcher = compiledPattern.matcher(message);

		if (matcher.find())
			try
		{
				final InputStream is = getHTTPSUrlContents("https://gdata.youtube.com/feeds/api/videos/" + matcher.group().replace("", "").replace(",", "").replace(".", "").split(" ")[0] + "?v=2&alt=json");
				final com.google.gson.JsonParser parser = new com.google.gson.JsonParser();

				final JsonObject jo = parser.parse(new InputStreamReader(is)).getAsJsonObject().get("entry").getAsJsonObject();
				final String title = jo.get("title").getAsJsonObject().get("$t").getAsString();
				//final float mins = Float.parseFloat(jo.get("media$group").getAsJsonObject().get("yt$duration").getAsString()) / 60;
				bot.sendIRC().message(channel, Colors.BOLD + "Youtube link: " + title);
				is.close();
		} catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	public static PircBotX getBot()
	{
		return bot;
	}

	public static ArrayList<String> getChannels()
	{
		return channels;
	}

	public static CommandHandler getCommandHandler()
	{
		return handler;
	}

	public static ConfigHandler getConfigHandler()
	{
		return config;
	}
	 public static LotteryHandler getLotteryHandler()
	 {
		 return lottery;
	 }

	public static InputStream getHTTPSUrlContents(final String surl)
	{
		URL url;

		try
		{
			url = new URL(surl);
			final HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			final InputStream is = conn.getInputStream();

			return is;

		} catch (final Exception e)
		{
			log("ERROR", "getHTTPSUrlContents", "Unable to connect to " + surl + ": " + e.toString());
			e.printStackTrace();
		}
		return null;
	}

	public static JsonParser getParser()
	{
		return parser;
	}

	public static SnapchatHandler getSnapchat()
	{
		return snapchat;
	}

	public static InputStream getUrlContents(final String surl)
	{
		/*
		 * Instead of: InputStream is = getUrlContents(url); parser.parse(new InputStreamReader(is));
		 *
		 * You can use: parser.parse(new URL(url).openStream())
		 *
		 * And instead of creating a new JsonParser before every parse, you can make a static global parser.
		 */
		URL url;

		try
		{
			url = new URL(surl);
			final URLConnection conn = url.openConnection();
			final InputStream is = conn.getInputStream();

			return is;
		} catch (final Exception e)
		{
			log("ERROR", "getUrlContents", "Unable to connect to " + surl + ": " + e.toString());
			e.printStackTrace();
		}
		return null;
	}

	public static WebSocketHandler getWebSocketHandler()
	{
		return socks;
	}

	public static HealthStatus getZNCStatus()
	{
		return ZNCStatus;
	}

	private static void handleNewBuild()
	{
		config.increment("build");
		System.out.println("Is build " + config.getInteger("build"));

		// TODO: Help file uploading

	}

	public static void log(final String level, final String origin, final String message)
	{
		
		getWebSocketHandler().logMessage("[" + level + "]" + " " + message);
		System.out.println("[" + level + "]" + " " + message);
		if(level.equals("WARN"))
			getBot().sendIRC().message("Peter", "["+origin+"] "+message);

		// try {
		// getConfigHandler().createChannelTable("SYSTEM");
		// getConfigHandler().setLog(new Date().toString(), origin,
		// "[" + level + "]" + " " + message, "SYSTEM");
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

	}

	/**
	 *
	 * @param username
	 *            The nickname to auth as
	 * @param password
	 *            The password to auth with
	 */
	public static void nickAuth(final String username, final String password)
	{
		if (!getConfigHandler().getString("botName").equals(username)) // If the bot name set in the database is not equal to the one we need....
			getConfigHandler().setString("botName", username); // Then there must be a mistake somewhere... So we assume the database is wrong

		if (!getBot().getNick().equals(username)) // If the nickname isn't already the username we want
			getBot().sendIRC().changeNick(username); // Then we change it to what we are trying to authenticate as

		getBot().sendIRC().message("nickserv", "IDENTIFY " + password); // Actually authenticating

	}

	public static void setBot(final PircBotX bot)
	{
		UnacceptableBot.bot = bot;
	}

	public static void setSnapchat(final SnapchatHandler sc)
	{
		snapchat = sc;
	}

	private final boolean loadChansFromDB = false; // When using ZNC this should be false to avoid dual entries in the database!

	private int messageCount = 0;

	/**
	 * Starts the init process of everything
	 *
	 * @author UnacceptableUse
	 */
	public UnacceptableBot()
	{

		try
		{
			initHandlers();
		} catch (final Exception e)
		{
			log("SEVERE", "HINIT", "A handler failed to initialize! " + e.toString() + ". Attempting to continue, but it doesn't look good.");
			e.printStackTrace();
		}

		try
		{
			System.out.println("Generating MD5 Checksum...");
			final MessageDigest md = MessageDigest.getInstance("MD5");

			final InputStream is = Files.newInputStream(Paths.get("unacceptablebot2.jar"));
			final DigestInputStream dis = new DigestInputStream(is, md);

			final MessageDigest mdg = dis.getMessageDigest();

			final String newDigest = String.valueOf(mdg.digest());
			final String oldDigest = config.getString("checksum");

			System.out.println("MD5: " + newDigest);

			if (!newDigest.equals(oldDigest))
			{
				System.out.println("Detected new build...");
				config.setString("checksum", newDigest);
				handleNewBuild();
			}

		} catch (final Exception e)
		{
			System.err.println("There was an error generating the MD5 Checksum. Not sure if new build or not...");
			e.printStackTrace();
		}

		config.increment("stat:startups");
		config.setLong("startupTime", new Date().getTime());
		timer = new Timer();
		timer.scheduleAtFixedRate(new SnapchatThread(), 0, (20 * 1000));

		loadSexQuotes();

		if (loadChansFromDB)
		{
			final String channelsStr = config.getChannels();
			final String[] channels = channelsStr.split(",");
			config.setLong("connectedChannels", channels.length);
			for (final String channel : channels)
			{
				final String currentChannel = channel.replace(",", "");
				UnacceptableBot.channels.add(currentChannel);
			}
		}

	}

	public void doChannelSave()
	{
		final String chanStr = "";
		for (int i = 0; i < channels.size(); i++)
			chanStr.concat(",".concat(channels.get(i)));
		if (!chanStr.equals(""))
			config.setChannels(chanStr);
	}
	
	public void doLottery(final String message, final String channel, final User sender)
	{
		if(!channel.equals(getConfigHandler().getString("lott:channel")))return;
		if(!getBot().getUserBot().isVerified()) return; //Lottery is disabled if bot is not logged in
		
		if(message.startsWith(".tip "+getBot().getNick()) && message.endsWith("lottery"))
		{
			getBot().sendIRC().message(channel, sender+" just entered the lottery! Type .tip "+getBot().getNick()+" to enter!");
			
		}
		
	}
	
	public static void updateDogecoinBalance()
	{
		getBot().sendIRC().message("DogeWallet", ".balance");
	}

	/**
	 * Handlers should be started in order of priority, because I said so.
	 *
	 * @throws Exception
	 *             Any exceptions thrown by the handlers should be handled with extreme panic
	 */
	private void initHandlers() throws Exception
	{
		handler.init(); // CommandHandler
		config.init(); // ConfigHandler
		socks.init(); // WebSocketHandler
		snapchat.init(); // SnapchatHandler
		lottery.init();
		parser = new JsonParser();
	}

	private void loadSexQuotes()
	{
		try
		{
			final BufferedReader br = new BufferedReader(new FileReader(new File("sexquotes.txt")));
			String line = "missingno";
			while ((line = br.readLine()) != null)
				sexQuotes.add(line);
			br.close();

		} catch (final IOException e)
		{

			e.printStackTrace();
		}
	}

	@Override
	public void onInvite(final InviteEvent event)
	{
		log("INFO", "INVITE", event.getUser() + " invited bot to " + event.getChannel());
		event.getBot().sendIRC().joinChannel(event.getChannel());
	}

	@Override
	public void onJoin(final JoinEvent event)
	{
		if (relay.containsKey(event.getChannel().getName()))
			for (final String to : relay.get(event.getChannel().getName()))
				bot.sendIRC().message(to, event.getUser().getNick() + " joined " + event.getChannel().getName());

		if (event.getUser().equals(event.getBot().getUserBot()))
		{
			channels.add(event.getChannel().getName());
			log("INFO", "JOIN", "Joined channel " + event.getChannel().getName());
		}

	}

	@Override
	public void onMessage(final MessageEvent event) throws Exception
	{
		if (relay.containsKey(event.getChannel().getName().toLowerCase()) && !event.getMessage().startsWith("!stoprelay"))
		{
			final String user = event.getUser().getNick().toLowerCase();
			/*
			 * .replace("a", "ᴀ") .replace("b", "ʙ") .replace("b", "ᴄ") .replace("d", "ᴅ") .replace("e", "ᴇ") .replace("f", "f") .replace("g", "ɢ") .replace("h", "ʜ") .replace("i", "ɪ") .replace("j", "ᴊ") .replace("k", "ᴋ") .replace("l", "ʟ") .replace("m", "ᴍ") .replace("n", "ɴ") .replace("o", "ᴏ") .replace("p", "ᴘ") .replace("q", "q") .replace("r", "ʀ") .replace("s", "s") .replace("t", "ᴛ") .replace("u", "ᴜ") .replace("v", "ᴠ") .replace("w", "ᴡ") .replace("x", "x") .replace("y", "ʏ") .replace("z", "ᴢ");
			 */

			for (final String to : relay.get(event.getChannel().getName().toLowerCase()))
				bot.sendIRC().message(to, event.getChannel().getName() + " - <" + user + "> " + event.getMessage());
		}

		if (event.getUser().getNick().equals("[MC]-DogeFest") && event.getMessage().contains("<"))
		{
			onMessage(new MessageEvent<PircBotX>(event.getBot(), event.getChannel(), event.getUser(), event.getMessage().substring(event.getMessage().indexOf(">") + 2)));
			return;
		}

		if (event.getMessage().equalsIgnoreCase("test"))
			bot.sendIRC().message(event.getChannel().getName(), "icles");

		if (event.getMessage().charAt(0) == '!')
		{
			if (event.getMessage().charAt(0) == '!')
				handler.processMessage(event);
			else if (event.getChannel().getName().equals("#doge-coin"))
			{
				if (event.getUser().getNick().equals("DogeWallet") && event.getMessage().contains("sent " + getConfigHandler().getString("botName")))
				{
					String[] parsedMessage = event.getMessage().split(" ");
					//0 [Wow!]
					//1 Peter
					//2 sent
					//3 StevieBOT
					//4 33
					//Doge
					getLotteryHandler().addParticipant(parsedMessage[1], Long.parseLong(parsedMessage[4]));
					event.respond(Colors.BOLD+"Thankyou for entering the lottery! Tip "+getBot().getNick()+" "+getConfigHandler().getFloat("lott:minimumEntry")+" or more to enter!");
				}
					
			}
				
		} else // Message is not command, so we'll do a check for twat mode, and check spellings :>
			if (twatMode)
			{
				if (!(SpellCheckHandler.getSuggestions(event.getMessage(), 1)).equals(event.getMessage().toLowerCase()))
					try
				{
						bot.sendIRC().message(event.getChannel().getName(), "*".concat(SpellCheckHandler.getSuggestions(event.getMessage(), 1).concat("?")));
				} catch (final Exception e){}
				stopBeingATwat(event.getMessage(), event.getChannel().getName(), event.getUser(), event.getBot());
			}
		if (event.getChannel().getName().equals("##boywanders"))
		{
			// 0
			// <WANDERBOT>:<USER> !
			final String[] messageStr = event.getMessage().split(" ");
			if (messageStr[1].charAt(0) == '.')
			{
				@SuppressWarnings("unchecked")
				final MessageEvent evt = new MessageEvent(event.getBot(), event.getChannel(), event.getUser(), messageStr[1]);
				handler.processMessage(evt);
			}
		} else if (event.getChannel().getName().equals("##Ocelotworks"))
		{
			if (event.getMessage().equals("new topic plz"))
				messageCount = 100;
			if (event.getMessage().equals("reload those sweet sex phrases bro"))
			{
				sexQuotes.clear();
				loadSexQuotes();
				event.respond("Doneski");
			}
			messageCount++;
			if (messageCount > 100)
			{
				event.getBot().sendRaw().rawLine("TOPIC ##Ocelotworks " + sexQuotes.get(rand.nextInt(sexQuotes.size())));
				messageCount = 0;
			}
		}

		doReddit(event.getMessage(), event.getChannel().getName(), event.getUser());
		doYoutube(event.getMessage(), event.getChannel().getName());
		doLottery(event.getMessage(), event.getChannel().getName(), event.getUser());
		recordMessage(event);
	}

	@Override
	public void onPrivateMessage(final PrivateMessageEvent event) throws Exception
	{
		// if(event.getMessage().startsWith("!"))
		// {
		// String user = event.getUser().getNick();
		// handler.processMessage(event);
		// }

		if (event.getUser().equals("*status"))
			if (ZNCStatus.isCritical())
			{
				if (event.getMessage().contains("Connected"))
				{
					ZNCStatus.setCritical(false);
					ZNCStatus.setStatus(event.getMessage());
				}
			} else if (event.getMessage().contains("Disconnected") || event.getMessage().contains("Error"))
			{
				ZNCStatus.setCritical(true);
				ZNCStatus.setStatus(event.getMessage());
			}

		if (event.getUser().getNick().equals("DogeWallet"))
		{
			try{
				String[] parsedMessage = event.getMessage().split(" ");
				//0 Balance:
				//1 0.0
				//2 Pending:
				//3 0.0
				//4 Idle
				//5 remaining:
				//6 10.0
				//7 Activity
				//8 Status:
				//9 POOR
				float balance = Float.parseFloat(parsedMessage[1]);
				getConfigHandler().setFloat("dogeWalletBalance", balance);
			}catch(Exception e)
			{
				log("WARN", "DOGE", "Error getting dogewallet balance: "+e.toString());
			}
		}
	}

	@Override
	public void onQuit(final QuitEvent event)
	{
		if (event.getUser().equals(event.getBot().getUserBot()))
		{
			log("INFO", "JOIN", "Quiting.");
			doChannelSave();
		}
	}

	/**
	 * Record the message to the database
	 *
	 * @author Neil
	 * **/
	private void recordMessage(final MessageEvent event) throws SQLException
	{
		final String message = event.getMessage();
		final Channel channel = event.getChannel();
		final User sender = event.getUser();
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		final int hours = calendar.get(Calendar.HOUR_OF_DAY);
		final int minutes = calendar.get(Calendar.MINUTE);
		final int date = calendar.get(Calendar.DATE);
		final int month = calendar.get(Calendar.MONTH);
		final String[] months = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
		final String stringMonth = months[month];
		final String dateTime = date + " " + stringMonth + ", " + hours + ":" + minutes;
		config.createChannelTable(channel.getName());
		config.setLog(dateTime, sender.getNick(), message, channel.getName());
	}

	public void stopBeingATwat(final String message, final String channel, final User sender, final PircBotX bot) throws SQLException
	{
		if (message.toLowerCase(Locale.ENGLISH).equals("what"))
		{
			final ConfigHandler config = UnacceptableBot.getConfigHandler();
			final ResultSet rs = config.logQuery(channel);
			rs.next();
			final String id = rs.getString(1);
			final ResultSet logRS = UnacceptableBot.getConfigHandler().getLog(channel, Integer.parseInt(id) - 2);
			logRS.next();
			if ((logRS.getString(3).charAt(0) != '.') && (logRS.getString(3).charAt(0) != '!'))
				bot.sendIRC().message(channel, logRS.getString(3).toUpperCase(Locale.ENGLISH));
			else
				bot.sendIRC().message(channel, sender.getNick() + " is a bad boy :(");
		}
	}
}
