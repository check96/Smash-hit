package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.badlogic.gdx.math.Vector3;

import GameGui.Screen.MultiplayerLobby;
import entity.Player;
import entity.Weapon;
import packet.Packet;
import packet.PacketType;
import packet.loginPacket;
import videogame.GameConfig;

public class Server extends Thread
{
	public DatagramSocket server;
	private static int id = 0;
	private int numPlayers = 1;
	
	public Server(int port, int numPlayers)
	{
		this.numPlayers = numPlayers;
		
		try 
		{
			server = new DatagramSocket(port);	
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
			if(id == numPlayers-1)
				MultiplayerLobby.ready = true;
			
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				server.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			packetManagement(packet.getData(), packet.getAddress(), packet.getPort());
		}
    }
	private void packetManagement(byte[] data, InetAddress address, int port)
	{
		String message = new String(data);
		PacketType type = Packet.findType(message.substring(0, 1));
		Packet packet = null;
		switch(type)
		{
			case LOGIN:		packet = new loginPacket(data);
							handleLogin((loginPacket) packet);
							break;
			case MOVE: 
							break;
			case HIT: 
							break;
			default:	break;
						
		}
		
	}

	private void handleLogin(loginPacket packet)
	{
		for (Player p : GameConfig.players)
		{
			if(p.getUsername().equals(packet.getUsername()))
				return;
		}
		
		Player player = new Player(new Vector3(0,-4.8f, 15+(20*id)), 4, packet.getUsername());
		Weapon weapon = new Weapon(new Vector3(0.5f,-4.5f,40));
		player.setWeapon(weapon);
		
		GameConfig.players.add(player);
		id++;
		
	}

	public static int getID()
	{
		return id;
	}
	
	
}
