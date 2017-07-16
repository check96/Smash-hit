package videogame;

import com.badlogic.gdx.math.Vector3;

import entity.Player;
import entity.Weapon;

public class MultiplayerWorld extends World 
{
	private int id;
	
	public MultiplayerWorld()
	{
//		this.id = id;
		GameConfig.LOCAL_COINS = 0;
		
		Player player = new Player(new Vector3(0,-4.8f, 15+(20*id)), 4);
		Weapon weapon = new Weapon(new Vector3(0.5f,-4.5f,40));
		player.setWeapon(weapon);
		
//		GameConfig.players.set
	}

	@Override
	public void update(float delta) {
		synchronized (GameConfig.tools)
		{
			map = GameConfig.tools.get(GameConfig.actualLevel-1);
		}
		
		GameConfig.player.move(delta);
		checkCollsion(delta);
		checkPlayerCollision(delta);
		
		GameConfig.ON = false;
		GameConfig.LEFT = false;
		GameConfig.RIGHT = false;
		GameConfig.BACK = false;
		
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

	private void checkPlayerCollision(float delta) 
	{
		
	}
	
	
}