package network.packet;

import com.badlogic.gdx.math.Vector3;

import videogame.GameConfig;

public class MovePacket extends Packet
{
	private float x,y,z;
	private int angle;
	private int id;
	
	public MovePacket(Vector3 position, int angle)
	{
		id = GameConfig.ID;
		x = position.x;
		y = position.y;
		z = position.z;
		this.angle = angle;
	}
		
	@Override
	public String toString() {return ("move," + id + "," + angle + "," + x + "," + y + "," + z);}
	
	
}
