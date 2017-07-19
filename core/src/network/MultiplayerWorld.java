package network;

import com.badlogic.gdx.math.Vector3;

import entity.Player;
import videogame.Countdown;
import videogame.GameConfig;
import videogame.World;

public class MultiplayerWorld extends World 
{
	private Player player;
	
	public MultiplayerWorld()
	{
		GameConfig.LOCAL_COINS = 0;
		
		for(Player pl : GameConfig.players)
			if(pl.getUsername().equals(GameConfig.username))
				player = pl;
	}

	@Override
	public void update(float delta)
	{
		synchronized (GameConfig.tools)
		{
			map = GameConfig.tools.get(GameConfig.actualLevel-1);
		}
		
		GameConfig.player.move(delta, GameConfig.DIRECTION);
		checkCollsion(delta);
		checkPlayerCollision(delta);
		
		GameConfig.ON = false;
		GameConfig.LEFT = false;
		GameConfig.RIGHT = false;
		GameConfig.BACK = false;
		
		GameConfig.HIT = false;
		checkGameOver();
		
		synchronized (GameConfig.tools)
		{
			GameConfig.tools.set(GameConfig.actualLevel-1,map);
		}
		
		synchronized (GameConfig.tools)
		{
			if(GameConfig.player.getX() > GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT * GameConfig.actualLevel)
			{
				GameConfig.tools.set(GameConfig.actualLevel-1, null); 
				GameConfig.actualLevel++;
			}
		}		
	}

	private void checkPlayerCollision(float delta) 
	{
		for(Player pl : GameConfig.players)
			if(!((pl.getUsername()).equals(player.getUsername())))
					player.collide(pl);
			
	}
	
	public synchronized void packetManager(String[] packet, float delta)
	{
		if(packet[0].equals("move"))
		{
			for (Player player : GameConfig.players)
			{
				if(player.getUsername().equals(packet[1]))
				{
					Vector3 direction = new Vector3(Integer.parseInt(packet[3]),Integer.parseInt(packet[4]),Integer.parseInt(packet[5]));
					if(packet[2].equals("on"))
						player.moveOn(delta, direction);
					else if(packet[2].equals("back"))
						player.moveBack(delta, direction);
					else if(packet[2].equals("left"))
						player.moveLeft(delta, direction);
					else if(packet[2].equals("right"))
						player.moveRight(delta, direction);
				}
			}
		}
		else if(packet[0].equals("hit"))
		{
			
		}
		else if(packet[0].equals("time"))
		{
			Countdown.increment(5);
		}
	}

	public void addPlayers(String[] usernames)
	{
		for(int i = 1; i < usernames.length; i++)
			GameConfig.players.add(new Player(new Vector3(0,-4.8f, 10*i), 4, usernames[i]));
	}
	
}