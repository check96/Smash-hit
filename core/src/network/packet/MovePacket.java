package network.packet;

import com.badlogic.gdx.math.Vector3;

import videogame.GameConfig;

public class MovePacket extends Packet
{
	private float x,y,z;
	private String key;
	
	public MovePacket(Vector3 direction, String _key)
	{
		x = direction.x;
		y = direction.y;
		z = direction.z;
		key = _key;
		username = GameConfig.username;;
	}
		
	@Override
	public String toString() {return ("move," + username + "," + key + "," + x + "," + y + "," + z);}
	
	
}
