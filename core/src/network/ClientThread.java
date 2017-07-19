package network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import videogame.Countdown;
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
		
		out.println(GameConfig.username);
	}
	@Override
	public void run() 
	{
		while(true)
		{
			try
			{
 				receive = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
