package videogame;

import com.badlogic.gdx.math.Vector3;

import entity.Destroyable;
import entity.Objects;
import entity.Player;
import entity.Weapon;

public class World 
{
	private int id;
	
	public World(int _id)
	{ 
		this.id = _id;
		Player player = new Player(new Vector3(0,-4.5f,28), null);
		Weapon weapon = new Weapon(new Vector3(20,6,5), 50);
		player.setWeapon(weapon);
		
		GameConfig.players.add(id, player);		
	}

	public void update()
	{		
//		for(int k = 0; k<GameConfig.newTools.length; k++)
//		{	for(int h = 0; h<GameConfig.newTools[k].length; h++)
//				if(GameConfig.tools.get(GameConfig.actualLevel-1)[h][k] != null)
//					System.out.print(GameConfig.tools.get(GameConfig.actualLevel-1)[h][k].type+ " ");
//				else
//					System.out.print(0+ " ");
//				
//			System.out.println();
//		}	
		
//		System.out.println();
//		System.out.println();
		
//		checkCollsion();
		GameConfig.players.get(id).move();
		
		GameConfig.ON =false;
		GameConfig.LEFT = false;
		GameConfig.RIGHT = false;
		GameConfig.BACK = false;
		GameConfig.HIT = false;
		
		synchronized (GameConfig.players)
		{
			if(GameConfig.players.get(id).getX() > GameConfig.ROOM_DIMENSION * 5.5f * GameConfig.actualLevel)
				GameConfig.actualLevel++;
		}		
		checkGameOver();
	}
	
	private void checkCollsion()
	{
		int i = 0, j = 0;;
		synchronized (GameConfig.players)
		{
			i = (int) (GameConfig.players.get(id).getX() / GameConfig.ROOM_DIMENSION);
			j = (int) (GameConfig.players.get(id).getZ() / GameConfig.ROOM_DIMENSION);
		}
		
		if(GameConfig.ON && i != GameConfig.ROOM_DIMENSION-1)
			if(GameConfig.tools.get(GameConfig.actualLevel-1)[i+1][j] != null);
				GameConfig.ON = false;

		if(GameConfig.BACK && i != 0)
			if( GameConfig.tools.get(GameConfig.actualLevel-1)[i-1][j] != null)
				GameConfig.BACK = false;
		 
		if(GameConfig.LEFT && j!=0)
			if( GameConfig.tools.get(GameConfig.actualLevel-1)[i][j-1] != null)
				GameConfig.LEFT = false;
		
		if(GameConfig.RIGHT && j!=GameConfig.ROOM_DIMENSION-1)
			if( GameConfig.tools.get(GameConfig.actualLevel-1)[i][j+1] != null)
				GameConfig.RIGHT = false;
		
		/*
			if(GameConfig.tools.get(GameConfig.actualLevel-1)[i+1][j] == null);		
			{	System.out.println("not null");
				if (GameConfig.players.get(id).collide(GameConfig.tools.get(GameConfig.actualLevel-1)[i+1][j]))
				{
					System.out.println("collide");
					GameConfig.ON = false;
					GameConfig.BACK = true;
					GameConfig.players.get(id).move();
					GameConfig.BACK = false;
					
					if(GameConfig.HIT)
					{
						GameConfig.HIT = false;
						hit(i+1,j);
					}
				}
			}
		}
		else if(GameConfig.BACK && i != 0)
		{
			if( GameConfig.tools.get(GameConfig.actualLevel-1)[i-1][j] != null)
			{
				if (GameConfig.players.get(id).collide(GameConfig.tools.get(GameConfig.actualLevel-1)[i-1][j]))
				{
					GameConfig.ON = true;
					GameConfig.BACK = false;
					GameConfig.players.get(id).move();
					GameConfig.ON = false;
				}	
			}
		}
		if(GameConfig.RIGHT && j!=GameConfig.ROOM_DIMENSION-1)
		{
			if( GameConfig.tools.get(GameConfig.actualLevel-1)[i][j+1] != null)
			{
				if (GameConfig.players.get(id).collide(GameConfig.tools.get(GameConfig.actualLevel-1)[i][j+1]))
				{
					GameConfig.RIGHT = false;
					GameConfig.LEFT = true;
					GameConfig.players.get(id).move();
					GameConfig.LEFT = false;
				}
			}
		}
		else if(GameConfig.LEFT && j!=0)
		{
			if( GameConfig.tools.get(GameConfig.actualLevel-1)[i][j-1] != null)
			{
				if (GameConfig.players.get(id).collide(GameConfig.tools.get(GameConfig.actualLevel-1)[i][j-1]))
				{
					GameConfig.LEFT = false;
					GameConfig.RIGHT = true;
					GameConfig.players.get(id).move();
					GameConfig.RIGHT = false;
				}			
			}
		}*/
	}

	private void hit(int i, int j)
	{
		// decrease tool's life
		GameConfig.tools.get(GameConfig.actualLevel-1)[i][j].decreaseHealth(GameConfig.players.get(id).getWeapon().getDamage());
		
		if(GameConfig.tools.get(GameConfig.actualLevel-1)[i][j].getHealth() == 0)
		{
			if(GameConfig.tools.get(GameConfig.actualLevel-1)[i][j].type == Objects.CLOCK)
				Countdown.increment(5);
			
			// add score and coins
			GameConfig.SCORE += GameConfig.tools.get(GameConfig.actualLevel-1)[i][j].type.score;
			GameConfig.COINS += GameConfig.tools.get(GameConfig.actualLevel-1)[i][j].getMoneyReward();
			
			
			//remove tools and toolsInstance
			synchronized (GameConfig.toolsInstance.get(GameConfig.actualLevel-1))
			{
				GameConfig.toolsInstance.get(GameConfig.actualLevel-1).remove(i*GameConfig.ROOM_DIMENSION + j%GameConfig.ROOM_DIMENSION);
			}

			synchronized (GameConfig.tools.get(GameConfig.actualLevel-1))
			{
				GameConfig.tools.get(GameConfig.actualLevel-1)[i][j] = null;
			}
		}
	
	}
	
	private void checkGameOver()
	{
		if(Countdown.getTime() == 0)
			GameConfig.GAME_OVER = true;
	}
}
