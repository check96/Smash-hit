package entity;

import com.badlogic.gdx.math.Vector3;

public class Wall extends AbstractGameObject{

	public Walls type;
	
	public Wall(Vector3 _position, Walls _type)
	{
		super(_position,_type.size);
		this.type = _type;
	}
}
