package network.packet;

import videogame.GameConfig;

public class LoadPacket extends Packet
{
	String map;
	
	public LoadPacket(String _map)
	{
		map = _map;
	}

	@Override
	public String toString ()
	{
		return ("load,"+map);
	}

}
