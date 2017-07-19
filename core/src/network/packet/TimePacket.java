package network.packet;

import network.Server;
import network.ServerThread;

public class TimePacket extends Packet 
{
	int time;
	
	public TimePacket(int _time) 
	{
		time =_time;
	}
	
	@Override
	public void writeData(ServerThread client) {}

	@Override
	public void writeData(Server server) {}

	@Override
	public byte[] getData() {return null;}
	
	@Override
	public String toString() {
		return ("time,"+time);
	}
}
