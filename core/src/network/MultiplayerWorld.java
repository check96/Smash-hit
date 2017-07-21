package network;

import java.util.ArrayList;
import com.badlogic.gdx.math.Vector3;

import GameGui.Deleter;
import entity.Destroyable;
import entity.Objects;
import entity.Player;
import entity.Wall;
import entity.Walls;
import entity.Weapon;
import network.packet.DoorPacket;
import network.packet.HitPacket;
import network.packet.MovePacket;
import videogame.Countdown;
import videogame.GameConfig;

public class MultiplayerWorld 
{
	private Destroyable[][] map;
	public static ArrayList<String> usernames = new ArrayList<String>();
	private ClientThread client;
	
	public MultiplayerWorld(ClientThread client)
	{
		GameConfig.LOCAL_COINS = 0;
		this.client = client;
	}

	public void update(float delta)
	{
		synchronized (GameConfig.tools)
		{
			map = GameConfig.tools.get(GameConfig.actualLevel-1);
		}
		
		int id = GameConfig.ID;
		
		boolean movement = GameConfig.players.get(id).move(delta, GameConfig.DIRECTION);
		boolean collide = checkCollsion(delta);
		boolean playerCollide = checkPlayerCollision(delta);
		
		if(movement || collide || playerCollide)
			client.out.println(new MovePacket(GameConfig.players.get(GameConfig.ID).getPosition(), GameConfig.players.get(GameConfig.ID).angle));
		
		if(GameConfig.HIT)
			hit(delta);
		
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

	private boolean checkWallCollision(float delta)
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
						return true;
					}
				}
			}
		}
		
		return false;
	}

	private boolean checkCollsion(float delta)
	{
    	int	i = (int) ((GameConfig.players.get(GameConfig.ID).getX() + 4.5f)/ GameConfig.CELL_HEIGHT) % GameConfig.ROOM_ROW;
    	int	j = (int) ((GameConfig.players.get(GameConfig.ID).getZ() + 3.5f) / GameConfig.CELL_WIDTH) % GameConfig.ROOM_COLUMN;
    	
		if((i == 0 && j !=5)  || (i == GameConfig.ROOM_ROW-1 && j != 5) || j == 0 || j == GameConfig.ROOM_COLUMN-1 )
			if(checkWallCollision(delta))
				client.out.println(new MovePacket(GameConfig.players.get(GameConfig.ID).getPosition(), GameConfig.players.get(GameConfig.ID).angle));

		if(map[i][j] instanceof Destroyable)
		{
			if(GameConfig.players.get(GameConfig.ID).collide(map[i][j]));
			{
				reaction(delta);
				return true;
			}
		}
		
		return false;
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

		boolean clock = false;
		if(map[i][j].type == Objects.CLOCK)
			clock = true;

		Deleter.remove(clock, map[i][j].getPosition()); 
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
    		if(GameConfig.players.get(GameConfig.ID).collide(map[i][j]))
    		{
    			if(map[i][j].type == Objects.CLOCK)
    				map[i][j].decreaseHealth(map[i][j].getHealth());
    			else
    				map[i][j].decreaseHealth(GameConfig.players.get(GameConfig.ID).getWeapon().getDamage()*delta);
    		
    			client.out.println(new HitPacket(i, j, map[i][j].getHealth()).toString());
    		}
    		
			if(map[i][j].getHealth() == 0)
			{
				if(map[i][j].type == Objects.DOOR)
					client.out.println(new DoorPacket());
				
				//remove tools and toolsInstance
				delete(i,j);
			}
    	}
	}

	private boolean checkPlayerCollision(float delta) 
	{
		for(int i = 0; i<GameConfig.players.size(); i++)
		{
			if(i != GameConfig.ID)
			{
				if(GameConfig.players.get(GameConfig.ID).collide(GameConfig.players.get(i)))
				{
					reaction(delta);
					return true;
				}
			}
		}
		
		return false;
	}
	
	public synchronized void packetManager(String[] packet, float delta)
	{
		if(packet[0].equals("move"))
		{
			int id = Integer.parseInt(packet[1]);

			int angle = Integer.parseInt(packet[2]);
			float x = Float.parseFloat(packet[3]);
			float y = Float.parseFloat(packet[4]);
			float z = Float.parseFloat(packet[5]);
			
			GameConfig.players.get(id).setPosition(x,y,z);
			GameConfig.players.get(id).angle = angle;
		}
		else if(packet[0].equals("door"))
		{
			delete(GameConfig.ROOM_ROW-1,GameConfig.ROOM_COLUMN/2);
		}
		else if(packet[0].equals("hit"))
		{
			int room = Integer.parseInt(packet[2]);
			int i = Integer.parseInt(packet[3]);
			int j = Integer.parseInt(packet[4]);
			int health = Integer.parseInt(packet[5]);
			
			if(GameConfig.tools.get(room)[i][j] instanceof Destroyable)
			{
				GameConfig.tools.get(room)[i][j].setHealth(health);
			
				if(GameConfig.tools.get(room)[i][j].getHealth() == 0)
				{
					//remove tools and toolsInstance
					delete(i,j);
				}
			}
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
			Player player = new Player(new Vector3(5,-4.8f, 10 * (i+1)), 4, usernames.get(i));
			player.setWeapon(new Weapon(player.getPosition()));
			
			GameConfig.players.add(player);
			
			if(usernames.get(i).equals(GameConfig.username))
				GameConfig.ID = i;
		}
	}
	
}