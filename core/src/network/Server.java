package network;

import java.io.*;
import java.net.*;
import java.util.Vector;

public class Server extends Thread
{
	private int dim = 15;
	private int port = 12345;
	public ServerSocket server;
	private Socket s;
	private BufferedReader in;
	private PrintWriter out;
	private Vector<int[][]> maps;
	private static int id = 0;
	
	public Server()
	{
		maps = new Vector<int[][]>();
		
		try 
		{
			server = new ServerSocket(port);	
		}catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		while(true)
		{
			System.out.println("in attesa...");
            try
            {
            	s = server.accept();
            	id++;
            	in = new BufferedReader(new InputStreamReader(s.getInputStream()));
    			out = new PrintWriter(s.getOutputStream());
            }
            catch(IOException e)
            {
                System.out.println("Could not get a client.");
            }
            System.out.println("connected"); 
            
            System.out.println("ID: "+id);
//			receive();
//			send(create());
//           
            // Sleep
            try
            {
                Thread.sleep(100);
            }
            catch(InterruptedException e)
            {
                System.out.println("Room has been interrupted.");
            }
/*
			try
			{
				s = server.accept();
				System.out.println("Connesso a: " + s.getInetAddress().getHostAddress());
				
//				
				s.close();
				
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		*/	
		}
	}
	
	private void send(int[][] map)
	{
		for(int i=0; i < map.length; i++)
		{
			for(int j=0; j < map[i].length; j++)
			{
				out.append(Integer.toString(map[i][j]).charAt(0));
			}
			out.println();
		}
		
		out.flush();
	}
	
	private void receive()
	{
		int[][] map = new int[dim][dim];
		
		for(int i=0; i<map.length; i++)
		{
			String str = "";
			try
			{
				str = in.readLine();
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
			
			for(int j=0; j<map[i].length; j++)
			{
					map[i][j] = str.charAt(j) - 48; 	// meno 48 perchè read() restituisce il numero in codice ASCII e 48 rappresenta lo 0
			}
		}
		maps.add(map);
	}
	
	
	private int[][] create()
	{
		int[][] map = new int[dim][dim];
		for(int i=0; i<map.length; i++)
			for(int j=0; j<map[i].length; j++)
			{ 
				int a = maps.get(0)[i][j];
				boolean same = true;
				for(int k=1; k<maps.size(); k++)
				{
					if(maps.get(k)[i][j] != a)
					{
						map[i][i] = 0;
						same = false;
						break;
					}
				}
				
				if(same)
					map[i][j] = a;
			}
		
		maps.clear();
		return map;
	}

	public static int getID()
	{
		return id;
	}
	
	
}
