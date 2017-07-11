package entity;

public enum Weapons
{
	MACE(70,0), SWORD(60,200);
	
	public float damage;
	public float price;
	
	private Weapons(float _damage, float _price)
	{
		this.damage = _damage;
		this.price = _price;
	}
}
