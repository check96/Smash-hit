package videogame;

import java.util.ListIterator;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

import entity.Destroyable;
import entity.Objects;
import entity.Player;
import entity.Weapon;
import entity.Weapons;

public class World 
{
	private int id;
	private Destroyable[][] map;
	
	public World(int _id)
	{ 
		this.id = _id;
		Player player = new Player(new Vector3(0,-4.8f,28),new Vector3(5,5,5));
		Weapon weapon = new Weapon(new Vector3(0.5f,-4.5f,28), Weapons.MACE);
		player.setWeapon(weapon);
		
		GameConfig.players.add(id, player);		
	}

	public void update(float delta)
	{		
		synchronized (GameConfig.tools)
		{
			map = GameConfig.tools.get(GameConfig.actualLevel-1);
		}
		
		GameConfig.players.get(id).move();
		checkCollsion(delta);
		
		GameConfig.ON =false;
		GameConfig.LEFT = false;
		GameConfig.RIGHT = false;
		GameConfig.BACK = false;
		GameConfig.HIT = false;
		
		synchronized (GameConfig.players)
		{
			if(GameConfig.players.get(id).getX() > GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT * GameConfig.actualLevel)
				GameConfig.actualLevel++;
		}		
		checkGameOver();
		
		synchronized (GameConfig.tools)
		{
			GameConfig.tools.set(GameConfig.actualLevel-1,map);
		}
	}
	
	private void checkCollsion(float delta)
	{
		int i = 0, j = 0;
		synchronized (GameConfig.players)
		{
			i = (int) (GameConfig.players.get(id).getX() / GameConfig.CELL_HEIGHT) % GameConfig.ROOM_ROW;
			j = (int) (GameConfig.players.get(id).getZ() / GameConfig.CELL_WIDTH) % GameConfig.ROOM_COLUMN;
		}
		
		if(i != GameConfig.ROOM_ROW-1)
		{
			if(map[i+1][j] != null)
			{
				if(GameConfig.players.get(id).collide(map[i+1][j]));
				{
					if(GameConfig.ON)
					{
						GameConfig.BACK = true;
						GameConfig.ON = false;
						GameConfig.players.get(id).move();
					}
					else if(GameConfig.BACK)
					{
						GameConfig.BACK = false;
						GameConfig.ON = true;
						GameConfig.players.get(id).move();
					}
					else if(GameConfig.RIGHT)
					{
						GameConfig.RIGHT = false;
						GameConfig.LEFT = true;
						GameConfig.players.get(id).move();
					}
					else if(GameConfig.LEFT)
					{
						GameConfig.RIGHT = true;
						GameConfig.LEFT = false;
						GameConfig.players.get(id).move();
					}
					
					if(GameConfig.HIT)
					{
						GameConfig.HIT = false;
						hit(i+1,j,delta);
					}
				}
			}
		}
		if(i != 0)
		{
			if(map[i-1][j] != null)
			{
				if(GameConfig.players.get(id).collide(map[i-1][j]))
				{
					if(GameConfig.ON)
					{
						GameConfig.BACK = true;
						GameConfig.ON = false;
						GameConfig.players.get(id).move();
					}
					else if(GameConfig.BACK)
					{
						GameConfig.BACK = false;
						GameConfig.ON = true;
						GameConfig.players.get(id).move();
					}
					else if(GameConfig.RIGHT)
					{
						GameConfig.RIGHT = false;
						GameConfig.LEFT = true;
						GameConfig.players.get(id).move();
					}
					else if(GameConfig.LEFT)
					{
						GameConfig.RIGHT = true;
						GameConfig.LEFT = false;
						GameConfig.players.get(id).move();
					}
					if(GameConfig.HIT)
					{
						GameConfig.HIT = false;
						hit(i-1,j,delta);
					}
				}
			}
		}
		if(j!=0)
		{
			if(map[i][j-1] != null)
			{
				if(GameConfig.players.get(id).collide(map[i][j-1]))
				{
					if(GameConfig.ON)
					{
						GameConfig.BACK = true;
						GameConfig.ON = false;
						GameConfig.players.get(id).move();
					}
					else if(GameConfig.BACK)
					{
						GameConfig.BACK = false;
						GameConfig.ON = true;
						GameConfig.players.get(id).move();
					}
					else if(GameConfig.RIGHT)
					{
						GameConfig.RIGHT = false;
						GameConfig.LEFT = true;
						GameConfig.players.get(id).move();
					}
					else if(GameConfig.LEFT)
					{
						GameConfig.RIGHT = true;
						GameConfig.LEFT = false;
						GameConfig.players.get(id).move();
					}
					if(GameConfig.HIT)
					{
						GameConfig.HIT = false;
						hit(i,j-1,delta);
					}
				}
			}
		}
		if(j!=GameConfig.ROOM_COLUMN-1)
		{
			if(map[i][j+1] != null)
			{
				if(GameConfig.players.get(id).collide(map[i][j+1]))
				{
					if(GameConfig.ON)
					{
						GameConfig.BACK = true;
						GameConfig.ON = false;
						GameConfig.players.get(id).move();
					}
					else if(GameConfig.BACK)
					{
						GameConfig.BACK = false;
						GameConfig.ON = true;
						GameConfig.players.get(id).move();
					}
					else if(GameConfig.RIGHT)
					{
						GameConfig.RIGHT = false;
						GameConfig.LEFT = true;
						GameConfig.players.get(id).move();
					}
					else if(GameConfig.LEFT)
					{
						GameConfig.RIGHT = true;
						GameConfig.LEFT = false;
						GameConfig.players.get(id).move();
					}
					if(GameConfig.HIT)
					{
						GameConfig.HIT = false;
						hit(i,j+1,delta);
					}
				}
			}
		}
	}
		
	private void hit(int i, int j,float delta)
	{
		// decrease tool's life
		map[i][j].decreaseHealth(GameConfig.players.get(id).getWeapon().getDamage()*delta);
		
		System.out.println(map[i][j].getHealth());
		if(map[i][j].getHealth() == 0)
		{
			if(map[i][j].type == Objects.CLOCK)
				Countdown.increment(5);
			
			// add score and coins
			GameConfig.SCORE += map[i][j].type.score;
			GameConfig.COINS += map[i][j].getMoneyReward();
			
			
			//remove tools and toolsInstance
			synchronized (GameConfig.toolsInstance.get(GameConfig.actualLevel-1))
			{
				ListIterator<ModelInstance> iterator = GameConfig.toolsInstance.get(GameConfig.actualLevel-1).listIterator();
				Vector3 objPosition = new Vector3();
				objPosition = map[i][j].getPosition();
				map[i][j] = null;

				while(	iterator.hasNext())
				{
					ModelInstance instance = iterator.next();
					Vector3 position = new Vector3();
					if(instance.transform.getTranslation(position).equals(objPosition))
					{
						GameConfig.destroyed.add(instance);
						iterator.remove();
						break;
					}
				}
			}
		}
	}
	
	private void checkGameOver()
	{
		if(Countdown.getTime() == 0)
			GameConfig.GAME_OVER = true;
	}
}
