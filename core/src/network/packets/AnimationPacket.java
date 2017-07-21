package network.packets;

import videogame.GameConfig;

public class AnimationPacket extends Packet
{
	private int id;
	private String type;
	
	public AnimationPacket(String type)
	{
		this.id = GameConfig.ID;
		this.type = type;
	}
	
	@Override
	public String toString()
	{
		return ("animation," + id + "," + type);
	}
}
