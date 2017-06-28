package entity;

import com.badlogic.gdx.math.Vector3;

public class Wall {

	private Vector3 position;
	public Walls type;
	
	public Wall(Vector3 _position, Walls _type)
	{
		this.position = _position;
		this.type = _type;
	}

	public Vector3 getPosition() { return position; }

}
