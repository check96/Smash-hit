package entity;

import com.badlogic.gdx.math.Vector3;

public enum Objects
{
	FLOOR(0,100,0,0,0,0),DESK(1,100,100,81,74f,141), PRINTER(2,60,300,35,18,30), PLANT(3,30,150,2.2f,6.2f,2.3f),
	LOCKER(4,40,200,34.5f,58,54), CHAIR(5,50,50,34,58,53), DOOR(6,50,1000,4.5f,20.5f,1), CLOCK(7,200,500,0.5f,2,2.5f);
	
	public int id;
	public int health;
	public int score;
	public Vector3 size;
	
	private Objects(int id, int health, int score, float x, float y, float z)
	{
		this.id = id;
		this.health = health;
		this.score = score;
		this.size = new Vector3(x,y,z);
	}
}
