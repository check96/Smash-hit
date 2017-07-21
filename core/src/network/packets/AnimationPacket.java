package network.packets;

import videogame.GameConfig;

public class AnimationPacket extends Packet
{
	private int id;
	private String type;
	private long time;
	
	public AnimationPacket(String type, long time)
	{
		this.id = GameConfig.ID;
		this.type = type;
		this.time = time;
	}
	
	@Override
	public String toString()
	{
		return ("animation," + id + "," + type + "," + time);
	}
}
