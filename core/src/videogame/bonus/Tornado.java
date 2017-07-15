package videogame.bonus;

import videogame.GameConfig;

public class Tornado
{
	public static boolean active = false;
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

		if(active && (System.currentTimeMillis() - startTime) > duration)
		{
			active = false;
			startTime = 0;
			GameConfig.player.getWeapon().setDamage(GameConfig.player.getWeapon().getDamage()/100);
			GameConfig.stateIndex = 0;
			GameConfig.STATE = "hit";
		}
	}
}
