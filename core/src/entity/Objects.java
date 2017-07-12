package entity;

import com.badlogic.gdx.math.Vector3;

public enum Objects
{
	FLOOR(0,100,0,0,0,0),DESK(1,50,500,5.5f,7,6), TRASH(2,60,300,6,7,6), PLANT(3,10,100, 4,6,4),
	LOCKER(4,30,300,3f,5,5f), CHAIR(5,40,400,4.5f,58,4.5f), DOOR(6,5,1000,6f,5.5f,6), CLOCK(7,1,0,3,2,3);
	
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