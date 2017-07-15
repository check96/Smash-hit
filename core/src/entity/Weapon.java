package entity;

import com.badlogic.gdx.math.Vector3;

public class Weapon extends AbstractGameObject 
{
	private float damage;
	
	public Weapon(Vector3 _position)
	{
		super(_position,new Vector3(4.3f,2f,0.6f));
		this.damage = 15;
	}
	
	public final float getDamage() {return damage;}
	public void setDamage(float _damage)	{ this.damage = _damage;}
		
	public void move(Player player)
	{
		this.Position.x = player.getPosition().x + 3;
		this.Position.z = player.getPosition().z;
	}
}
