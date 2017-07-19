package network.packet;

public enum PacketType
{
	NULL(0), LOAD(1), MOVE(2), HIT(3);
	
	public int id;
	
	private PacketType(int id)
	{
		this.id = id;
	}
	
}
