package entity;

public enum Weapons
{
	MACE(50,0), SWORD(60,200);
	
	public float price;
	public float damage;
	
	private Weapons(float _damage, float _price)
	{
		this.damage = _damage;
		this.price = _price;
	}
}
