package entity;

import com.badlogic.gdx.math.Vector3;
import videogame.GameConfig;

public class Player extends AbstractGameObject
{	
	private Weapon weapon;
	private Vector3 velocity = new Vector3(20,20,20);
	private String username;
	
	public Player (Vector3 _position, float radius)
	{
		super(_position, radius);
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
	
	public void move(float delta)
	{			
		Vector3 speed = new Vector3(velocity.x*GameConfig.DIRECTION.x, velocity.y*GameConfig.DIRECTION.y, velocity.z*GameConfig.DIRECTION.z);
		
		if(GameConfig.RIGHT)
		{
			Position.x -= speed.z * delta;	
			Position.z += speed.x * delta;
		}
		if(GameConfig.LEFT)	
		{
			Position.x += speed.z * delta;
			Position.z -= speed.x * delta; 
		}
		
		if(GameConfig.ON)	
		{
			Position.x += speed.x * delta;
			Position.z += speed.z * delta;
		}
		
		if(GameConfig.BACK)	
		{
			Position.x -= speed.x * delta;	
			Position.z -= speed.z * delta;
		}
		
		weapon.move(this);
	}
}