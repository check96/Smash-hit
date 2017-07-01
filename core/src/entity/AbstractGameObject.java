package entity;

import com.badlogic.gdx.math.Vector3;

public abstract class AbstractGameObject implements ICollidable
{
	protected Vector3 Position;
	protected Vector3 Size;
	protected float Radius;
	
	public AbstractGameObject( Vector3 _position, Vector3 _size)
	{
		Position = new Vector3(_position);
		Size = new Vector3(_size);
		Radius=0;
	}
	public AbstractGameObject(Vector3 _position, float _radius)
	{
		Position = new Vector3(_position);
		Size = null;
		Radius = _radius;
	}
	
	public void setPosition(Vector3 pos)	{Position = pos;}
	public void setSize(Vector3 size)		{Size = size;}
	public void setRadius(float radius) 	{Radius = radius;}
	
	public final Vector3 getPosition()	{return this.Position;}
	public final Vector3 getSize()		{return this.Size;}	
	public final float getRadius() 		{return this.Radius;}
	public final float getX() {return this.Position.x;}
	public final float getY() {return this.Position.y;}
	public final float getZ() {return this.Position.z;}
	
	public boolean collide(ICollidable c)
	{		

		AbstractGameObject a =(AbstractGameObject) c;
		double squaredDistance = SquaredDistPoint(this.Position, a);
		return squaredDistance <= (this.Radius * this.Radius);
		
		/*
		if (getX() <= a.getX() + a.getSize().x && a.getX() <= getX() + getSize().x &&
			getY() <= a.getY() + a.getSize().y && a.getY() <= getY() + getSize().y &&
			getZ() <= a.getZ() + a.getSize().z && a.getZ() <= getZ() + getSize().z)
				return true;
		
		return false;*/
	}
	
	public double SquaredDistPoint(Vector3 p, AbstractGameObject aabb)
	{
		double sq = 0.0;
		sq += check( p.x, aabb.getX(), aabb.getX()+aabb.getSize().x );
		sq += check( p.y, aabb.getY(), aabb.getY()+aabb.getSize().y );
		sq += check( p.z, aabb.getZ(), aabb.getZ()+aabb.getSize().z );
			 
		return sq;
	
	}
	
	public double check( float pn,  float bmin, float bmax )
	{
		float out = 0;
		float v = pn;
		 
		if ( v < bmin ) 
		{             
			float val = (bmin - v);             
		    out += val * val;         
		}         
		         
		if ( v > bmax )
		{
			float val = (v - bmax);
		    out += val * val;
		}
		 
		return out;
	}
}