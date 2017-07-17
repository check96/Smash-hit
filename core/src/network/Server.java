package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import GameGui.GameManager;

public class Server extends Thread
{
	public ServerSocket server;
	public static MultiplayerScreen screen;
	private int numPlayers = 1;
	public static ArrayList<Client> clients;
	
	public Server(GameManager game, int port, int numPlayers)
	{
		this.numPlayers = numPlayers;
		clients = new ArrayList<Client>(); 
				
		try 
		{
			server = new ServerSocket(port);	
			System.out.println("Server created");
		}catch (IOException e)
		{
			e.printStackTrace();
		}
		
		this.start();
	}
	
	public void run()
	{
		while(true)
		{
			if(!MultiplayerLobby.ready)
			{
				try
				{
					Socket socket = server.accept();
					clients.add(new Client(socket));
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				if(clients.size() == numPlayers)
					MultiplayerLobby.ready = true;
			}
		}
    }
}
