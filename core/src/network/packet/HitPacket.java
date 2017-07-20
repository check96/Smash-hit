package network.packet;

import videogame.GameConfig;

public class HitPacket extends Packet 
{
	private int i;
	private int j;
	private int id;
	private int room;
	
	public HitPacket(int _i, int _j)
	{
		room = GameConfig.actualLevel-1;
		id = GameConfig.ID;
		i = _i;
		j = _j;
	}

	@Override
	public String toString() 
	{
		return ("hit," + id + "," + room + "," + i + "," + j);
	}

}
