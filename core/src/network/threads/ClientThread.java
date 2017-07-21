package network.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import com.badlogic.gdx.Gdx;

import network.MultiplayerWorld;
import network.Screen.MultiplayerScreen;
import network.packet.MultiplayerLobby;
import videogame.GameConfig;

public class ClientThread extends Thread
{
	public Socket socket;
	public BufferedReader in;
	public PrintWriter out;
	private MultiplayerScreen multiplayerScreen;
	
	public ClientThread(Socket _socket, MultiplayerScreen _multiplayerScreen) 
	{
		this.socket = _socket;
		this.multiplayerScreen = _multiplayerScreen;
		
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
 				String receive = in.readLine();
 				packetManagement(receive);
 			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void packetManagement(String receive)
	{
		String[] packet = receive.split(",");
		
		if(packet[0].equals("ready"))
			MultiplayerLobby.ready = true;
		else if(packet[0].equals("load"))
		{
			if(!GameConfig.isServer)
				multiplayerScreen.game.multiplayerMapGenerator.loadRoom(packet[2]);
		}
		else if(packet[0].equals("login"))
		{
			MultiplayerWorld.usernames.clear();
			String[] usernames = receive.split(",");
			for(int i = 1; i < usernames.length; i++)
				MultiplayerWorld.usernames.add(usernames[i]);

			MultiplayerWorld.addPlayers();
		}
		else if(packet[0].equals("logout"))
		{
		}
		else
				multiplayerScreen.getWorld().packetManager(packet, Gdx.graphics.getDeltaTime());
	}

}
