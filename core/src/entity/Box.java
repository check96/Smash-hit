package entity;

import com.badlogic.gdx.math.Vector3;

public class Box
{
	public Vector3 Size;
	public Vector3 Position;
	
	public Box(Vector3 _position, Vector3 _size)
	{
		Size = _size;
		Position = _position;
	}
}
