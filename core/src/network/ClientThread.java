package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import videogame.GameConfig;

public class ClientThread extends Thread
{
	public Socket socket;
	public BufferedReader in;
	public PrintWriter out;
	public String receive = "";
	
	public ClientThread(Socket _socket) 
	{
		socket = _socket;
		try
		{
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		out.println("login,"+GameConfig.username);
	}
	@Override
	public void run() 
	{
		while(true)
		{
			try
			{
 				receive = in.readLine();
 				if(receive.substring(0, 5).equals("login"))
 				{
 					MultiplayerWorld.usernames.clear();
 					String[] usernames = receive.split(",");
 					for(int i = 1; i < usernames.length; i++)
 						MultiplayerWorld.usernames.add(usernames[i]);

 					MultiplayerWorld.addPlayers();
 				}
 				if(receive.equals("ready"))
 				{
 					MultiplayerLobby.ready = true;
 					receive = "";
 				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
