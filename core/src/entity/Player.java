package entity;

import com.badlogic.gdx.math.Vector3;
import videogame.GameConfig;


public class Player extends AbstractGameObject
{	
	private float speed = 5f;
	private Weapon weapon;
	
	public Player (Vector3 _position, Vector3 _size)
	{
		super(_position, _size);
	}
	public void setWeapon(Weapon weapon) {this.weapon = weapon;}
	public void setX(float _x)  {Position.x = _x;}	
	public void setZ(float _z)  {Position.z = _z;}
	
	public final Weapon getWeapon() {return weapon;}
	
	public void move()
	{				
		if(GameConfig.RIGHT)
		{
			Position.x -= GameConfig.DIRECTION.z/speed;	
			Position.z += GameConfig.DIRECTION.x/speed;
		}
		if(GameConfig.LEFT)	
		{
			Position.x += GameConfig.DIRECTION.z/speed;
			Position.z -= GameConfig.DIRECTION.x/speed; 
		}
		
		if(GameConfig.ON)	
		{
			Position.x += GameConfig.DIRECTION.x/speed;
			Position.z += GameConfig.DIRECTION.z/speed;
		}
		
		if(GameConfig.BACK)	
		{
			Position.x -= GameConfig.DIRECTION.x/speed;	
			Position.z -= GameConfig.DIRECTION.z/speed;
		}
		
		weapon.move(this);
	}
}