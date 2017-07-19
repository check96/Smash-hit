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
import java.net.UnknownHostException;

import GameGui.GameManager;
import network.packet.Packet;
import network.packet.PacketType;
import network.packet.HitPacket;
import network.packet.LoadPacket;
import network.packet.MovePacket;

public class ServerThread extends Thread
{
	private Socket socket;
	public BufferedReader in;
	public PrintWriter out;
	private boolean connected = true;
	
	public ServerThread(Socket _socket)
	{
		this.socket = _socket;
		connected = true;
	
		try
		{
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
		} catch (IOException e)
		{
			try { socket.close(); }
			catch(Exception e1) { System.out.println(e1.getMessage());}
		
		}
	}
	
	public void run()
	{
		while(true)
		{
			try
			{
				String line = in.readLine();
				System.out.println("server " + line);
//				packetManagement(line);
				
			} catch (Exception e)
			{
			}
			
		}
	}

	public boolean isConnected()
	{
		return connected;
	}
	
	private void packetManagement(String line)
	{
		
	}
	
	public void disconnect()
	{
		try {
			connected = false;
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Socket getSocket()
	{
		return socket;
	}
}
