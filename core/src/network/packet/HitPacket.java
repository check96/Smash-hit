package network.packet;

import videogame.GameConfig;

public class HitPacket extends Packet 
{
	private int i;
	private int j;
	private int id;
	private int room;
	private int health;
	
	public HitPacket(int _i, int _j, int health)
	{
		room = GameConfig.actualLevel-1;
		id = GameConfig.ID;
		i = _i;
		j = _j;
		this.health = health;
	}

	@Override
	public String toString() 
	{
		return ("hit," + id + "," + room + "," + i + "," + j + "," + health);
	}

}
