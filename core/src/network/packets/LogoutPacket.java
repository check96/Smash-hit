package network.packets;

import videogame.GameConfig;

public class LogoutPacket extends Packet
{
	private int id;
	private String message;
	
	public LogoutPacket(String message)
	{
		this.message = message;
		id = GameConfig.ID;
	}
	
	@Override
	public String toString()
	{
		return("logout," + id + "," + message);
	}
}
