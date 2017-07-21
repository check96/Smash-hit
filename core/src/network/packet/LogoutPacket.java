package network.packet;

import videogame.GameConfig;

public class LogoutPacket
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
