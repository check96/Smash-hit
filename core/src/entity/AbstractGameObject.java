package entity;

import com.badlogic.gdx.math.Vector3;

public abstract class AbstractGameObject implements ICollidable
{
	protected Vector3 Position;
	protected Vector3 Size;
	
	public AbstractGameObject( Vector3 _position, Vector3 _size)
	{
		Position = new Vector3(_position);
		Size = new Vector3(_size);
	}
	
	public void setPosition(Vector3 pos)	{Position = pos;}
	public void setSize(Vector3 size)		{Size = size;}
	
	public final Vector3 getPosition()	{return this.Position;}
	public final Vector3 getSize()		{return this.Size;}	
	public final float getX() {return this.Position.x;}
	public final float getY() {return this.Position.y;}
	public final float getZ() {return this.Position.z;}
	
	public boolean collide(ICollidable c)
	{		

		AbstractGameObject a =(AbstractGameObject) c;
		
		if (getX()- getSize().x/2 <= a.getX() + a.getSize().x/2 && a.getX() - a.getSize().x/2 <= getX() + getSize().x/2 &&
			getY()- getSize().y/2 <= a.getY() + a.getSize().y/2 && a.getY() - a.getSize().y/2 <= getY() + getSize().y/2 &&
			getZ()- getSize().z/2 <= a.getZ() + a.getSize().z/2 && a.getZ() - a.getSize().z/2 <= getZ() + getSize().z/2)
				return true;
		
		return false;
	}
}