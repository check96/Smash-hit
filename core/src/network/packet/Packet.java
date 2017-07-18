package network.packet;

import network.ServerThread;
import network.Server;

public abstract class Packet
{
	protected String username;
	
	public static PacketType findType(String ID)
	{
		int id = Integer.parseInt(ID);

		
		for(PacketType p : PacketType.values()) 
			if(p.id == id) 
				return p;

		return PacketType.NULL;
	}
	
	public abstract void writeData(ServerThread client);
	public abstract void writeData(Server server);
	public abstract byte[] getData();
	
	public final String getUsername() { return username;}
}
