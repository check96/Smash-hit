package entity;

import com.badlogic.gdx.math.Vector3;

public abstract class AbstractGameObject implements ICollidable
{
	protected Box boundingBox;
	
	public AbstractGameObject( Vector3 _position, Vector3 _size)
	{
		boundingBox = new Box(_position, _size);
	}
	
	public void setPosition(Vector3 pos)	{boundingBox.Position = pos;}
	public void setSize(Vector3 size)	{boundingBox.Size = size;}
	
	public final Vector3 getPosition()	{return this.boundingBox.Position;}
	public final Vector3 getSize()		{return this.boundingBox.Size;}	
	public final float getX() {return this.boundingBox.Position.x;}
	public final float getY() {return this.boundingBox.Position.y;}
	public final float getZ() {return this.boundingBox.Position.z;}
	
	public boolean collide(ICollidable c)
	{	
		AbstractGameObject a =(AbstractGameObject) c;
		if (getX() < a.getX() + a.getSize().x && a.getX() < getX() + getSize().x &&
			getY() < a.getY() + a.getSize().y && a.getY() < getY() + getSize().y &&
			getZ() < a.getZ() + a.getSize().z && a.getZ() < getZ() + getSize().z)
				return true;
		
		return false;
	}
}