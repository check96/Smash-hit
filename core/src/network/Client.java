package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import GameGui.GameManager;
import GameGui.Screen.GameScreen;
import GameGui.Screen.LoadingScreen;
import videogame.GameConfig;

public class Client extends Thread
{
	private GameManager game;
	private String ip;
	private String username;
	private int port = 12345; 
	private Socket s;
	private BufferedReader in;
	private PrintWriter out;
	private int id;
	
	public Client(GameManager _game, String ip, String username)
	{
		this.game = _game;
		this.ip = ip;
		this.username = username;
		
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
