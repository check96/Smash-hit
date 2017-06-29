package entity;

import com.badlogic.gdx.math.Vector3;

import videogame.GameConfig;

public enum Walls
{
	RIGHT_WALL(85,22,1), LEFT_WALL(85,22,1), FOREWARD_WALL(1,22,42),
	BACK_WALL(1,22,85), CEILING(GameConfig.ROOM_DIMENSION*5.5f,1,GameConfig.ROOM_DIMENSION*5.5f),
	FLOOR(GameConfig.ROOM_DIMENSION*5.5f,1,GameConfig.ROOM_DIMENSION*5.5f);
	
	Vector3 size;
	
	private Walls(float x, float y, float z)
	{
		size = new Vector3(x,y,z);
	}
}