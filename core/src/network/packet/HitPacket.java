package network.packet;

import videogame.GameConfig;

public class HitPacket extends Packet 
{
	private int i;
	private int j;
	private int id;
	private int health;
	
	public HitPacket(int _i, int _j, int health)
	{
		id = GameConfig.ID;
		i = _i;
		j = _j;
		this.health = health;
	}

	@Override
	public String toString() 
	{
		return ("hit," + id + "," + i + "," + j + "," + health);
	}

}
