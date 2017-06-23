package entity;

import com.badlogic.gdx.math.Vector3;
import videogame.GameConfig;


public class Player extends AbstractGameObject
{	
	private float speed = 10f;
	private Weapon weapon;
	public Player (Vector3 _position, Weapon _weapon)
	{
		super(_position, new Vector3(2,10.5f,2));
		this.weapon = _weapon;
	}
	
	public void setWeapon(Weapon weapon) {this.weapon = weapon;}
	public void setX(float _x)  {boundingBox.Position.x = _x;}	
	public void setZ(float _z)  {boundingBox.Position.z = _z;}
	
	public final Weapon getWeapon() {return weapon;}
	
	public void move()
	{				
		float position = GameConfig.ROOM_DIMENSION *5.5f* (GameConfig.actualLevel-1);
		
//    	int i = (int)((getX()-position)/5.5f);
//    	int j = (int)(getZ()/5.5f);
//    	
//		System.out.println("x: "+getX()+"  z: "+getZ());
//		System.out.println("i: "+i+"  j: "+j);

		if(GameConfig.RIGHT)
		{
			boundingBox.Position.x -= GameConfig.DIRECTION.z/speed;	
			boundingBox.Position.z += GameConfig.DIRECTION.x/speed;
		
//			GameConfig.RIGHT = false;
		}
		if(GameConfig.LEFT)	
		{
			boundingBox.Position.x += GameConfig.DIRECTION.z/speed;
			boundingBox.Position.z -= GameConfig.DIRECTION.x/speed; 
//			GameConfig.LEFT = false;
		}
		
		if(GameConfig.ON)	
		{
			boundingBox.Position.x += GameConfig.DIRECTION.x/speed;
			boundingBox.Position.z += GameConfig.DIRECTION.z/speed;
//			GameConfig.ON = false;
		}
		
		if(GameConfig.BACK)	
		{
			boundingBox.Position.x -= GameConfig.DIRECTION.x/speed;	
			boundingBox.Position.z -= GameConfig.DIRECTION.z/speed;
//			GameConfig.BACK = false;
		}
		
		weapon.move(this);
	}
}