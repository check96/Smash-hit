package videogame;

import com.badlogic.gdx.math.Vector3;

import GameGui.Deleter;
import entity.Destroyable;
import entity.Objects;
import entity.Player;
import entity.Wall;
import entity.Walls;
import entity.Weapon;
import videogame.bonus.Bomb;
import videogame.bonus.Tornado;

public class World 
{
	protected Destroyable[][] map;
	private Bomb bomb;
	
	public World()
	{ 
		GameConfig.LOCAL_COINS = 0;
		
		GameConfig.player = new Player(new Vector3(0,-4.8f,45), 4);
		Weapon weapon = new Weapon(new Vector3(0.5f,-4.5f,20));
		GameConfig.player.setWeapon(weapon);
	}

	public final Bomb getBomb()	{return bomb;}
	
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
		
		bonusManagement(delta);
				
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
	
	private void bonusManagement(float delta)
	{
		if(GameConfig.STATE.equals("bomb1"))
		{
			if(!(bomb instanceof Bomb) && GameConfig.numBomb1 > 0)
				bomb = new Bomb(false);
		}
		else if(GameConfig.STATE.equals("bomb2"))
		{
			if(!(bomb instanceof Bomb) && GameConfig.numBomb2 > 0)
				bomb = new Bomb(true);
		}
		if(GameConfig.STATE.equals("tornado"))
		{
			Tornado.check();
			hit(delta);
		}
		
		if(GameConfig.HIT) 
		{
			if( GameConfig.STATE.equals("hit"))
				 hit(delta);
			else if(GameConfig.STATE.equals("bomb1") && GameConfig.numBomb1 > 0)
			{
				if(!bomb.inAction())
				{
					bomb.shoot();
					GameConfig.numBomb1--;
				}
			}
			else if(GameConfig.STATE.equals("bomb2") && GameConfig.numBomb2 > 0)
			{
				if(!bomb.inAction())
				{
					bomb.shoot();
					GameConfig.numBomb2--;
				}
			}
		}
		
		if(bomb instanceof Bomb)
		{
			bomb.manageBomb(delta);
			
			if(!bomb.inAction() && bomb.shooted())
			{
				int	x = (int) ((bomb.getX() + 4.5f)/ GameConfig.CELL_HEIGHT) % GameConfig.ROOM_ROW;
		    	int	y = (int) ((bomb.getZ() + 3.5f) / GameConfig.CELL_WIDTH) % GameConfig.ROOM_COLUMN;

		    	if(!bomb.isUpgrade())
		    	{
					if(map[x][y] != null)
						delete(x,y);		
		    	}
		    	else
		    	{
		    		for(int i = x-1; i <= x+1; i++)
		    			for(int j = y-1; j <= y+1; j++)
		    			{
		    				if(i >= 0 && j >= 0 && j<GameConfig.ROOM_COLUMN && i<GameConfig.ROOM_ROW)
		    				{
		    					if(map[i][j] != null)
		    						delete(i,j);		
		    				}
		    			}
		    	}
				bomb = null;
			}
		}
	}

	protected void checkWallCollision(float delta)
	{
		synchronized (GameConfig.walls)
		{
			for (Wall wall : GameConfig.walls)
			{
				if(wall.type != Walls.CEILING && wall.type != Walls.FLOOR)
				{
					if(GameConfig.player.collide(wall))
					{
						reaction(delta);
					}
				}
			}
		}
	}

	protected void checkCollsion(float delta)
	{
    	int	i = (int) ((GameConfig.player.getX() + 4.5f)/ GameConfig.CELL_HEIGHT) % GameConfig.ROOM_ROW;
    	int	j = (int) ((GameConfig.player.getZ() + 3.5f) / GameConfig.CELL_WIDTH) % GameConfig.ROOM_COLUMN;
    	
		if((i == 0 && j !=5)  || (i == GameConfig.ROOM_ROW-1 && j != 5) || j == 0 || j == GameConfig.ROOM_COLUMN-1 )
			checkWallCollision(delta);

		if(map[i][j] instanceof Destroyable)
		{
			if(GameConfig.player.collide(map[i][j]));
			{
				if(map[i][j].type == Objects.CLOCK || map[i][j].type == Objects.VORTEX)
    				hit(delta);
				else
					reaction(delta);
			}
		}
	}
		
	protected void reaction(float delta)
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

	protected void delete(int i, int j)
	{
		// add score and coins
		GameConfig.SCORE += map[i][j].type.score;
		GameConfig.LOCAL_COINS += map[i][j].getMoneyReward() * GameConfig.coinsMultiplier;
		
		boolean clock = false;
		boolean vortex = false;
		
		if(map[i][j].type == Objects.CLOCK)
			clock = true;

		else if(map[i][j].type == Objects.VORTEX)
			vortex = true;
		
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
		
		Deleter.remove(clock,vortex, map[i][j].getPosition(), map[i][j].getMoneyReward());
		map[i][j] = null;
	}
	
	protected void hit(float delta)
	{
		GameConfig.ON = true;
		GameConfig.player.move(delta);
		GameConfig.player.move(delta);
		
		int	i = (int) ((GameConfig.player.getX() + 4.5f)/ GameConfig.CELL_HEIGHT) % GameConfig.ROOM_ROW;
    	int	j = (int) ((GameConfig.player.getZ() + 3.5f) / GameConfig.CELL_WIDTH) % GameConfig.ROOM_COLUMN;
    	
    	GameConfig.ON = false;
		GameConfig.BACK = true;
		GameConfig.player.move(delta);
		GameConfig.player.move(delta);
		GameConfig.BACK = false;
		
		// decrease tool's life
    	if(map[i][j] instanceof Destroyable)
    	{
    		if(GameConfig.player.collide(map[i][j]))
    		{
    			if(map[i][j].type == Objects.CLOCK || map[i][j].type == Objects.VORTEX)
    				map[i][j].decreaseHealth(map[i][j].getHealth());
    			else
    				map[i][j].decreaseHealth(GameConfig.player.getWeapon().getDamage()*delta);
    		}


			if(map[i][j].getHealth() == 0)
			{
				if(map[i][j].type == Objects.CLOCK)
					Countdown.increment(5 * (GameConfig.clockLevel+1));
				else if (map[i][j].type == Objects.VORTEX)
				{
					GameConfig.stateIndex = 3;
					GameConfig.STATE = "tornado";
				}
									
				//remove tools and toolsInstance
				delete(i,j);
			}
    	}
	}
	
	protected void checkGameOver()
	{
		if(Countdown.getTime() == 0)
			GameConfig.GAME_OVER = true;
	}
}
