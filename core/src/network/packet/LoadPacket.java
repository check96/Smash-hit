package network.packet;

import videogame.GameConfig;

public class LoadPacket extends Packet
{
	private String map;
	private int level;
	
	public LoadPacket(String _map, int level)
	{
		map = _map;
		this.level = level;
	}

	@Override
	public String toString ()
	{
		return ("load,"+GameConfig.ID + "," + map + "," + level);
	}

}
