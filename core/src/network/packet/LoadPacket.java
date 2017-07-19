package network.packet;

import network.ServerThread;
import network.Server;

public class LoadPacket extends Packet
{
	String map;
	
	public LoadPacket(String _map)
	{
		map = _map;
	}

	@Override
	public void writeData(ServerThread client) {}

	@Override
	public void writeData(Server server) {}
	
	@Override
	public byte[] getData() {return null;}

	@Override
	public String toString ()
	{
		return ("load,"+map);
	}

}
