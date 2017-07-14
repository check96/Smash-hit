package videogame.bonus;

import entity.Destroyable;
import videogame.GameConfig;

public class Tornado
{
	public static boolean active = false;
	public static boolean collide = false;
	public static int x,y;
	private static long startTime = 0;
	private static long duration = 10000;
	
	public static void check()
	{
		if(!active)
		{
			active = true;
			startTime = System.currentTimeMillis();
			GameConfig.player.getWeapon().setDamage(GameConfig.player.getWeapon().getDamage()*100);
		}
		int	x = (int) ((GameConfig.player.getX() + 4.5f)/ GameConfig.CELL_HEIGHT) % GameConfig.ROOM_ROW;
    	int	y = (int) ((GameConfig.player.getZ() + 3.5f) / GameConfig.CELL_WIDTH) % GameConfig.ROOM_COLUMN;
    	
    	for(int i = x-1; i <= x+1; i++)
			for(int j = y-1; j <= y+1; j++)
			{
				if(i >= 0 && j >= 0 && j<GameConfig.ROOM_COLUMN && i<GameConfig.ROOM_ROW)
					checkCollsion(i,j);		
			}
		
		if(active && (System.currentTimeMillis() - startTime) > duration)
		{
			active = false;
			startTime = 0;
			GameConfig.player.getWeapon().setDamage(GameConfig.player.getWeapon().getDamage()/100);
			GameConfig.stateIndex = 0;
			GameConfig.STATE = "hit";
		}
	}
	
	private static void checkCollsion(int i, int j)
	{
    	Destroyable object = GameConfig.tools.get(GameConfig.actualLevel-1)[i][j];
		
    	if(object instanceof Destroyable)
		{
			if(GameConfig.player.collide(object));
			{
				collide = true;
				x = i;
				y = j;
			}
		}
	}

}
