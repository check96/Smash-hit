package GameGui;

import videogame.GameConfig;

public class TimeXplosion extends Thread {
	
	public TimeXplosion()
	{
		this.start();
	}
	public void run()
	{
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(GameConfig.xplosion1)
			GameConfig.xplosion1 = false;
		else if(GameConfig.xplosion2)
			GameConfig.xplosion2 = false;
		GameConfig.bombXplosion = null;
	}

}
