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
	private Destroyable[][] map;
	
	public World()
	{ 
		GameConfig.player = new Player(new Vector3(0,-4.8f,45),new Vector3(5,5,5));
		Weapon weapon = new Weapon(new Vector3(0.5f,-4.5f,40), Weapons.MACE);
		GameConfig.player.setWeapon(weapon);
	}

	public void update(float delta)
	{		
		synchronized (GameConfig.tools)
		{
			map = GameConfig.tools.get(GameConfig.actualLevel-1);
		}
		
		GameConfig.player.move();
//		checkCollsion(delta);
		
		GameConfig.ON =false;
		GameConfig.LEFT = false;
		GameConfig.RIGHT = false;
		GameConfig.BACK = false;
		GameConfig.HIT = false;
		
		synchronized (GameConfig.player)
		{
			if(GameConfig.player.getX() > GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT * GameConfig.actualLevel)
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
		synchronized (GameConfig.player)
		{
			if(GameConfig.player.getX() < 0)
    			i = 0;
    		else
    			i = (int) ((GameConfig.player.getX() / GameConfig.CELL_HEIGHT) +1 )% GameConfig.ROOM_ROW;
			
    		j = (int) (GameConfig.player.getZ() / GameConfig.CELL_WIDTH) % GameConfig.ROOM_COLUMN;
		}
		
		if(i != GameConfig.ROOM_ROW-1)
		{
			if(map[i+1][j] != null)
			{
				if(GameConfig.player.collide(map[i+1][j]));
				{
					System.out.print("collide with "+ map[i+1][j].type+"  in "+map[i+1][j].getPosition()+"  of "+ map[i+1][j].getSize());
					if(GameConfig.ON)
					{
						System.out.println("ON");
						GameConfig.BACK = true;
						GameConfig.ON = false;
						GameConfig.player.move();
					}
					else if(GameConfig.BACK)
					{
						System.out.println("BACK");
						GameConfig.BACK = false;
						GameConfig.ON = true;
						GameConfig.player.move();
					}
					else if(GameConfig.RIGHT)
					{
						System.out.println("RIGHT");
						GameConfig.RIGHT = false;
						GameConfig.LEFT = true;
						GameConfig.player.move();
					}
					else if(GameConfig.LEFT)
					{
						System.out.println("LEFT");
						GameConfig.RIGHT = true;
						GameConfig.LEFT = false;
						GameConfig.player.move();
					}
					
					System.out.println();
					System.out.println(GameConfig.player.getPosition() + "    "+GameConfig.player.getSize());
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
				if(GameConfig.player.collide(map[i-1][j]))
				{
					if(GameConfig.ON)
					{
						GameConfig.BACK = true;
						GameConfig.ON = false;
						GameConfig.player.move();
					}
					else if(GameConfig.BACK)
					{
						GameConfig.BACK = false;
						GameConfig.ON = true;
						GameConfig.player.move();
					}
					else if(GameConfig.RIGHT)
					{
						GameConfig.RIGHT = false;
						GameConfig.LEFT = true;
						GameConfig.player.move();
					}
					else if(GameConfig.LEFT)
					{
						GameConfig.RIGHT = true;
						GameConfig.LEFT = false;
						GameConfig.player.move();
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
				if(GameConfig.player.collide(map[i][j-1]))
				{
					if(GameConfig.ON)
					{
						GameConfig.BACK = true;
						GameConfig.ON = false;
						GameConfig.player.move();
					}
					else if(GameConfig.BACK)
					{
						GameConfig.BACK = false;
						GameConfig.ON = true;
						GameConfig.player.move();
					}
					else if(GameConfig.RIGHT)
					{
						GameConfig.RIGHT = false;
						GameConfig.LEFT = true;
						GameConfig.player.move();
					}
					else if(GameConfig.LEFT)
					{
						GameConfig.RIGHT = true;
						GameConfig.LEFT = false;
						GameConfig.player.move();
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
				if(GameConfig.player.collide(map[i][j+1]))
				{
					if(GameConfig.ON)
					{
						GameConfig.BACK = true;
						GameConfig.ON = false;
						GameConfig.player.move();
					}
					else if(GameConfig.BACK)
					{
						GameConfig.BACK = false;
						GameConfig.ON = true;
						GameConfig.player.move();
					}
					else if(GameConfig.RIGHT)
					{
						GameConfig.RIGHT = false;
						GameConfig.LEFT = true;
						GameConfig.player.move();
					}
					else if(GameConfig.LEFT)
					{
						GameConfig.RIGHT = true;
						GameConfig.LEFT = false;
						GameConfig.player.move();
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
		map[i][j].decreaseHealth(GameConfig.player.getWeapon().getDamage()*delta);
		
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
