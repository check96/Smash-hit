package network;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

import GameGui.Deleter;
import entity.Destroyable;
import entity.Objects;
import entity.Player;
import entity.Wall;
import entity.Walls;
import videogame.Countdown;
import videogame.GameConfig;
import videogame.World;

public class MultiplayerWorld 
{
	private Destroyable[][] map;
	public static ArrayList<String> usernames = new ArrayList<String>();
	
	public MultiplayerWorld()
	{
		GameConfig.LOCAL_COINS = 0;
	}

	public void update(float delta)
	{
		synchronized (GameConfig.tools)
		{
			map = GameConfig.tools.get(GameConfig.actualLevel-1);
		}
		
		int id = GameConfig.ID;
		
		GameConfig.players.get(id).move(delta, GameConfig.DIRECTION);
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
			if(GameConfig.players.get(id).getX() > GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT * GameConfig.actualLevel)
			{
				GameConfig.tools.set(GameConfig.actualLevel-1, null); 
				GameConfig.actualLevel++;
			}
		}		
	}

	private void checkGameOver()
	{
		if(Countdown.getTime() == 0)
			GameConfig.GAME_OVER = true;
		
	}

	private void checkWallCollision(float delta)
	{
		synchronized (GameConfig.walls)
		{
			for (Wall wall : GameConfig.walls)
			{
				if(wall.type != Walls.CEILING && wall.type != Walls.FLOOR)
				{
					if(GameConfig.players.get(GameConfig.ID).collide(wall))
					{
						reaction(delta);
					}
				}
			}
		}
	}

	private void checkCollsion(float delta)
	{
    	int	i = (int) ((GameConfig.players.get(GameConfig.ID).getX() + 4.5f)/ GameConfig.CELL_HEIGHT) % GameConfig.ROOM_ROW;
    	int	j = (int) ((GameConfig.players.get(GameConfig.ID).getZ() + 3.5f) / GameConfig.CELL_WIDTH) % GameConfig.ROOM_COLUMN;
    	
		if((i == 0 && j !=5)  || (i == GameConfig.ROOM_ROW-1 && j != 5) || j == 0 || j == GameConfig.ROOM_COLUMN-1 )
			checkWallCollision(delta);

		if(map[i][j] instanceof Destroyable)
		{
			if(GameConfig.players.get(GameConfig.ID).collide(map[i][j]));
			{
				if(map[i][j].type == Objects.CLOCK)
    				hit(delta);
				else
					reaction(delta);
			}
		}
	}
		
	private void reaction(float delta)
	{
		if(GameConfig.ON)
			GameConfig.players.get(GameConfig.ID).moveBack(delta, GameConfig.DIRECTION);
		else if(GameConfig.BACK)
			GameConfig.players.get(GameConfig.ID).moveOn(delta, GameConfig.DIRECTION);
		else if(GameConfig.LEFT)
			GameConfig.players.get(GameConfig.ID).moveRight(delta, GameConfig.DIRECTION);
		else if(GameConfig.RIGHT)
			GameConfig.players.get(GameConfig.ID).moveLeft(delta, GameConfig.DIRECTION);
	}

	private void delete(int i, int j)
	{
		// add score and coins
		GameConfig.SCORE += map[i][j].type.score;
		GameConfig.LOCAL_COINS += map[i][j].getMoneyReward() * GameConfig.coinsMultiplier;
		
		boolean clock = false;
		
		if(map[i][j].type == Objects.CLOCK)
			clock = true;

		switch (map[i][j].type)
		{
			case DESK:	 GameConfig.destroyedDesks++;
						 break;
						
			case CHAIR:  GameConfig.destroyedChairs++;
						 break;
			
			case LOCKER: GameConfig.destroyedLockers++;
						 break;
			
			case DOOR:   GameConfig.destroyedDoors++;
						 break;
						 
			case PLANT:	 GameConfig.destroyedPlants++;
						 break;
			default: 	 break;
		}
		
		Deleter.remove(clock, map[i][j].getPosition(), map[i][j].getMoneyReward());
		map[i][j] = null;
	}
	
	private void hit(float delta)
	{
		GameConfig.players.get(GameConfig.ID).moveOn(delta, GameConfig.DIRECTION);
		GameConfig.players.get(GameConfig.ID).moveOn(delta, GameConfig.DIRECTION);
		
		int	i = (int) ((GameConfig.players.get(GameConfig.ID).getX() + 4.5f)/ GameConfig.CELL_HEIGHT) % GameConfig.ROOM_ROW;
    	int	j = (int) ((GameConfig.players.get(GameConfig.ID).getZ() + 3.5f) / GameConfig.CELL_WIDTH) % GameConfig.ROOM_COLUMN;
    	
		GameConfig.players.get(GameConfig.ID).moveBack(delta, GameConfig.DIRECTION);
		GameConfig.players.get(GameConfig.ID).moveBack(delta, GameConfig.DIRECTION);
		
		// decrease tool's life
    	if(map[i][j] instanceof Destroyable)
    	{
    		if(GameConfig.player.collide(map[i][j]))
    		{
    			if(map[i][j].type == Objects.CLOCK)
    				map[i][j].decreaseHealth(map[i][j].getHealth());
    			else
    				map[i][j].decreaseHealth(GameConfig.player.getWeapon().getDamage()*delta);
    		}


			if(map[i][j].getHealth() == 0)
			{
				if(map[i][j].type == Objects.CLOCK)
					Countdown.increment(5 * (GameConfig.clockLevel+1));
													
				//remove tools and toolsInstance
				delete(i,j);
			}
    	}
	}


	private void checkPlayerCollision(float delta) 
	{
		
	}
	
	public synchronized void packetManager(String[] packet, float delta)
	{
		if(packet[0].equals("move"))
		{
			int id = Integer.parseInt(packet[1]);

			Vector3 direction = new Vector3(Float.parseFloat(packet[3]),Float.parseFloat(packet[4]),Float.parseFloat(packet[5]));
			if(packet[2].equals("on"))
				GameConfig.players.get(id).moveOn(delta, direction);
			else if(packet[2].equals("back"))
				GameConfig.players.get(id).moveBack(delta, direction);
			else if(packet[2].equals("left"))
				GameConfig.players.get(id).moveLeft(delta, direction);
			else if(packet[2].equals("right"))
				GameConfig.players.get(id).moveRight(delta, direction);
		}
		else if(packet[0].equals("hit"))
		{
			
		}
		else if(packet[0].equals("time"))
		{
			Countdown.increment(5);
		}
	}

	public static void addPlayers()
	{
		GameConfig.players.clear();
		for(int i = 0; i < usernames.size(); i++)
		{
			GameConfig.players.add(new Player(new Vector3(5,-4.8f, 10 * (i+1)), 4, usernames.get(i)));
			
			if(usernames.get(i).equals(GameConfig.username))
				GameConfig.ID = i;
		}
	}
	
}