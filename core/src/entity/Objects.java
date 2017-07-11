package entity;

import com.badlogic.gdx.math.Vector3;

public enum Objects
{
	FLOOR(0,100,0,0,0,0),DESK(1,100,100,5.5f,7,6), PRINTER(2,60,300,6,7,6), PLANT(3,30,150, 2,6f,2f),
	LOCKER(4,40,200,3f,5,5f), CHAIR(5,50,50,4.5f,58,4.5f), DOOR(6,50,1000,6f,5.5f,6), CLOCK(7,200,500,0.5f,2,2.5f);
	
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