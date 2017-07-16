package packet;

import network.Client;
import network.Server;

public class movePacket extends Packet
{
	private float x, y, z;
	
	public movePacket(byte[] data)
	{
		String[] info = new String(data).split(",");
		this.username = info[0];
		this.x = Float.parseFloat(info[1]);
		this.y = Float.parseFloat(info[2]);
		this.z = Float.parseFloat(info[3]);	
	}
	
	@Override
	public void writeData(Client client) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeData(Server server) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte[] getData() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public final float getX() {return x;}
	public final float getY() {return y;}
	public final float getZ() {return z;}
}
