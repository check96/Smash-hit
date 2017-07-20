package network.packet;

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
