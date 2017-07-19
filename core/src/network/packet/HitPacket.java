package network.packet;

import videogame.GameConfig;

public class HitPacket extends Packet 
{
	int i;
	int j;

	public HitPacket(int _i, int _j)
	{
		username = GameConfig.username;
		i = _i;
		j = _j;
	}

	@Override
	public String toString() 
	{
		return ("hit,"+i+","+j);
	}

}
