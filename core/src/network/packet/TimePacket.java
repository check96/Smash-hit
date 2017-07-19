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
	public String toString() {
		return ("time,"+time);
	}
}
