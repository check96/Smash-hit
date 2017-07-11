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
		GameConfig.player = new Player(new Vector3(0,-4.8f,45), 4);
		Weapon weapon = new Weapon(new Vector3(0.5f,-4.5f,40), Weapons.MACE);
		GameConfig.player.setWeapon(weapon);
	}

	public void update(float delta)
	{		
		synchronized (GameConfig.tools)
		{
			map = GameConfig.tools.get(GameConfig.actualLevel-1);
		}
		
		GameConfig.player.move(delta);
		checkCollsion(delta);
		
//		if(GameConfig.HIT)
//		{
//			hit(i,j+1,delta);
//		}
		
		
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
	
	private void checkWallCollision(int i, int j,float delta)
	{
		if(i == 0 && GameConfig.player.getX() < GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT * GameConfig.actualLevel)
		{
			if(GameConfig.ON)
			{
				System.out.println("ON");
				GameConfig.BACK = true;
				GameConfig.ON = false;
				GameConfig.player.move(delta);
			}
			else if(GameConfig.BACK)
			{
				System.out.println("BACK");
				GameConfig.BACK = false;
				GameConfig.ON = true;
				GameConfig.player.move(delta);
			}
			else if(GameConfig.RIGHT)
			{
				System.out.println("RIGHT");
				GameConfig.RIGHT = false;
				GameConfig.LEFT = true;
				GameConfig.player.move(delta);
			}
			else if(GameConfig.LEFT)
			{
				System.out.println("LEFT");
				GameConfig.RIGHT = true;
				GameConfig.LEFT = false;
				GameConfig.player.move(delta);
			}
		}
		if(i == GameConfig.ROOM_ROW-1 && GameConfig.player.getX() > GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT * GameConfig.actualLevel)
		{
			if(GameConfig.ON)
			{
				System.out.println("ON");
				GameConfig.BACK = true;
				GameConfig.ON = false;
				GameConfig.player.move(delta);
			}
			else if(GameConfig.BACK)
			{
				System.out.println("BACK");
				GameConfig.BACK = false;
				GameConfig.ON = true;
				GameConfig.player.move(delta);
			}
			else if(GameConfig.RIGHT)
			{
				System.out.println("RIGHT");
				GameConfig.RIGHT = false;
				GameConfig.LEFT = true;
				GameConfig.player.move(delta);
			}
			else if(GameConfig.LEFT)
			{
				System.out.println("LEFT");
				GameConfig.RIGHT = true;
				GameConfig.LEFT = false;
				GameConfig.player.move(delta);
			}
		}
		if(j == 0 && GameConfig.player.getZ() < 0)
		{
			if(GameConfig.ON)
			{
				System.out.println("ON");
				GameConfig.BACK = true;
				GameConfig.ON = false;
				GameConfig.player.move(delta);
			}
			else if(GameConfig.BACK)
			{
				System.out.println("BACK");
				GameConfig.BACK = false;
				GameConfig.ON = true;
				GameConfig.player.move(delta);
			}
			else if(GameConfig.RIGHT)
			{
				System.out.println("RIGHT");
				GameConfig.RIGHT = false;
				GameConfig.LEFT = true;
				GameConfig.player.move(delta);
			}
			else if(GameConfig.LEFT)
			{
				System.out.println("LEFT");
				GameConfig.RIGHT = true;
				GameConfig.LEFT = false;
				GameConfig.player.move(delta);
			}
		}
		if(j == GameConfig.ROOM_ROW-1 && GameConfig.player.getZ() > GameConfig.ROOM_COLUMN * GameConfig.CELL_WIDTH)
		{
			if(GameConfig.ON)
			{
				System.out.println("ON");
				GameConfig.BACK = true;
				GameConfig.ON = false;
				GameConfig.player.move(delta);
			}
			else if(GameConfig.BACK)
			{
				System.out.println("BACK");
				GameConfig.BACK = false;
				GameConfig.ON = true;
				GameConfig.player.move(delta);
			}
			else if(GameConfig.RIGHT)
			{
				System.out.println("RIGHT");
				GameConfig.RIGHT = false;
				GameConfig.LEFT = true;
				GameConfig.player.move(delta);
			}
			else if(GameConfig.LEFT)
			{
				System.out.println("LEFT");
				GameConfig.RIGHT = true;
				GameConfig.LEFT = false;
				GameConfig.player.move(delta);
			}
		}
	}

	private void checkCollsion(float delta)
	{
		int i = 0, j = 0;
		synchronized (GameConfig.player)
		{
    		i = (int) ((GameConfig.player.getX() + 4.5f)/ GameConfig.CELL_HEIGHT) % GameConfig.ROOM_ROW;
    		j = (int) ((GameConfig.player.getZ()+3.5f) / GameConfig.CELL_WIDTH) % GameConfig.ROOM_COLUMN;
		}
		
		System.out.println("i "+i+"   j "+j);
		
		if(map[i][j] != null)
		{
			if(GameConfig.player.collide(map[i][j]));
			{
				System.out.print("collide with "+ map[i][j].type+"  in "+map[i][j].getPosition()+"  of "+ map[i][j].getSize());
				reaction(delta);
			}
		}
	}
		
	private void reaction(float delta)
	{
		if(GameConfig.ON)
		{
			System.out.println("ON");
			GameConfig.ON = false;
			GameConfig.BACK = true;
			GameConfig.player.move(delta);
			GameConfig.BACK = false;
		}
		else if(GameConfig.BACK)
		{
			System.out.println("BACK");
			GameConfig.BACK = false;
			GameConfig.ON = true;
			GameConfig.player.move(delta);
			GameConfig.ON = false;
		}
		if(GameConfig.LEFT)
		{
			System.out.println("LEFT");
			GameConfig.LEFT = false;
			GameConfig.RIGHT = true;
			GameConfig.player.move(delta);
			GameConfig.RIGHT = false;
		}
		else if(GameConfig.RIGHT)
		{
			System.out.println("RIGHT");
			GameConfig.RIGHT = false;
			GameConfig.LEFT = true;
			GameConfig.player.move(delta);
			GameConfig.LEFT = false;
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
				Vector3 objPosition = map[i][j].getPosition();
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
