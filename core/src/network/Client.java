package network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;

import GameGui.GameManager;
import network.packet.Packet;
import network.packet.PacketType;
import network.packet.hitPacket;
import network.packet.loginPacket;
import network.packet.movePacket;

public class Client extends Thread
{
	private GameManager game;
	private InetAddress ipAddress;
	private String ip;
	private String username;
	private int port; 
	private Socket socket;
	private int id;
	private int numPlayers;
	private BufferedReader in;
	private PrintWriter out;
	
	public Client(Socket _socket)
	{
		this.socket = _socket;
		try
		{
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true); 
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		this.start();
	}
	
	public void run()
	{
		while(true)
		{
		}
	}
}
