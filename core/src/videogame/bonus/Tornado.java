package videogame.bonus;

import videogame.GameConfig;

public class Tornado
{
	public static boolean active = false;
	public static long startTime = 0;
	public static long duration = 10000;
	
	public static void check()
	{
		if(!active)
		{
			GameConfig.tornadoSound = true;
			active = true;
			startTime = System.currentTimeMillis();
			GameConfig.player.getWeapon().setDamage(GameConfig.player.getWeapon().getDamage()*1000);
		}

		if(active && (System.currentTimeMillis() - startTime) > (duration + duration/2 * GameConfig.vortexLevel))
		{
			GameConfig.tornadoSoundStop = true;
			active = false;
			startTime = 0;
			GameConfig.player.getWeapon().setDamage(GameConfig.player.getWeapon().getDamage()/1000);
			GameConfig.stateIndex = 0;
			GameConfig.STATE = "hit";
		}
	}
}
