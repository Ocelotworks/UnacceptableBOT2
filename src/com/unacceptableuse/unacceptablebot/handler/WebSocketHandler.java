package com.unacceptableuse.unacceptablebot.handler;

import java.net.InetSocketAddress;
import java.util.ArrayList;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.pircbotx.Channel;
import org.pircbotx.User;

import com.google.common.collect.ImmutableSet;
import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class WebSocketHandler extends WebSocketServer
{

	private final ArrayList<WebSocket> connectedClients = new ArrayList<WebSocket>();

	public WebSocketHandler()
	{
		super(new InetSocketAddress(8791));
	}

	private void broadcast(final String message)
	{
		if (getConnectedClients() == 0)
			return;
		for (final WebSocket sock : connectedClients)
			sock.send(message);
	}

	public int getConnectedClients()
	{
		return connectedClients.size();
	}

	public void init()
	{
		start();
	}

	public void logMessage(final String log)
	{
		broadcast("logm:" + log);
	}

	@Override
	public void onClose(final WebSocket sock, final int arg1, final String arg2, final boolean arg3)
	{
		connectedClients.remove(sock);

	}

	@Override
	public void onError(final WebSocket arg0, final Exception arg1)
	{

	}

	@Override
	public void onMessage(final WebSocket sock, final String message)
	{

		if(message.equals("cinf"))
		{
			final User u = UnacceptableBot.getBot().getUserBot();
			final ImmutableSet<Channel> channels = u.getChannels();
			for (final Channel c : channels)
				sock.send("cinf:" + c.getName() + ":" + c.getMode() + ":" + c.getUsers().size() + (c.getChannelLimit() != -1 ? c.getChannelLimit() : "") + ":" + (c.getOps().contains(u) ? "Opped" : c.getVoices().contains(u) ? "Voiced" : "User"));
			sock.send("cinfend");
			sock.close(1000);
		}else if(message.startsWith("cadd"))
		{
			UnacceptableBot.getBot().sendIRC().joinChannel(message.split(":")[1]);
		}
	
			
//		case "recon":
//			try
//			{
//				UnacceptableBot.getBot().startBot();
//			} catch (final Exception e)
//			{
//				sock.send("recon:" + e.toString());
//				e.printStackTrace();
//			}
//
//			break;
		

	}

	@Override
	public void onOpen(final WebSocket sock, final ClientHandshake arg1)
	{
		connectedClients.add(sock);
	}

}
