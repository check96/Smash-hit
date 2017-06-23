package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import GameGui.GameManager;
import GameGui.GameScreen;
import GameGui.LoadingScreen;
import videogame.GameConfig;

public class Client extends Thread
{
	private GameManager game;
	private String ip = "127.0.0.1";
	private int port = 12345; 
	private Socket s;
	private BufferedReader in;
	private PrintWriter out;
	private int id;
	
	public Client(GameManager _game)
	{
		this.game = _game;
		
		id = Server.getID();
		
		System.out.println("ID: " + id);
		try 
		{
			s = new Socket(ip,port);
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintWriter(s.getOutputStream());
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		this.start();
		game.setScreen(new LoadingScreen(game));
	}
	
	public void run()
	{
		while(true)
		{
			try 
			{
				System.out.println("send");
//				send();
//				receive();
			} catch (Exception e)
			{
				System.out.println("ERRORE DI CONNESSIONE");
			}
		}
	}
}
