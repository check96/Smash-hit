package network.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import network.MultiplayerWorld;
import videogame.Countdown;
import videogame.GameConfig;

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
			String line;
			try {
				line = in.readLine();
				packetManagement(line);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isConnected()
	{
		return connected;
	}
	
	private synchronized void packetManagement(String line)
	{
		String[] packet = line.split(",");
		
		if(packet[0].equals("login"))
		{
			String send = "login,";
			for (String username : MultiplayerWorld.usernames)
			{
				send += username + ",";
			}
			send += packet[1];
			MultiplayerWorld.usernames.add(packet[1]);
			GameConfig.server.sendData(send);
		}
		else
			GameConfig.server.sendData(line);
	}
	
	public void disconnect()
	{
		try 
		{
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