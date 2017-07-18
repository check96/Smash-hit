package network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import videogame.Countdown;

public class ClientThread extends Thread
{
	public Socket socket;
	public BufferedReader in;
	public PrintWriter out;
	
	public ClientThread(Socket _socket) 
	{
		socket = _socket;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() 
	{
		while(true)
		{
			String line;
			try {
 				line = in.readLine();
 				packetManager(line);
 			
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	private void packetManager(String line)
	{
		String[] packet = line.split(",");
		if(packet[0].equals("loadMap"))
		{
			
		}
		else if(packet[0].equals("move"))
		{
			
		}
		else if(packet[0].equals("destroy"))
		{
			
		}
		else if(packet[0].equals("time"))
		{
			Countdown.increment(5);
		}
	} 
	
	
}
