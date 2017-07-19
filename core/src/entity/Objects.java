package entity;

import com.badlogic.gdx.math.Vector3;

public enum Objects
{
	FLOOR(0,100,0,0,0,0), DESK(1,50,500,5.5f,8,6), PLANT(2,10,100,4,11,4), LOCKER(3,30,300,7,5,5f),
	CHAIR(4,40,400,4.5f,10,4.5f), DOOR(4,5,1000,5,5.5f,6), CLOCK(6,1,0,3,2,3), VORTEX(7,1,0,3,2,3);
	
	public int id;
	public int health;
	public int score;
	Vector3 size;
	
	private Objects(int id, int health, int score, float x, float y, float z)
	{
		this.id = id;
		this.health = health;
		this.score = score;
		this.size = new Vector3(x,y,z);
	}
}