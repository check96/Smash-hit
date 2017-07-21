package network.packet;

import videogame.GameConfig;

public class LoadPacket extends Packet
{
	private String map;
	
	public LoadPacket(String _map)
	{
		map = _map;
	}

	@Override
	public String toString ()
	{
		return ("load,"+GameConfig.ID + "," + map);
	}

}
