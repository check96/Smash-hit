package network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread
{
	public Socket s;
	public BufferedReader in;
	public PrintWriter out;
	
	public ClientThread(Socket _s) 
	{
		s =_s;
		try {
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() 
	{
		while(true)
		{
			try {
 				String line = in.readLine();
 				System.out.println("client "+line);
 				out.write("suca\n");
 				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
