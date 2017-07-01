package videogame;

import com.badlogic.gdx.math.Vector3;

import entity.Destroyable;
import entity.Objects;
import entity.Player;
import entity.Weapon;
import entity.Weapons;

public class World 
{
	private int id;
	
	public World(int _id)
	{ 
		this.id = _id;
		Player player = new Player(new Vector3(0,-4.5f,28),2);
		Weapon weapon = new Weapon(new Vector3(0.5f,-4.5f,28), Weapons.MACE);
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
		
		GameConfig.players.get(id).move();
		checkCollsion();
		
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
			i = (int) (GameConfig.players.get(id).getX() / 5.5f) % GameConfig.ROOM_DIMENSION;
			j = (int) (GameConfig.players.get(id).getZ() / 5.5f) % GameConfig.ROOM_DIMENSION;
		}
	
		if(i < GameConfig.ROOM_DIMENSION-1)	//controllo a destra
		{
			if(GameConfig.tools.get(GameConfig.actualLevel-1)[i+1][j] instanceof Destroyable)
			{
				System.out.println(GameConfig.players.get(id).getPosition());
				System.out.println(GameConfig.tools.get(GameConfig.actualLevel-1)[i+1][j].getPosition());
				System.out.println();
				
				if(GameConfig.players.get(id).collide(GameConfig.tools.get(GameConfig.actualLevel-1)[i+1][j]))
				{
					if(GameConfig.RIGHT)
						GameConfig.RIGHT = false;
					
				}
			}
		}
		
		if(i > 0)	//controllo a sinistra
		{
			
		}
		
		if(j > 0)	//controllo sopra
		{
			
		}
		if(j < GameConfig.ROOM_DIMENSION-1)	//controllo sotto
		{
			
		}
		/*
		if(i != GameConfig.ROOM_DIMENSION-1)
		{
			if(GameConfig.tools.get(GameConfig.actualLevel-1)[i+1][j] instanceof Destroyable)
			{
				if(GameConfig.players.get(id).collide(GameConfig.tools.get(GameConfig.actualLevel-1)[i+1][j]));
				{
					System.out.print("collide with: "+GameConfig.tools.get(GameConfig.actualLevel-1)[i+1][j].toString()+" on: ");
					if(GameConfig.ON)
					{
						System.out.println("ON");
						GameConfig.BACK = true;
						GameConfig.ON = false;
						GameConfig.players.get(id).move();
					}
					else if(GameConfig.BACK)
					{
						System.out.println("BACK");
						GameConfig.BACK = false;
						GameConfig.ON = true;
						GameConfig.players.get(id).move();
					}
					else if(GameConfig.RIGHT)
					{
						System.out.println("RIGHT");
						GameConfig.RIGHT = false;
						GameConfig.LEFT = true;
						GameConfig.players.get(id).move();
					}
					else if(GameConfig.LEFT)
					{
						System.out.println("LEFT");
						GameConfig.RIGHT = true;
						GameConfig.LEFT = false;
						GameConfig.players.get(id).move();
					}
				}
			}
		}
	/*	else if(i != 0)
		{
			if(GameConfig.tools.get(GameConfig.actualLevel-1)[i-1][j] != null)
				if(GameConfig.tools.get(GameConfig.actualLevel-1)[i-1][j].collide(GameConfig.players.get(id)));
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
				}
		}
		else if(j!=0)
		{
			if(GameConfig.tools.get(GameConfig.actualLevel-1)[i][j-1] != null)
				if(GameConfig.tools.get(GameConfig.actualLevel-1)[i][j-1].collide(GameConfig.players.get(id)));
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
				}
		}
		else if(j!=GameConfig.ROOM_DIMENSION-1)
		{
			if(GameConfig.tools.get(GameConfig.actualLevel-1)[i][j+1] != null)
				if(GameConfig.tools.get(GameConfig.actualLevel-1)[i][j+1].collide(GameConfig.players.get(id)));
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
