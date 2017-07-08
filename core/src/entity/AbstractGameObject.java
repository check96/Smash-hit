package entity;

import com.badlogic.gdx.math.Vector3;

public abstract class AbstractGameObject implements ICollidable
{
	protected Vector3 Position;
	protected Vector3 Size;
	
	public AbstractGameObject( Vector3 _position, Vector3 _size)
	{
		Position = _position;
		Size = _size;
	}
	
	public void setPosition(Vector3 pos)	{Position = pos;}
	public void setSize(Vector3 size) 	{Size = size;}
	
	public final Vector3 getPosition()	{return this.Position;}
	public final Vector3 getSize()		{return this.Size;}	
	public final float getX() {return this.Position.x;}
	public final float getY() {return this.Position.y;}
	public final float getZ() {return this.Position.z;}
	
	public boolean collide(ICollidable c)
	{		
		AbstractGameObject a =(AbstractGameObject) c;
		
//		  if((rect1.x > rect2.x + rect2.w || rect1.x + rect1.w < rect2.x) || (rect1.y > rect2.y + rect2.w || rect1.y + rect1.w < rect2.y)){
	         
//		return (x <= a.x+a.width && a.x <= x+width  && y <= a.y+a.height && a.y <= y+height);
        //      x <= a.x + a.width &&  x+width > a.x
		
		if (getX() + getSize().x/2 > a.getX() + a.getSize().x/2 || getX() + getSize().x/2 < a.getX() + a.getSize().x/2 ||
//			getY() - getSize().y/2 <= a.getY() + a.getSize().y/2 && a.getY() - a.getSize().y/2 <= getY() + getSize().y/2 &&
			getZ() - getSize().z/2 > a.getZ() + a.getSize().z/2 || getZ() + getSize().z/2 < a.getZ() - a.getSize().z/2)
				return false;
		
		return true;
	}
}