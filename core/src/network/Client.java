package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import GameGui.GameManager;
import GameGui.Screen.MultiplayerLobby;
import packet.Packet;
import packet.PacketType;
import packet.hitPacket;
import packet.loginPacket;
import packet.movePacket;

public class Client extends Thread
{
	private GameManager game;
	private String ip;
	private String username;
	private int port; 
	private DatagramSocket socket;
	private int id;
	private int numPlayers;
	
	public Client(GameManager _game, String ip, String username, int port, int numPlayers)
	{
		this.game = _game;
		this.ip = ip;
		this.username = username;
		this.port = port;
		this.numPlayers = numPlayers;
		
		id = Server.getID();
		
		System.out.println("ID: " + id);
		try 
		{
			socket = new DatagramSocket(port);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		this.start();
		game.setScreen(new MultiplayerLobby(game));
	}
	
	public void run()
	{
		while(true) {
			byte[] data = new byte[1024];
			
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
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
							break;
							
			case MOVE: 		packet = new movePacket(data);
							break;
							
			case HIT: 		packet = new hitPacket(data); 
						 	break;
						 	
			default:	break;
						
		}
		
	}

}
