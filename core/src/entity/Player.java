package entity;

import com.badlogic.gdx.math.Vector3;
import videogame.GameConfig;

public class Player extends AbstractGameObject
{	
	private Weapon weapon;
	
	public Player (Vector3 _position, Vector3 _size)
	{
		super(_position, _size);
	}
	public void setWeapon(Weapon weapon) {this.weapon = weapon;}
	public void setX(float _x)  {Position.x = _x;}	
	public void setZ(float _z)  {Position.z = _z;}
	
	public final Weapon getWeapon() {return weapon;}
	
	public void move(float delta)
	{			
		Vector3 speed = new Vector3(GameConfig.SPEED.x*GameConfig.DIRECTION.x,GameConfig.SPEED.y*GameConfig.DIRECTION.y,GameConfig.SPEED.z*GameConfig.DIRECTION.z);
		
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