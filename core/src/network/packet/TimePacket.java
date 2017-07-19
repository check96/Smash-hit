package network.packet;

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
