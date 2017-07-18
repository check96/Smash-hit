package network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
	private BufferedReader in;
	private PrintWriter out;
	
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
					in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
					Client client = new Client(socket);
					
					out.write("login");
					clients.add(client);
					
				}
				catch(Exception e)
				{
				}
				
				if(clients.size() == numPlayers)
					MultiplayerLobby.ready = true;
			}
			
		}
    }
}
