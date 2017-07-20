package network.packet;

import com.badlogic.gdx.math.Vector3;

import videogame.GameConfig;

public class MovePacket extends Packet
{
	private float x,y,z;
	private String key;
	private int id;
	
	public MovePacket(Vector3 direction, String _key)
	{
		id = GameConfig.ID;
		x = direction.x;
		y = direction.y;
		z = direction.z;
		key = _key;
	}
		
	@Override
	public String toString() {return ("move," + id + "," + key + "," + x + "," + y + "," + z);}
	
	
}
