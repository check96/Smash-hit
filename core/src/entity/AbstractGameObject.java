package entity;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

import videogame.AI.Vertex;

public abstract class AbstractGameObject implements ICollidable
{
	protected Vector3 Position;
	protected Vector3 Size;
	protected float radius;
	
	public AbstractGameObject( Vector3 _position, Vector3 _size)
	{
		Position = _position;
		Size = _size;
		radius = 0;
	}
	
	public AbstractGameObject( Vector3 _position, float _radius)
	{
		Position = _position;
		Size = null;
		radius = _radius;
	}
	
	public void setPosition(Vector3 pos)	{Position = pos;}
	public void setSize(Vector3 size) 	{Size = size;}
	public void setRadius(float radius)	{this.radius = radius;}
	
	public final Vector3 getPosition()	{return this.Position;}
	public final Vector3 getSize()		{return this.Size;}
	public final float getRadius()		{return this.radius;}
	
	public final float getX()	 {return this.Position.x;}
	public final float getY() 	 {return this.Position.y;}
	public final float getZ() 	 {return this.Position.z;}
	
	public boolean collide(ICollidable c)
	{		
		AbstractGameObject box =(AbstractGameObject) c;
		
		ArrayList<Vertex> vertex = new ArrayList<Vertex>();
		vertex.add(new Vertex(box.getX() + box.getSize().x/2, box.getZ() - box.getSize().z/2));	// 0
		vertex.add(new Vertex(box.getX() + box.getSize().x/2, box.getZ() + box.getSize().z/2));	// 1
		vertex.add(new Vertex(box.getX() - box.getSize().x/2, box.getZ() - box.getSize().z/2));	// 2
		vertex.add(new Vertex(box.getX() - box.getSize().x/2, box.getZ() + box.getSize().z/2));	// 3

		ArrayList<Vertex> points = new ArrayList<Vertex>();
		
		points.add(new Vertex(getX(), getZ()+radius));	// 1
		points.add(new Vertex(getX() + radius*(float)Math.sqrt(2)/2, getZ() + radius*(float)Math.sqrt(2)/2));	// 2
		points.add(new Vertex(getX() + radius, getZ()));	// 3
		points.add(new Vertex(getX() + radius*(float)Math.sqrt(2)/2, getZ() - radius*(float)Math.sqrt(2)/2));	// 4
		points.add(new Vertex(getX(), getZ() - radius));	// 5
		points.add(new Vertex(getX() - radius*(float)Math.sqrt(2)/2, getZ() - radius*(float)Math.sqrt(2)/2));	// 6
		points.add(new Vertex(getX() - radius, getZ()));	// 7
		points.add(new Vertex(getX() - radius*(float)Math.sqrt(2)/2, getZ() + radius*(float)Math.sqrt(2)/2));	// 8

		for(Vertex point : points)
		{
			for(int i=0; i<vertex.size(); i++)
				if(point.X <= vertex.get(0).X && point.X >= vertex.get(2).X && 
				   point.Z <= vertex.get(1).Z && point.Z >= vertex.get(0).Z)
						return true;
		}
		
		return false;
	}
	
	
}