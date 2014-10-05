package com.unacceptableuse.unacceptablebot.handler;

import java.net.InetSocketAddress;
import java.util.ArrayList;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class WebSocketHandler extends WebSocketServer
{

	private ArrayList<WebSocket> connectedClients = new ArrayList<WebSocket>(); 
	
	
	public WebSocketHandler() 
	{
		super(new InetSocketAddress(8791));	
	}

	public void init()
	{
		this.start();
	}

	@Override
	public void onClose(WebSocket sock, int arg1, String arg2, boolean arg3)
	{
		connectedClients.remove(sock);
		
	}

	@Override
	public void onError(WebSocket arg0, Exception arg1)
	{
		
		
	}

	@Override
	public void onMessage(WebSocket sock, String arg1)
	{
		
		
	}
	
	public int getConnectedClients()
	{
		return connectedClients.size();
	}
	
	public void logMessage(String log)
	{
		broadcast("logm:"+log);
	}
	
	
	private void broadcast(String message)
	{
		if(getConnectedClients() == 0)return;
		for(WebSocket sock : connectedClients)
		{
			sock.send(message);
		}
	}

	@Override
	public void onOpen(WebSocket sock, ClientHandshake arg1)
	{
		connectedClients.add(sock);
		
	}

}
