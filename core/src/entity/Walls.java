package entity;

import com.badlogic.gdx.math.Vector3;
import videogame.GameConfig;

public enum Walls
{
	RIGHT_WALL(85,22,1), LEFT_WALL(85,22,1), FOREWARD_WALL(1,22,42), BACK_WALL(1,22,20),
	CEILING(GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT,1,GameConfig.ROOM_COLUMN * GameConfig.CELL_WIDTH),
	FLOOR(GameConfig.ROOM_ROW * GameConfig.CELL_HEIGHT,1,GameConfig.ROOM_COLUMN * GameConfig.CELL_WIDTH);
	
	Vector3 size;
	
	private Walls(float x, float y, float z)
	{
		size = new Vector3(x,y,z);
	}
}