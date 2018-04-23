package Kind;

/**
 * 
 * Enemy Types
 * 
 * @author kyungyoonkim
 *
 */
public enum EnemyType {
	
	Level1("res/monster/monster1.png", 0.20, 0.20, 100, 25, 50),
	Level2("res/monster/monster2.png", 0.50, 0.50, 150, 25, 75);
	
	private String 	imgURI 		= null;
	private double 	velocityX 	= .0;
	private double 	velocityY 	= .0;
	private int 	maxHP 		= 0;
	private int 	volume 		= 0;
	private int 	money		= 0;
	
	private EnemyType(String imgURI, double velocityX, double velocityY, int maxHP, int volume, int money)
	{
		this.imgURI = imgURI;
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.maxHP = maxHP;
		this.volume = volume;
		this.money = money;
	}
	
	public String getImgURI() {
		return imgURI;
	}

	public double getVelocityX() {
		return velocityX;
	}

	public double getVelocityY() {
		return velocityY;
	}

	public int getMaxHP() {
		return maxHP;
	}
	
	public int getVolume() {
		return volume;
	}
	
	public int getMoney() {
		return money;
	}
}
