package Kind;

/**
 * 
 * Tower Types
 * 
 * @author kyungyoonkim
 *
 */
public enum TowerType {
	
	Tier1("res/tower/tower1.png", 500, 1000.0, ShootType.MagicBall, 500),
	Tier2("res/tower/tower2.png", 500, 300.0, ShootType.Fire, 250),
	Tier3("res/tower/tower3.png", 500, 300.0, ShootType.Ice, 100);
	
	private String		imageURI		= null;
	private int 		shootRange 		= 0;
	private double 		shootDelay 		= 0;
	private ShootType 	shootType 		= ShootType.Null;
	private int			price 		    = 0;
	
	private TowerType(String imageURI, int shootRange, double shootDelay, ShootType shootType, int price)
	{
		this.imageURI = imageURI;
		this.shootRange = shootRange;
		this.shootDelay = shootDelay;
		this.shootType = shootType;
		this.price = price;
	}

	public String getImageURI() {
		return imageURI;
	}

	public int getShootRange() {
		return shootRange;
	}

	public double getShootDelay() {
		return shootDelay;
	}

	public ShootType getShootType() {
		return shootType;
	}
	
	public int getPrice() {
		return price;
	}
	

	
}
