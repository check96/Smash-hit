package entity;

import com.badlogic.gdx.math.Vector3;

public class Weapon extends AbstractGameObject 
{
	private float damage;
	
	public Weapon(Vector3 _position, float _damage)
	{
		super(_position,new Vector3(4.3f,2f,0.6f));
		damage = _damage;	
		
	}
	
	public final double getDamage() {return this.damage;}
	
	public void setDamage(float _damage)	{ this.damage = _damage;}
		
	public void move(Player player)
	{
		this.boundingBox.Position.x = player.getPosition().x + 5;
		this.boundingBox.Position.z = player.getPosition().z;
	}
}
