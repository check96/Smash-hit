package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import network.Screen.MultiplayerScreen;
import network.packet.MultiplayerLobby;
import network.packet.Packet;
import network.threads.ServerThread;

public class Server extends Thread
{
	public static ServerSocket serverSocket;
	public static MultiplayerScreen screen;
	private int numPlayers = 1;
	public static ArrayList<String> usernames = new ArrayList<String>();
	public static ArrayList<ServerThread> clients = new ArrayList<ServerThread>();
	public BufferedReader in;
	public PrintWriter out;
	
	public Server(int port, int numPlayers)
	{
		this.numPlayers = numPlayers;
				
		try 
		{
			serverSocket = new ServerSocket(port);
			System.out.println("Server created");
			
		}catch (IOException e)
		{
			e.printStackTrace();
		}
		
		this.start();
	}
	
	public void launch()
	{
		for(int i = 0; i<numPlayers; i++)
		{
			try
			{
				Socket socket = serverSocket.accept();
				ServerThread c = new ServerThread(socket);
				clients.add(c);
				c.start();
			}
			catch(Exception e){}
		}
		
		sendData(MultiplayerLobby.loadPacket);
		sendData("ready,");
	}	
	
	public void sendData(Packet packet)
	{
		String[] packets = packet.toString().split(",");
		
		String code = packets[0];
		int id = Integer.parseInt(packets[1]);
		
		for(int i = 0; i < clients.size(); i++)
		{
			if(i != id || code.equals("load"))
				clients.get(i).out.println(packet.toString());
		}
	}
	
	public void sendData(String line)
	{
		for(int i = 0; i < clients.size(); i++)
			clients.get(i).out.println(line);
	}
}

