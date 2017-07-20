package entity;

import com.badlogic.gdx.math.Vector3;
import videogame.GameConfig;

public class Player extends AbstractGameObject
{	
	private Weapon weapon;
	private Vector3 velocity = new Vector3(20,20,20);
	private String username;
	public int angle;
	
	public Player (Vector3 _position, float radius)
	{
		super(_position, radius);
		angle = 90;
	}
	
	public Player (Vector3 _position, float radius, String username)
	{
		super(_position, radius);
		this.username = username;
	}
	
	public void setWeapon(Weapon weapon) {this.weapon = weapon;}
	public void setX(float _x)  {Position.x = _x;}	
	public void setZ(float _z)  {Position.z = _z;}
	
	public final Weapon getWeapon() 	{return weapon;}
	public final String getUsername() 	{return username;}
	
	public boolean move(float delta, Vector3 direction)
	{	
		if(GameConfig.RIGHT)
		{
			moveRight(delta,direction);
			return true;
		}
		if(GameConfig.LEFT)	
		{
			moveLeft(delta, direction);
			return true;
		}
		if(GameConfig.ON)
		{
			moveOn(delta, direction);
			return true;
		}
		if(GameConfig.BACK)
		{
			moveBack(delta, direction);
			return true;
		}
		
		return false;
	}
	
	public void moveRight(float delta, Vector3 direction)
	{
		Vector3 speed = new Vector3(velocity.x * direction.x, velocity.y * direction.y, velocity.z * direction.z);
		Position.x -= speed.z * delta;	
		Position.z += speed.x * delta;
	}
	
	public void moveLeft(float delta, Vector3 direction)
	{
		Vector3 speed = new Vector3(velocity.x * direction.x, velocity.y * direction.y, velocity.z * direction.z);
		Position.x += speed.z * delta;
		Position.z -= speed.x * delta; 
	}
		
	public void moveOn(float delta, Vector3 direction)
	{
		Vector3 speed = new Vector3(velocity.x * direction.x, velocity.y * direction.y, velocity.z * direction.z);
		Position.x += speed.x * delta;
		Position.z += speed.z * delta;
	}
	
	public void moveBack(float delta, Vector3 direction)
	{
		Vector3 speed = new Vector3(velocity.x * direction.x, velocity.y * direction.y, velocity.z * direction.z);
		Position.x -= speed.x * delta;	
		Position.z -= speed.z * delta;
	}
}