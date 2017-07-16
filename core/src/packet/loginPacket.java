package packet;

import network.Client;
import network.Server;

public class loginPacket extends Packet
{
	private float x,y,z;
	
	public loginPacket(byte[] data)
	{
		String[] info = new String(data).split(",");
		this.username = info[0];
		this.x = Float.parseFloat(info[1]);
		this.y = Float.parseFloat(info[2]);
		this.z = Float.parseFloat(info[3]);
	}

	@Override
	public void writeData(Client client) {
		 

	}

	@Override
	public void writeData(Server server) {
		// TODO Auto-generated method stub

	}

	@Override
	public byte[] getData()
	{
		return (this.username + "," + this.x + "," + this.y + "," + this.z).getBytes();
	}
	
	public final float getX() {return x;}
	public final float getY() {return y;}
	public final float getZ() {return z;}
}
