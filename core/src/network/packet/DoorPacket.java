package network.packet;

import videogame.GameConfig;

public class DoorPacket extends Packet 
{
	private int id;
	
	public DoorPacket()
	{
		id = GameConfig.ID;
	}
	
	@Override
	public String toString()
	{
		return ("door,"+id);
	}
}
