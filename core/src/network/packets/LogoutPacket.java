package network.packets;

import videogame.GameConfig;

public class LogoutPacket extends Packet
{
	private int id;
	
	public LogoutPacket()
	{
		id = GameConfig.ID;
	}
	
	@Override
	public String toString()
	{
		return("logout," + id);
	}
}
