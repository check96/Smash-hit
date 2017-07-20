package entity;

import com.badlogic.gdx.math.Vector3;

public class Destroyable extends AbstractGameObject
{
	private int health;
	private float moneyReward;
	public Objects type;
	
	public Destroyable (Vector3 _position, float money, Objects _type)
	{
		super(_position, _type.size);
		this.type = _type;
		this.health = type.health;
		this.moneyReward = money;
	}
	

	@Override
	public String toString()
	{
		return(type + "  (" + Position.x +","+ Position.z+") ");
	}
	
	
	public final int getHealth() {return health;}
	public final float getMoneyReward()	{return moneyReward;}
	
	public void decreaseHealth(double damage)
	{
		health -= damage;
		if(health < 0)
			health = 0;
	}


	public void setHealth(int health)
	{
		this.health = health;
		
		if(health < 0)
			health = 0;
	}

	

}
