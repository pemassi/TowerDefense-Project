package Kind;

public enum ShootType{
	Bullet("res/projectile/bullet.png", 5, 10, 10, 15), 
	Fire("res/projectile/fire.png", 5, 5, 25, 15), 
	Ice("res/projectile/ice.png", 5, 5, 10, 15), 
	MagicBall("res/projectile/magicball.png", 5, 5, 50, 20), 
	Null(null, 0, 0, 0, 0);
	
	
	private String 	imgURI 		= null;
	private double 	velocityX 	= .0;
	private double 	velocityY 	= .0;
	private int 	volume 		= 0;
	private int 	damage 		= 0;
	
	/**
	 * 
	 * 
	 * @param imgURI
	 * @param velocityX
	 * @param velocityY
	 * @param damage
	 * @param volume
	 */
	private ShootType(String imgURI, double velocityX, double velocityY, int damage, int volume) {
		this.imgURI = imgURI;
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.damage = damage;
		this.volume = volume;
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

	public int getVolume() {
		return volume;
	}

	public int getDamage() {
		return damage;
	}

}

