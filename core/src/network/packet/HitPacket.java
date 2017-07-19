package network.packet;

import network.ServerThread;
import network.Server;

public class HitPacket extends Packet 
{
	int i;
	int j;

	public HitPacket(int _i, int _j)
	{
		i = _i;
		j = _j;
	}

	@Override
	public String toString() 
	{
		return ("hit,"+i+","+j);
	}

}
