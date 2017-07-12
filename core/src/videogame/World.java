package videogame;

import java.util.ListIterator;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

import entity.Destroyable;
import entity.Objects;
import entity.Player;
import entity.Wall;
import entity.Walls;
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
		
		GameConfig.ON = false;
		GameConfig.LEFT = false;
		GameConfig.RIGHT = false;
		GameConfig.BACK = false;
		
		if(GameConfig.HIT)
		{
			hit(delta);
		}
		
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
	
	private void checkWallCollision(float delta)
	{
		for (Wall wall : GameConfig.walls)
		{
			if(wall.type != Walls.CEILING && wall.type != Walls.FLOOR)
				if(GameConfig.player.collide(wall))
				{
//					System.out.println(wall.type);
					reaction(delta);
				}
		}
	}

	private void checkCollsion(float delta)
	{
    	int	i = (int) ((GameConfig.player.getX() + 4.5f)/ GameConfig.CELL_HEIGHT) % GameConfig.ROOM_ROW;
    	int	j = (int) ((GameConfig.player.getZ() + 3.5f) / GameConfig.CELL_WIDTH) % GameConfig.ROOM_COLUMN;
		
		if(i == 0 || i == GameConfig.ROOM_ROW-1 || j == 0 || j == GameConfig.ROOM_COLUMN-1 )
			checkWallCollision(delta);

		if(map[i][j] instanceof Destroyable)
		{
			if(GameConfig.player.collide(map[i][j]));
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
		{
			GameConfig.ON = false;
			GameConfig.BACK = true;
			GameConfig.player.move(delta);
			GameConfig.ON = true;
			GameConfig.BACK = false;
		}
		else if(GameConfig.BACK)
		{
			GameConfig.BACK = false;
			GameConfig.ON = true;
			GameConfig.player.move(delta);
			GameConfig.BACK = true;
			GameConfig.ON = false;
		}
		else if(GameConfig.LEFT)
		{
			GameConfig.LEFT = false;
			GameConfig.RIGHT = true;
			GameConfig.player.move(delta);
			GameConfig.LEFT = true;
			GameConfig.RIGHT = false;
		}
		else if(GameConfig.RIGHT)
		{
			GameConfig.RIGHT = false;
			GameConfig.LEFT = true;
			GameConfig.player.move(delta);
			GameConfig.RIGHT = true;
			GameConfig.LEFT = false;
		}
	}

	private void hit(float delta)
	{
		GameConfig.ON = true;
		GameConfig.player.move(delta);
		
		int	i = (int) ((GameConfig.player.getX() + 4.5f)/ GameConfig.CELL_HEIGHT) % GameConfig.ROOM_ROW;
    	int	j = (int) ((GameConfig.player.getZ() + 3.5f) / GameConfig.CELL_WIDTH) % GameConfig.ROOM_COLUMN;
    	
    	GameConfig.ON = false;
		GameConfig.BACK = true;
		GameConfig.player.move(delta);
		GameConfig.BACK = false;
		
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

    		System.out.println(map[i][j].type + "  "+map[i][j].getHealth());
		
			if(map[i][j].getHealth() == 0)
			{
				System.out.println(map[i][j].type);
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
					
					boolean clock = false;
					if(map[i][j].type == Objects.CLOCK)
						clock = true;
					
					map[i][j] = null;
	
					while(iterator.hasNext())
					{
						ModelInstance instance = iterator.next();
						Vector3 position = new Vector3();
						if(instance.transform.getTranslation(position).equals(objPosition))
						{
							if(!clock)
								GameConfig.destroyed.add(instance);
							
							iterator.remove();
							break;
						}
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
